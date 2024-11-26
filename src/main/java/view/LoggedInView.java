package view;

import entity.Article;
import entity.CommonArticle;
import interface_adapter.digest.DigestController;
import interface_adapter.logged_in.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JPanel categoryButtonsPanel;
    private final JPanel articlePanel;

    private final AddCategoryController addCategoryController;
    private final RemoveCategoryController removeCategoryController;
    private final DigestController digestController;
    private final SaveArticleController saveArticleController;
    private final UnsaveArticleController unsaveArticleController;

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        AddCategoryController addCategoryController,
                        RemoveCategoryController removeCategoryController,
                        DigestController digestController,
                        SaveArticleController saveArticleController,
                        UnsaveArticleController unsaveArticleController) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // the initial state of the logged in view, containing the current user and saved categories
        LoggedInState initalState = loggedInViewModel.getState();

        this.addCategoryController = addCategoryController;
        this.removeCategoryController = removeCategoryController;
        this.digestController = digestController;
        this.saveArticleController = saveArticleController;
        this.unsaveArticleController = unsaveArticleController;

        // Navbar Panel
        JPanel navigationPanel = new JPanel();
        // TODO add method for handling pressing the Saved Articles Button to switch view
        JButton savedArticlesButton = new JButton("Saved Articles");
        JLabel newsLabel = new JLabel("News");
        // TODO add method for handling pressing the Log out Button to switch view
        JButton logoutButton = new JButton("Log out");
        navigationPanel.add(savedArticlesButton);
        navigationPanel.add(newsLabel);
        navigationPanel.add(logoutButton);


        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Category:"));

        JTextField categoryField = new JTextField(16);
        inputPanel.add(categoryField);

        // add category button and use case
        JButton addCategoryButton = new JButton("Add Category");
        addCategoryButton.addActionListener(e -> {
            String category = categoryField.getText();  // Get the category name from the text field
            this.addCategoryController.execute(category); // Execute AddCategory use case
            categoryField.setText(""); // Clear the text field after adding the category
        });

        inputPanel.add(addCategoryButton);

        // Category Panel
        JPanel categoryPanel = new JPanel();

        categoryButtonsPanel = new JPanel();
        categoryButtonsPanel.setLayout(new FlowLayout());
        // Populate the categoryButtonsPanel with the saved categories of the current user
        for (String category: initalState.getCategoriesList()){
            JButton categoryButton = createCategoryButton(category);
            categoryButtonsPanel.add(categoryButton);
        }
        categoryButtonsPanel.revalidate();    // Revalidate the layout
        categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes

        JButton generateButton = createGenerateButton();

        // inputPanel.add(generateButton);  // consider putting this in the input panel
        categoryPanel.add(inputPanel);
        categoryPanel.add(categoryButtonsPanel);
        categoryPanel.add(generateButton);

        // Article Panel
        articlePanel = new JPanel();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(navigationPanel);
        this.add(categoryPanel);
        this.add(articlePanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().startsWith("add category: ")){
            String category = evt.getPropertyName().substring("add category: ".length());
            JButton categoryButton = createCategoryButton(category);
            categoryButtonsPanel.add(categoryButton);
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        }

        // Populate the articlePanel with articles for each article the user generates.
        if (evt.getPropertyName().startsWith("articles")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            articlePanel.removeAll();

            for (Article article: state.getArticleList()){
                JPanel articleSlide = new JPanel();
                JLabel articleTitle = new JLabel(article.getTitle());
                JLabel articleAuthor = new JLabel(article.getAuthor());
                JLabel articleDate = new JLabel(article.getDate());
                JLabel articleLink = new JLabel(article.getLink());
                JLabel articleDescription = new JLabel(article.getDescription());

                JButton saveButton = createSaveButton(article);
                JButton unsaveButton = createUnsaveButton(article);

                articleSlide.add(articleTitle);
                articleSlide.add(articleAuthor);
                articleSlide.add(articleDate);
                articleSlide.add(articleLink);
                articleSlide.add(articleDescription);
                articleSlide.add(saveButton);
                articleSlide.add(unsaveButton);
                articlePanel.add(articleSlide);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    private JButton createCategoryButton(String category) {
        JButton categoryButton = new JButton(category);
        categoryButton.addActionListener(e -> {
            // execute remove category use case
            this.removeCategoryController.execute(category);

            // Remove this button from the panel
            categoryButtonsPanel.remove(categoryButton);
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        });
        return categoryButton;
    }

    private JButton createGenerateButton() {
        JButton generateButton = new JButton("Generate");
        generateButton.setBackground(Color.GREEN);
        generateButton.addActionListener(e -> {
            // execute digest use case
            this.digestController.execute(loggedInViewModel.getState().getCategoriesList().toArray(new String[0]));

            generateButton.setBackground(Color.BLUE);

        });
        return generateButton;
    }

    private JButton createSaveButton(Article article) {
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN);

        saveButton.addActionListener(e -> {
            // execute digest use case
            this.saveArticleController.execute(article);
        });
        return saveButton;
    }

    private JButton createUnsaveButton(Article article) {
        JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(e -> {
            // execute digest use case
            this.unsaveArticleController.execute(article);
        });
        return unsaveButton;
    }
}
