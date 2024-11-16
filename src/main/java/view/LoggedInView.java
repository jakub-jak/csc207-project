package view;

import entity.Article;
import interface_adapter.logged_in.AddCategoryController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.RemoveCategoryController;

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

    public LoggedInView(LoggedInViewModel loggedInViewModel,
                        AddCategoryController addCategoryController,
                        RemoveCategoryController removeCategoryController) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // the initial state of the logged in view, containing the current user and saved categories
        LoggedInState initalState = loggedInViewModel.getState();

        this.addCategoryController = addCategoryController;
        this.removeCategoryController = removeCategoryController;

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
            this.addCategoryController.execute(initalState.getUsername(), category); // Execute AddCategory use case
            categoryField.setText(""); // Clear the text field after adding the category
        });

        inputPanel.add(addCategoryButton);

        // Category Panel
        JPanel categoryPanel = new JPanel();

        categoryButtonsPanel = new JPanel();
        categoryButtonsPanel.setLayout(new FlowLayout());
        // Populate the categoryButtonsPanel with the saved categories of the current user
        for (String category: initalState.getCategoriesList()){
            JButton categoryButton = createCategoryButton(initalState.getUsername(), category);
            categoryButtonsPanel.add(categoryButton);
        }
        categoryButtonsPanel.revalidate();    // Revalidate the layout
        categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes

        // TODO add method for handling pressing the generate button
        JButton generateButton = new JButton("Generate");

        // inputPanel.add(generateButton);  // consider putting this in the input panel
        categoryPanel.add(inputPanel);
        categoryPanel.add(categoryButtonsPanel);
        categoryPanel.add(generateButton);

        // Article Panel
        articlePanel = new JPanel();


        this.add(navigationPanel, BorderLayout.NORTH);
        this.add(categoryPanel, BorderLayout.CENTER);
        this.add(articlePanel, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().startsWith("add category: ")){
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            String user = state.getUsername();
            String category = evt.getPropertyName().substring("add category: ".length());
            JButton categoryButton = createCategoryButton(user, category);
            categoryButtonsPanel.add(categoryButton);
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        }

        // TODO add propertyChangeEvent for category
        // Populate the categoryPanel with buttons for each category that the user has.
//        if (evt.getPropertyName().contains("category")) {
//            final LoggedInState state = (LoggedInState) evt.getNewValue();
//            String user = state.getUsername();
//
//            categoryButtonsPanel.removeAll();
//            for (String category: state.getCategoriesList()){
//                JButton categoryButton = createCategoryButton(user, category);
//                categoryButtonsPanel.add(categoryButton);
//            }
//        }

        // TODO add propertyChangeEvent for articles
        // Populate the articlePanel with articles for each article the user generates.
        if (evt.getPropertyName().equals("articles")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            articlePanel.removeAll();
            for (Article article: state.getArticleList()){
                JPanel articleSlide = new JPanel();
                JLabel articleTitle = new JLabel(article.getTitle());
                JLabel articleAuthor = new JLabel(article.getAuthor());
                JLabel articleDate = new JLabel(article.getDate());
                JLabel articleLink = new JLabel(article.getLink());
                JLabel articleDescription = new JLabel(article.getDescription());
                JButton saveButton = new JButton("Save");
                // TODO add actionListener to save button that saves it to the user DB when it is pressed
                saveButton.addActionListener(e -> {});
                articleSlide.add(articleTitle);
                articleSlide.add(articleAuthor);
                articleSlide.add(articleDate);
                articleSlide.add(articleLink);
                articleSlide.add(articleDescription);
                articleSlide.add(saveButton);
                articlePanel.add(articleSlide);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    private JButton createCategoryButton(String user, String category) {
        JButton categoryButton = new JButton(category);
        categoryButton.addActionListener(e -> {
            // execute remove category use case
            this.removeCategoryController.execute(user, category);

            // Remove this button from the panel
            categoryButtonsPanel.remove(categoryButton);
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        });
        return categoryButton;
    }
}
