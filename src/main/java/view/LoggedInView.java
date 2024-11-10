package view;

import interface_adapter.loggedin.LoggedInState;
import interface_adapter.loggedin.LoggedInViewModel;
import interface_adapter.loggedin.LogoutController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    private final JLabel passwordErrorField = new JLabel();
    private LogoutController logoutController;

    private final JLabel username;
    private final JButton logOut;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Logged In Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Text field for category input
        JPanel inputPanel = new JPanel();
        JTextField categoryField = new JTextField(16);
        inputPanel.add(new JLabel("Enter Category:"));
        inputPanel.add(categoryField);

        // Panel for buttons (add, remove, update preferences)
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Category");
        JButton removeButton = new JButton("Remove Category");
        JButton updateButton = new JButton("Update Preferences");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);

        // Panel to display categories
        JPanel listPanel = new JPanel();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> categoryList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(categoryList);
        listPanel.add(scrollPane);

        // Update the main window
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);
        this.add(listPanel, BorderLayout.SOUTH);

        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        final JPanel buttons = new JPanel();
        logOut = new JButton("Log Out");
        buttons.add(logOut);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logOut.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                evt -> {
                    if (evt.getSource().equals(logOut)) {
                        // 1. get the state out of the loggedInViewModel. It contains the username.
                        // 2. Execute the logout Controller.
                        final LoggedInState currentState = loggedInViewModel.getState();
                        this.logoutController.execute(currentState.getUsername());
                    }
                }
        );

        // Action listener for adding a category
        addButton.addActionListener((ActionEvent e) -> {
            String category = categoryField.getText().trim();
            if (!category.isEmpty() && !categoriesList.contains(category)) {
                categoriesList.add(category);
                listModel.addElement(category);  // Add category to list view
                categoryField.setText("");  // Clear the input field
            } else {
                JOptionPane.showMessageDialog(this, "Category is empty or already exists!");
            }
        });

        // Action listener for removing a selected category
        removeButton.addActionListener((ActionEvent e) -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                categoriesList.remove(selectedCategory);
                listModel.removeElement(selectedCategory);  // Remove category from the list view
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to remove!");
            }
        });

        // Action listener for updating preferences (storing the list of categories)
        updateButton.addActionListener((ActionEvent e) -> {
            // Once the "Update Preferences" button is pressed, update currentUser's categories
            currentUser.setCategories(new ArrayList<>(categoriesList));

            StringBuilder sb = new StringBuilder("Saved Categories:\n");
            for (String category : categoriesList) {
                sb.append(category).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Preferences Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(username);
        this.add(passwordErrorField);
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
