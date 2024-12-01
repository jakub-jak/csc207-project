package use_case.add_category;

import java.util.List;

/**
 * The AddCategory Interactor.
 */
public class AddCategoryInteractor implements AddCategoryInputBoundary {
    private final AddCategoryDataAccessInterface addCategoryDataAccessObject;
    private final AddCategoryOutputBoundary addCategoryPresenter;

    public AddCategoryInteractor(AddCategoryDataAccessInterface addCategoryDataAccessInterface,
                           AddCategoryOutputBoundary addCategoryOutputBoundary) {
        this.addCategoryDataAccessObject = addCategoryDataAccessInterface;
        this.addCategoryPresenter = addCategoryOutputBoundary;
    }

    @Override
    public void execute(AddCategoryInputData addCategoryInputData) {
        final String inputCategory = addCategoryInputData.getCategory();
        final List<String> categories = addCategoryDataAccessObject.getUserCategories();

        if (categories.contains(inputCategory)) {
            addCategoryPresenter.prepareFailView("Category already exists.");
        }
        else if (inputCategory.isEmpty()) {
            addCategoryPresenter.prepareFailView("Please enter a valid category.");
        }
        else {
            addCategoryDataAccessObject.saveCategory(inputCategory);
            final AddCategoryOutputData addCategoryOutputData =
                    new AddCategoryOutputData(inputCategory, false);
            addCategoryPresenter.prepareSuccessView(addCategoryOutputData);
        }
    }
}
