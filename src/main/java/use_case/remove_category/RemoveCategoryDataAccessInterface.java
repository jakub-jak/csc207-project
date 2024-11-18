package use_case.remove_category;

import java.util.List;

public interface RemoveCategoryDataAccessInterface {
    /**
     * Saves the category to the user's categories.
     * @param username the user to save
     * @param category the category to save
     */
    void saveRemovedCategory(String username, String category);

    /**
     * Gets the given user's category list.
     * @param username the given user
     * @return the given user's category list.
     */
    List<String> getUserCategories(String username);
}
