package use_case.add_category;

import java.util.List;

/**
 * The AddCategory Interactor.
 */
public class AddCategoryInteractor implements AddCategoryInputBoundary {
    private final AddCategoryDataAccessInterface addcategoryDataAccessObject;
    private final AddCategoryOutputBoundary addCategoryPresenter;

    public AddCategoryInteractor(AddCategoryDataAccessInterface addCategoryDataAccessInterface,
                           AddCategoryOutputBoundary addCategoryOutputBoundary) {
        this.addcategoryDataAccessObject = addCategoryDataAccessInterface;
        this.addCategoryPresenter = addCategoryOutputBoundary;
    }

    @Override
    public void execute(AddCategoryInputData addCategoryInputData) {
        final String username = addCategoryInputData.getUsername();
        final String inputCategory = addCategoryInputData.getCategory();
        final List<String> categories = addcategoryDataAccessObject.getUserCategories(username);

        if (categories.contains(inputCategory)) {
            addCategoryPresenter.prepareFailView("Category already exists.");
        } else if (inputCategory.isEmpty()) {
            addCategoryPresenter.prepareFailView("Please enter a valid category.");
        } else {
            final AddCategoryOutputData addCategoryOutputData =
                    new AddCategoryOutputData(username, inputCategory, false);
            addCategoryPresenter.prepareSuccessView(addCategoryOutputData);
        }
    }
}
