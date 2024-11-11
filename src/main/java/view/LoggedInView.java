package view;

import entity.Article;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

import javax.swing.*;
import java.awt.*;
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

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

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

        // Category Panel
        JPanel categoryPanel = new JPanel();
        JPanel inputPanel = new JPanel();
        JTextField categoryField = new JTextField(16);
        inputPanel.add(new JLabel("Enter Category:"));
        // TODO add method for handling pressing the generate button
        JButton generateButton = new JButton("Generate");
        inputPanel.add(categoryField);
        categoryButtonsPanel = new JPanel();
        categoryPanel.add(inputPanel);
        categoryPanel.add(categoryButtonsPanel);
        categoryButtonsPanel.add(generateButton);

        // Article Panel
        articlePanel = new JPanel();


        this.add(navigationPanel, BorderLayout.NORTH);
        this.add(categoryPanel, BorderLayout.CENTER);
        this.add(articlePanel, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // TODO add propertyChangeEvent for category
        // Populate the categoryPanel with buttons for each category that the user has.
        if (evt.getPropertyName().equals("category")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            categoryButtonsPanel.removeAll();
            for (String category: state.getCategoriesList()){
                JButton categoryButton = new JButton(category);
                // TODO add actionListener to each button that deletes it when it is pressed
                categoryButton.addActionListener(e -> {});
                categoryButtonsPanel.add(categoryButton);
            }
        }

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
}
