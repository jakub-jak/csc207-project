package use_case.add_category;

import java.util.List;

/**
 * Data Access Interface for the Add Category Use Case.
 */
public interface AddCategoryDataAccessInterface {
    /**
     * Saves the category to the user's categories.
     * @param category the category to save
     */
    void saveCategory(String category);

    /**
     * Gets the current user's category list.
     * @return the current user's category list.
     */
    List<String> getUserCategories();
}
