package use_case.remove_category;

import java.util.List;

public interface RemoveCategoryDataAccessInterface {
    /**
     * Saves the category to the current user's categories.
     * @param category the category to save
     */
    void removeCategory(String category);

    /**
     * Gets the current user's category list.
     * @return the current user's category list.
     */
    List<String> getUserCategories();
}
