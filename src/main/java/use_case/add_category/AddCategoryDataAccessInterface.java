package use_case.add_category;

import java.util.List;

public interface AddCategoryDataAccessInterface {
    /**
     * Saves the category to the user's categories.
     * @param username the user to save
     * @param category the category to save
     */
    void saveCategory(String username, String category);

    /**
     * Gets the given user's category list.
     * @param username the given user
     * @return the given user's category list.
     */
    List<String> getUserCategories(String username);
}
