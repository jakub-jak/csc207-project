package app;

import entity.CommonUser;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The version of Main with a simple GUI.
 */
public class MainWithInMemory {
    // Create a CommonUser instance (with sample data)
    private static User currentUser = new CommonUser(
            "john_doe",
            "password123",
            new ArrayList<>(),
            null
    );

    // Temporary list that mirrors the currentUser's categories
    private static List<String> categoriesList = new ArrayList<>(currentUser.getCategories());

    /**
     * The main method for starting the program.
     * @param args input to main
     */
    public static void main(String[] args) {

        // The main application window.
        final JFrame application = new JFrame("Demo");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setLayout(new BorderLayout());  // Use BorderLayout for better component arrangement

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
        application.add(inputPanel, BorderLayout.NORTH);
        application.add(buttonPanel, BorderLayout.CENTER);
        application.add(listPanel, BorderLayout.SOUTH);

        // Action listener for adding a category
        addButton.addActionListener((ActionEvent e) -> {
            String category = categoryField.getText().trim();
            if (!category.isEmpty() && !categoriesList.contains(category)) {
                categoriesList.add(category);
                listModel.addElement(category);  // Add category to list view
                categoryField.setText("");  // Clear the input field
            } else {
                JOptionPane.showMessageDialog(application, "Category is empty or already exists!");
            }
        });

        // Action listener for removing a selected category
        removeButton.addActionListener((ActionEvent e) -> {
            String selectedCategory = categoryList.getSelectedValue();
            if (selectedCategory != null) {
                categoriesList.remove(selectedCategory);
                listModel.removeElement(selectedCategory);  // Remove category from the list view
            } else {
                JOptionPane.showMessageDialog(application, "Please select a category to remove!");
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
            JOptionPane.showMessageDialog(application, sb.toString(), "Preferences Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        // Finalize and show the frame
        application.pack();
        application.setVisible(true);

    }
}
