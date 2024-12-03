package view;

import entity.Article;
import interface_adapter.logged_in.AddCategoryController;
import interface_adapter.logged_in.RemoveCategoryController;
import interface_adapter.logged_in.ShareArticleController;
import interface_adapter.logged_in.UnsaveArticleController;
import interface_adapter.logout.LogoutController;
import interface_adapter.saved_articles.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Saved Articles View.
 */
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
    private LogoutController logoutController;

    public SavedArticlesView(SavedArticlesViewModel savedArticlesViewModel) {
        this.savedArticlesViewModel = savedArticlesViewModel;
        this.savedArticlesViewModel.addPropertyChangeListener(this);

        final SavedArticlesState savedArticlesState = this.savedArticlesViewModel.getState();

        // Panels
        final JPanel navigationPanel = new JPanel();
        filterPanel = new JPanel();
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        // NavBar
        final JButton newsButton = new JButton("News");
        newsButton.addActionListener(e -> {
            this.newsController.execute();
        });
        final JLabel savedArticlesTitleLabel = new JLabel("Saved Articles");

        navigationPanel.add(newsButton);
        navigationPanel.add(savedArticlesTitleLabel);
        navigationPanel.add(createLogoutButton());

        // Input Panel
        final JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Filter By Categories:"));
        final JTextField categoriesFilter = new JTextField(16);
        inputPanel.add(categoriesFilter);

        // Add category filter button and use case
        final JButton addFilterButton = new JButton("Add Filter");
        addFilterButton.addActionListener(actionEvent -> {
            final String category = categoriesFilter.getText();
            this.addCategoryController.execute(category);
            categoriesFilter.setText("");
        });

        // Perform filter button
        final JButton performFilterButton = new JButton("Perform Filter");
        performFilterButton.addActionListener(actionEvent -> {
            // Filters the Articles based on the current
            articlesPanel.removeAll();
            
            for (String category: savedArticlesState.getCategoriesFilterList()) {
                for (Article article : savedArticlesState.getArticlesByCategory(category)) {
                    final JPanel articleSlide = getArticleSlide(article);

                    // Add a divider (separator) after each article
                    final JSeparator separator = new JSeparator();
                    articlesPanel.add(articleSlide);
                    articlesPanel.add(separator);

                    articlesPanel.revalidate();
                    articlesPanel.repaint();
                }
            }
        });

        inputPanel.add(addFilterButton);
        inputPanel.add(performFilterButton);

        // Category filter button panel
        for (String category: savedArticlesState.getCategoriesFilterList()) {
            final JButton categoryButton = createCategoryButton(category);
            filterPanel.add(categoryButton);
        }

        // Article Panel
        final JScrollPane articlesScrollPane = new JScrollPane(articlesPanel);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(navigationPanel);
        // this.add(inputPanel); Remove because it is not working correctly and I don't have time to fix it
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
        if (evt.getPropertyName().startsWith("add category: ")) {
            final String category = evt.getPropertyName().substring("add category: ".length());
            final JButton categoryButton = createCategoryButton(category);
            filterPanel.add(categoryButton);
            filterPanel.revalidate();
            filterPanel.repaint();
        }

        if (evt.getPropertyName().equals("articles")) {
            final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
            refreshArticlePanel(savedArticlesState);
        }
    }

    private JButton createLogoutButton() {
        final JButton generateButton = new JButton("Logout");
        generateButton.addActionListener(actionEvent -> {
            // execute logout use case
            this.logoutController.execute(savedArticlesViewModel.getState().getUsername());
        });
        return generateButton;
    }

    private JButton createCategoryButton(String category) {
        final JButton categoryButton = new JButton(category);
        categoryButton.addActionListener(actionEvent -> {
            // execute remove category use case
            this.removeCategoryController.execute(category);

            // Remove this button from the panel
            filterPanel.remove(categoryButton);
            filterPanel.revalidate();
            filterPanel.repaint();
        });
        return categoryButton;
    }

    // refresh the article panel to show new articles generated,
    private void refreshArticlePanel(SavedArticlesState state) {
        articlesPanel.removeAll();

        for (Article article: state.getArticleList()) {
            final JPanel articleSlide = getArticleSlide(article);
            
            // Add a divider (separator) after each article
            final JSeparator separator = new JSeparator();
            articlesPanel.add(articleSlide);
            articlesPanel.add(separator);

            articlesPanel.revalidate();
            articlesPanel.repaint();
        }
    }

    @NotNull
    private JPanel getArticleSlide(Article article) {
        final JPanel articleSlide = new JPanel();
        articleSlide.setLayout(new BoxLayout(articleSlide, BoxLayout.Y_AXIS));
        final String fontName = "Arial";
        final int fontSize12 = 12;
        final int fontSize14 = 14;

        // Title
        final JLabel articleTitle = new JLabel(article.getTitle());
        articleTitle.setFont(new Font(fontName, Font.BOLD, fontSize14));

        // Author
        final JLabel articleAuthor = new JLabel(article.getAuthor());
        articleAuthor.setFont(new Font(fontName, Font.PLAIN, fontSize12));

        // Date
        final JLabel articleDate = new JLabel(article.getDate());
        articleDate.setFont(new Font(fontName, Font.PLAIN, fontSize12));

        // Link
        final JLabel articleLink = new JLabel(article.getLink());
        articleLink.setFont(new Font(fontName, Font.PLAIN, fontSize12));
        articleLink.setForeground(Color.BLUE);

        // Description
        final JTextArea articleDescription = new JTextArea(article.getDescription());
        articleDescription.setFont(new Font(fontName, Font.PLAIN, fontSize12));
        articleDescription.setLineWrap(true);
        articleDescription.setWrapStyleWord(true);
        articleDescription.setEditable(false);
        articleDescription.setBackground(articleSlide.getBackground());

        // Calculate max width of the description as half of the window size
        final int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        final int height = 100;
        articleDescription.setPreferredSize(new Dimension(maxWidth, height));
        final JScrollPane descriptionScrollPane = new JScrollPane(articleDescription);

        // un-save / share buttons
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(createUnsaveButton(article));
        buttonPanel.add(createShareButton(article));

        // Add labels to article slide panel
        articleSlide.add(articleTitle);
        articleSlide.add(articleAuthor);
        articleSlide.add(articleDate);
        articleSlide.add(articleLink);
        articleSlide.add(descriptionScrollPane);
        articleSlide.add(buttonPanel);
        return articleSlide;
    }

    private JButton createUnsaveButton(Article article) {
        final JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(actionEvent -> {
            // execute unsave article use case
            this.unsaveArticleController.execute(article);
            articlesPanel.revalidate();
            articlesPanel.repaint();

            // Display a success message
            JOptionPane.showMessageDialog(
                    null,
                    "The article has been successfully unsaved and will not appear under Saved Articles "
                            + "anymore.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        return unsaveButton;
    }

    private JButton createShareButton(Article article) {
        final JButton shareButton = new JButton("Share to my email");
        shareButton.setBackground(Color.BLUE);

        shareButton.addActionListener(actionEvent -> {
            try {
                this.shareArticleController.execute(article);
                // Show a success popup message
                JOptionPane.showMessageDialog(
                        null,
                        "The article has been sent to your email! Please check your inbox.",
                        "Email Sent",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            catch (Exception ex) {
                // Show an error popup message in case of an exception
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred while sending the email. Please try again. "
                                + "You might want to check if the email address you signed-up with is a valid one.",
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

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setNewsController(NewsController newsController) {
        this.newsController = newsController;
    }
}
