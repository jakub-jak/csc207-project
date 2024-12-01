package view;

import entity.Article;
import interface_adapter.logged_in.AddCategoryController;
import interface_adapter.logged_in.RemoveCategoryController;
import interface_adapter.logged_in.ShareArticleController;
import interface_adapter.logged_in.UnsaveArticleController;
import interface_adapter.saved_articles.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SavedArticlesView extends JPanel implements PropertyChangeListener {
    private final String viewName = "saved articles";
    private final SavedArticlesViewModel savedArticlesViewModel;
    private final JPanel filterPanel;
    private final JPanel articlesPanel;

    private AddCategoryController addCategoryController;
    private RemoveCategoryController removeCategoryController;
    private UnsaveArticleController unsaveArticleController;
    private ShareArticleController shareArticleController;
    private NewsController newsController;

    public SavedArticlesView(SavedArticlesViewModel savedArticlesViewModel) {
        this.savedArticlesViewModel = savedArticlesViewModel;
        this.savedArticlesViewModel.addPropertyChangeListener(this);

        SavedArticlesState savedArticlesState = this.savedArticlesViewModel.getState();

        // Panels
        JPanel navigationPanel = new JPanel();
        filterPanel = new JPanel();
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        // NavBar
        JButton newsButton = new JButton("News");
        newsButton.addActionListener(e -> {
            this.newsController.execute();
        });
        JLabel savedArticlesTitleLabel = new JLabel("Saved Articles");
        // TODO: add action listener to go to the login view
        JButton LogoutButton = new JButton("Log out");
        navigationPanel.add(newsButton);
        navigationPanel.add(savedArticlesTitleLabel);
        navigationPanel.add(LogoutButton);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Filter By Categories:"));
        JTextField categoriesFilter = new JTextField(16);
        inputPanel.add(categoriesFilter);

        // Add category filter button and use case
        JButton addFilterButton = new JButton("Add Filter");
        addFilterButton.addActionListener(e -> {
            String category = categoriesFilter.getText();  // Get the category name from the text field
            this.addCategoryController.execute(category); // Execute AddCategory use case
            categoriesFilter.setText(""); // Clear the text field after adding the category
        });

        // Perform filter button
        JButton performFilterButton = new JButton("Perform Filter");
        performFilterButton.addActionListener(e -> {
            // Filters the Articles based on the current
            articlesPanel.removeAll();
            
            for (String category: savedArticlesState.getCategoriesFilterList()) {
                for (Article article : savedArticlesState.getArticlesByCategory(category)) {
                    JPanel articleSlide = getArticleSlide(article);

                    // Add a divider (separator) after each article
                    JSeparator separator = new JSeparator();
                    articlesPanel.add(articleSlide);
                    articlesPanel.add(separator);

                    articlesPanel.revalidate();    // Revalidate the layout
                    articlesPanel.repaint();       // Repaint the panel to reflect the changes
                }
            }
        });

        inputPanel.add(addFilterButton);
        inputPanel.add(performFilterButton);

        // Category filter button panel
        for (String category: savedArticlesState.getCategoriesFilterList()){
            JButton categoryButton = createCategoryButton(category);
            filterPanel.add(categoryButton);
        }


        // Article Panel
        JScrollPane articlesScrollPane = new JScrollPane(articlesPanel);


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(navigationPanel);
        //this.add(inputPanel); Remove because it is not working correctly and I don't have time to fix it
        this.add(articlesScrollPane);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().startsWith("add category: ")){
            String category = evt.getPropertyName().substring("add category: ".length());
            JButton categoryButton = createCategoryButton(category);
            filterPanel.add(categoryButton);
            filterPanel.revalidate();    // Revalidate the layout
            filterPanel.repaint();       // Repaint the panel to reflect the changes
        }

        if (evt.getPropertyName().equals("articles")){
            final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
            refreshArticlePanel(savedArticlesState);
        }
    }

    private JButton createCategoryButton(String category) {
        JButton categoryButton = new JButton(category);
        categoryButton.addActionListener(e -> {
            // execute remove category use case
            this.removeCategoryController.execute(category);

            // Remove this button from the panel
            filterPanel.remove(categoryButton);
            filterPanel.revalidate();    // Revalidate the layout
            filterPanel.repaint();       // Repaint the panel to reflect the changes
        });
        return categoryButton;
    }

    // refresh the article panel to show new articles generated,
    private void refreshArticlePanel(SavedArticlesState state) {
        articlesPanel.removeAll();

        for (Article article: state.getArticleList()){
            JPanel articleSlide = getArticleSlide(article);
            
            // Add a divider (separator) after each article
            JSeparator separator = new JSeparator();
            articlesPanel.add(articleSlide);
            articlesPanel.add(separator);

            articlesPanel.revalidate();    // Revalidate the layout
            articlesPanel.repaint();       // Repaint the panel to reflect the changes
        }
    }

    @NotNull
    private JPanel getArticleSlide(Article article) {
        JPanel articleSlide = new JPanel();
        articleSlide.setLayout(new BoxLayout(articleSlide, BoxLayout.Y_AXIS)); // Stack components vertically

        // Title
        JLabel articleTitle = new JLabel(article.getTitle());
        articleTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Author
        JLabel articleAuthor = new JLabel(article.getAuthor());
        articleAuthor.setFont(new Font("Arial", Font.PLAIN, 12));

        // Date
        JLabel articleDate = new JLabel(article.getDate());
        articleDate.setFont(new Font("Arial", Font.PLAIN, 12));

        // Link
        JLabel articleLink = new JLabel(article.getLink());
        articleLink.setFont(new Font("Arial", Font.PLAIN, 12));
        articleLink.setForeground(Color.BLUE);

        // Description
        JTextArea articleDescription = new JTextArea(article.getDescription());
        articleDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        articleDescription.setLineWrap(true);
        articleDescription.setWrapStyleWord(true);
        articleDescription.setEditable(false); // Disable editing
        articleDescription.setBackground(articleSlide.getBackground()); // Make the background same as articleSlide

        // Calculate max width of the description as half of the window size
        int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2; // Get half the screen width
        articleDescription.setPreferredSize(new Dimension(maxWidth, 100)); // Set preferred size with max width and some height
        JScrollPane descriptionScrollPane = new JScrollPane(articleDescription);

        // un-save / share buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createUnsaveButton(article));
        buttonPanel.add(createShareButton(article));

        // Add labels to article slide panel
        articleSlide.add(articleTitle);
        articleSlide.add(articleAuthor);
        articleSlide.add(articleDate);
        articleSlide.add(articleLink);
        articleSlide.add(descriptionScrollPane); // Scrollable description
        articleSlide.add(buttonPanel);
        return articleSlide;
    }

    private JButton createUnsaveButton(Article article) {
        JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(e -> {
            // execute unsave article use case
            this.unsaveArticleController.execute(article);
            articlesPanel.revalidate();    // Revalidate the layout
            articlesPanel.repaint();       // Repaint the panel to reflect the changes

            // Display a success message
            JOptionPane.showMessageDialog(
                    null,
                    "The article has been successfully unsaved and will not appear under Saved Articles " +
                            "anymore.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        return unsaveButton;
    }

    private JButton createShareButton(Article article) {
        JButton shareButton = new JButton("Share to my email");
        shareButton.setBackground(Color.BLUE);

        shareButton.addActionListener(e -> {
            try {
                this.shareArticleController.execute(article);
                // Show a success popup message
                JOptionPane.showMessageDialog(
                        null,
                        "The article has been sent to your email! Please check your inbox.",
                        "Email Sent",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                // Show an error popup message in case of an exception
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred while sending the email. Please try again. " +
                                "You might want to check if the email address you signed-up with is a valid one.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                throw new RuntimeException(ex);
            }
        });

        return shareButton;
    }

    public String getViewName() {
        return viewName;
    }

    public void setAddCategoryController(AddCategoryController addCategoryController) {
        this.addCategoryController = addCategoryController;
    }

    public void setRemoveCategoryController(RemoveCategoryController removeCategoryController) {
        this.removeCategoryController = removeCategoryController;
    }

    public void setUnsaveArticleController(UnsaveArticleController unsaveArticleController) {
        this.unsaveArticleController = unsaveArticleController;
    }

    public void setShareArticleController(ShareArticleController shareArticleController) {
        this.shareArticleController = shareArticleController;
    }

    public void setNewsController(NewsController newsController) {
        this.newsController = newsController;
    }
}
