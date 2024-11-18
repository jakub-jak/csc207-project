package use_case.remove_category;

import java.util.List;

public interface RemoveCategoryDataAccessInterface {
    /**
     * Saves the category to the user's categories.
     *
     * @param category the category to save
     */
    void saveCategory(String category);

    /**
     * Gets the given user's category list.
     *
     * @return the given user's category list.
     */
    List<String> getUserCategories();
}
