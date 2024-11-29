package view;

import entity.Article;
import interface_adapter.saved_articles.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SavedArticlesView extends JPanel implements PropertyChangeListener {
    private final String viewName = "saved articles";
    private final SavedArticlesViewModel savedArticlesViewModel;
    private final JPanel filterPanel;
    private final JPanel articlesPanel;

    private final AddCategoryController addCategoryController;
    private final RemoveCategoryController removeCategoryController;
    private final UnsaveArticleController unsaveArticleController;
    private final NewsController newsController;
    private final PerformFilterController performFilterController;

    public SavedArticlesView(SavedArticlesViewModel savedArticlesViewModel,
                             AddCategoryController addCategoryController,
                             RemoveCategoryController removeCategoryController,
                             UnsaveArticleController unsaveArticleController,
                             NewsController newsController,
                             PerformFilterController performFilterController) {
        this.savedArticlesViewModel = savedArticlesViewModel;
        this.savedArticlesViewModel.addPropertyChangeListener(this);

        SavedArticlesState savedArticlesState = this.savedArticlesViewModel.getState();

        this.addCategoryController = addCategoryController;
        this.removeCategoryController = removeCategoryController;
        this.unsaveArticleController = unsaveArticleController;
        this.newsController = newsController;
        this.performFilterController = performFilterController;

        // Navbar Panel
        JPanel navigationPanel = new JPanel();
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

        // Perform filter button and use case
        JButton performFilterButton = new JButton("Perform Filter");
        performFilterButton.addActionListener(e -> {
            this.performFilterController.execute();
        });

        inputPanel.add(addFilterButton);
        inputPanel.add(performFilterButton);

        // Category filter button panel
        filterPanel = new JPanel();
        for (String category: savedArticlesState.getCategoriesFilterList()){
            JButton categoryButton = createCategoryButton(category);
            filterPanel.add(categoryButton);
        }


        // Article Panel
        articlesPanel = new JPanel();
        JScrollPane articlesScrollPane = new JScrollPane(articlesPanel);
        articlesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(navigationPanel);
        this.add(categoriesFilter);
        this.add(articlesPanel);
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

    // refresh the article panel to show new articles generated, following the digest use case
    private void refreshArticlePanel(SavedArticlesState state) {
        articlesPanel.removeAll();

        for (Article article: state.getArticleList()){
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


            // Add a divider (separator) after each article
            JSeparator separator = new JSeparator();
            articlesPanel.add(articleSlide);
            articlesPanel.add(separator);

            articlesPanel.revalidate();    // Revalidate the layout
            articlesPanel.repaint();       // Repaint the panel to reflect the changes
        }
    }

    private JButton createUnsaveButton(Article article) {
        JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(e -> {
            // execute unsave article use case
            this.unsaveArticleController.execute(article);
            articlesPanel.revalidate();    // Revalidate the layout
            articlesPanel.repaint();       // Repaint the panel to reflect the changes
        });
        return unsaveButton;
    }

    private JButton createShareButton(Article article) {
        JButton shareButton = new JButton("Share to my email");
        shareButton.setBackground(Color.BLUE);

        shareButton.addActionListener(e -> {
            // execute share article use case
            JOptionPane.showMessageDialog(
                    this,
                    "Sharing article: " + article.getTitle(),
                    "Share",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // When I have a shareArticleController, replace the above line with:
            // this.shareArticleController.execute(article);
        });
        return shareButton;
    }
}
