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
import java.util.ArrayList;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private final JPanel categoryButtonsPanel;
    private final JPanel articlePanel;

    private AddCategoryController addCategoryController;
    private RemoveCategoryController removeCategoryController;
    private DigestController digestController;
    private SaveArticleController saveArticleController;
    private UnsaveArticleController unsaveArticleController;
    private ShareArticleController shareArticleController;
    private SavedArticlesController savedArticlesController;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // the initial state of the logged in view, containing the current user and saved categories
        LoggedInState initalState = loggedInViewModel.getState();

        // Navbar Panel
        JPanel navigationPanel = new JPanel();
        JButton savedArticlesButton = new JButton("Saved Articles");
        savedArticlesButton.addActionListener(e -> {
            this.savedArticlesController.execute();
        });
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

        JButton generateButton = createGenerateButton();

        // inputPanel.add(generateButton);  // consider putting this in the input panel
        categoryPanel.add(inputPanel);
        categoryPanel.add(categoryButtonsPanel);
        categoryPanel.add(generateButton);

        // Article Panel
        articlePanel = new JPanel();
        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.Y_AXIS));

        JScrollPane scrollArticlePanel = new JScrollPane(articlePanel);  // Wrap the articlePanel in a JScrollPane to make it scrollable
        scrollArticlePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(navigationPanel);
        this.add(categoryPanel);
        this.add(scrollArticlePanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Initilize
        if (evt.getPropertyName().equals("init")){
            // Populate the categoryButtonsPanel with the saved categories of the current user
            final LoggedInState state = loggedInViewModel.getState();
            for (String category: state.getCategoriesList()){
                JButton categoryButton = createCategoryButton(category);
                categoryButtonsPanel.add(categoryButton);
            }
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        }

        if (evt.getPropertyName().startsWith("add category: ")){
            String category = evt.getPropertyName().substring("add category: ".length());
            JButton categoryButton = createCategoryButton(category);
            categoryButtonsPanel.add(categoryButton);
            categoryButtonsPanel.revalidate();    // Revalidate the layout
            categoryButtonsPanel.repaint();       // Repaint the panel to reflect the changes
        }

        // Populate the articlePanel with articles for each article the user generates.
        if (evt.getPropertyName().equals("articles")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            refreshArticlePanel(state);
        }
    }

    public String getViewName() {
        return viewName;
    }

    // refresh the article panel to show new articles generated, following the digest use case
    private void refreshArticlePanel(LoggedInState state) {
        articlePanel.removeAll();

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

            // save / un-save / share buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(createSaveButton(article));
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
            articlePanel.add(articleSlide);
            articlePanel.add(separator);

            articlePanel.revalidate();    // Revalidate the layout
            articlePanel.repaint();       // Repaint the panel to reflect the changes
        }
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
        });
        return generateButton;
    }

    private JButton createSaveButton(Article article) {
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN);

        saveButton.addActionListener(e -> {
            // execute save article use case
            this.saveArticleController.execute(article);
            articlePanel.revalidate();    // Revalidate the layout
            articlePanel.repaint();       // Repaint the panel to reflect the changes
        });
        return saveButton;
    }

    private JButton createUnsaveButton(Article article) {
        JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(e -> {
            // execute unsave article use case
            this.unsaveArticleController.execute(article);
            articlePanel.revalidate();    // Revalidate the layout
            articlePanel.repaint();       // Repaint the panel to reflect the changes
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

    public void setAddCategoryController(AddCategoryController addCategoryController) {
        this.addCategoryController = addCategoryController;
    }

    public void setRemoveCategoryController(RemoveCategoryController removeCategoryController) {
        this.removeCategoryController = removeCategoryController;
    }

    public void setDigestController(DigestController digestController) {
        this.digestController = digestController;
    }

    public void setSaveArticleController(SaveArticleController saveArticleController) {
        this.saveArticleController = saveArticleController;
    }

    public void setUnsaveArticleController(UnsaveArticleController unsaveArticleController) {
        this.unsaveArticleController = unsaveArticleController;
    }

    public void setShareArticleController(ShareArticleController shareArticleController) {
        this.shareArticleController = shareArticleController;
    }

    public void setSavedArticlesController(SavedArticlesController savedArticlesController) {
        this.savedArticlesController = savedArticlesController;
    }
}
