package view;

import entity.Article;
import interface_adapter.digest.DigestController;
import interface_adapter.logged_in.*;
import interface_adapter.logout.LogoutController;

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

    private AddCategoryController addCategoryController;
    private RemoveCategoryController removeCategoryController;
    private DigestController digestController;
    private SaveArticleController saveArticleController;
    private UnsaveArticleController unsaveArticleController;
    private ShareArticleController shareArticleController;
    private SavedArticlesController savedArticlesController;
    private LogoutController logoutController;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // Navbar Panel
        final JPanel navigationPanel = new JPanel();
        final JButton savedArticlesButton = new JButton("Saved Articles");
        savedArticlesButton.addActionListener(e -> {
            this.savedArticlesController.execute();
        });

        // adding saved articles and logout use cases to navbar
        navigationPanel.add(savedArticlesButton);
        navigationPanel.add(new JLabel("News"));
        navigationPanel.add(createLogoutButton());

        // Input panel
        final JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Category:"));

        final JTextField categoryField = new JTextField(16);
        inputPanel.add(categoryField);

        // add category button and use case
        inputPanel.add(createAddCategoryButton(categoryField));

        // Category Panel
        final JPanel categoryPanel = new JPanel();
        categoryPanel.add(inputPanel);
        categoryPanel.add(createGenerateButton());

        categoryButtonsPanel = new JPanel();
        categoryButtonsPanel.setLayout(new FlowLayout());

        // Article Panel
        articlePanel = new JPanel();
        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.Y_AXIS));

        final JScrollPane scrollArticlePanel = new JScrollPane(articlePanel);
        scrollArticlePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(navigationPanel);
        this.add(categoryPanel);
        this.add(categoryButtonsPanel);
        this.add(scrollArticlePanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Initialize
        if (evt.getPropertyName().equals("init")) {
            categoryButtonsPanel.removeAll();
            articlePanel.removeAll();
            // Populate the categoryButtonsPanel with the saved categories of the current user
            final LoggedInState state = loggedInViewModel.getState();
            for (String category: state.getCategoriesList()) {
                final JButton categoryButton = createCategoryButton(category);
                categoryButtonsPanel.add(categoryButton);
            }
            categoryButtonsPanel.revalidate();
            categoryButtonsPanel.repaint();
            articlePanel.revalidate();
            articlePanel.repaint();
        }
        else if (evt.getPropertyName().startsWith("add category: ")) {
            final String category = evt.getPropertyName().substring("add category: ".length());
            final JButton categoryButton = createCategoryButton(category);
            categoryButtonsPanel.add(categoryButton);
            categoryButtonsPanel.revalidate();
            categoryButtonsPanel.repaint();
        }
        else if (evt.getPropertyName().equals("articles")) {
            // Populate the articlePanel with articles for each article the user generates.
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            refreshArticlePanel(state);
        }
        else if (evt.getPropertyName().equals("articles add")) {
            // Display a success message
            JOptionPane.showMessageDialog(
                    null,
                    "The article has been successfully saved! You can now view this article under Saved "
                            + "Articles.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        else if (evt.getPropertyName().equals("articles remove")) {
            // Display a success message
            JOptionPane.showMessageDialog(
                    null,
                    "The article has been successfully unsaved and will not appear under Saved Articles "
                            + "anymore.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        else if (evt.getPropertyName().equals("Article is not saved.") || evt
                .getPropertyName().equals("Article already saved.")) {
            // Display an error message
            JOptionPane.showMessageDialog(this, evt.getPropertyName(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getViewName() {
        return viewName;
    }

    // refresh the article panel to show new articles generated, following the digest use case
    private void refreshArticlePanel(LoggedInState state) {
        articlePanel.removeAll();

        for (Article article: state.getArticleList()) {
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
            articleAuthor.setFont(new Font(fontName, Font.PLAIN, fontSize14));

            // Date
            final JLabel articleDate = new JLabel(article.getDate());
            articleDate.setFont(new Font(fontName, Font.PLAIN, fontSize12));

            // Link
            final JLabel articleLink = new JLabel(article.getLink());
            articleLink.setFont(new Font(fontName, Font.PLAIN, fontSize12));
            articleLink.setForeground(Color.BLUE);

            // Description
            final JTextArea articleDescription = new JTextArea(article.getDescription());
            articleDescription.setFont(new Font("Arial", Font.PLAIN, 12));
            articleDescription.setLineWrap(true);
            articleDescription.setWrapStyleWord(true);
            articleDescription.setEditable(false);
            articleDescription.setBackground(articleSlide.getBackground());

            // Calculate max width of the description as half of the window size
            final int maxWidth = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
            final int height = 100;
            articleDescription.setPreferredSize(new Dimension(maxWidth, height));
            final JScrollPane descriptionScrollPane = new JScrollPane(articleDescription);

            // save / un-save / share buttons
            final JPanel buttonPanel = new JPanel();
            buttonPanel.add(createSaveButton(article));
            buttonPanel.add(createUnsaveButton(article));
            buttonPanel.add(createShareButton(article));
            buttonPanel.add(createShareToOthersButton(article));

            // Add labels to article slide panel
            articleSlide.add(articleTitle);
            articleSlide.add(articleAuthor);
            articleSlide.add(articleDate);
            articleSlide.add(articleLink);
            articleSlide.add(descriptionScrollPane);
            articleSlide.add(buttonPanel);

            // Add a divider (separator) after each article
            final JSeparator separator = new JSeparator();
            articlePanel.add(articleSlide);
            articlePanel.add(separator);
            articlePanel.revalidate();
            articlePanel.repaint();
        }
    }

    private JButton createAddCategoryButton(JTextField categoryField) {
        final JButton addCategoryButton = new JButton("Add Category");
        addCategoryButton.addActionListener(actionEvent -> {
            final String category = categoryField.getText();
            this.addCategoryController.execute(category);
            categoryField.setText("");
        });
        return addCategoryButton;
    }

    private JButton createCategoryButton(String category) {
        final JButton categoryButton = new JButton(category);
        categoryButton.addActionListener(actionEvent -> {
            // execute remove category use case
            this.removeCategoryController.execute(category);
            // Remove this button from the panel
            categoryButtonsPanel.remove(categoryButton);
            categoryButtonsPanel.revalidate();
            categoryButtonsPanel.repaint();
        });
        return categoryButton;
    }

    private JButton createGenerateButton() {
        final JButton generateButton = new JButton("Generate");
        generateButton.setBackground(Color.GREEN);
        generateButton.addActionListener(actionEvent -> {
            // execute digest use case
            this.digestController.execute(loggedInViewModel.getState().getCategoriesList().toArray(new String[0]));
        });
        return generateButton;
    }

    private JButton createLogoutButton() {
        final JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionEvent -> {
            // execute logout use case
            this.logoutController.execute(loggedInViewModel.getState().getUsername());
        });
        return logoutButton;
    }

    private JButton createSaveButton(Article article) {
        final JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN);

        saveButton.addActionListener(actionEvent -> {
            // execute save article use case
            this.saveArticleController.execute(article);
            articlePanel.revalidate();
            articlePanel.repaint();
        });
        return saveButton;
    }

    private JButton createUnsaveButton(Article article) {
        final JButton unsaveButton = new JButton("Unsave");
        unsaveButton.setBackground(Color.RED);

        unsaveButton.addActionListener(actionEvent -> {
            // execute unsave article use case
            this.unsaveArticleController.execute(article);
            articlePanel.revalidate();
            articlePanel.repaint();
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

    private JButton createShareToOthersButton(Article article) {
        final JButton shareToOthersButton = new JButton("Share to other email");
        shareToOthersButton.setBackground(Color.GREEN);

        shareToOthersButton.addActionListener(e -> {
            // Show an input dialog to get the recipient's email
            final String recipientEmail = JOptionPane.showInputDialog(
                    null,
                    "Enter the recipient's email address:",
                    "Share Article",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No email address was entered. Please try again.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                // Execute the share article use case for another email
                this.shareArticleController.executeToOtherEmail(article, recipientEmail);

                // Show a success popup message
                JOptionPane.showMessageDialog(
                        null,
                        "The article has been successfully sent to " + recipientEmail + ".",
                        "Email Sent",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception ex) {
                // Show an error popup message in case of an exception
                JOptionPane.showMessageDialog(
                        null,
                        "An error occurred while sending the email. Please try again. " +
                                "You might want to check if the email address entered is valid.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                throw new RuntimeException(ex);
            }
        });

        return shareToOthersButton;
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

    public void setLogoutController(LogoutController logOutController) {
        this.logoutController = logOutController;
    }
}
