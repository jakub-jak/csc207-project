package use_case.remove_category;

import java.util.List;

/**
 * The RemoveCategory Interactor.
 */
public class RemoveCategoryInteractor implements RemoveCategoryInputBoundary  {
    private final RemoveCategoryDataAccessInterface removecategoryDataAccessObject;
    private final RemoveCategoryOutputBoundary removeCategoryPresenter;

    public RemoveCategoryInteractor(RemoveCategoryDataAccessInterface removeCategoryDataAccessInterface,
                                 RemoveCategoryOutputBoundary removeCategoryOutputBoundary) {
        this.removecategoryDataAccessObject = removeCategoryDataAccessInterface;
        this.removeCategoryPresenter = removeCategoryOutputBoundary;
    }

    @Override
    public void execute(RemoveCategoryInputData removeCategoryInputData) {
        final String username = removeCategoryInputData.getUsername();
        final String inputCategory = removeCategoryInputData.getCategory();
        final List<String> categories = removecategoryDataAccessObject.getUserCategories(username);

        if (! categories.contains(inputCategory)) {
            removeCategoryPresenter.prepareFailView("Category does not exist.");
        }
        else {
            removecategoryDataAccessObject.saveRemovedCategory(username, inputCategory);
            final RemoveCategoryOutputData removeCategoryOutputData =
                    new RemoveCategoryOutputData(username, inputCategory, false);
            removeCategoryPresenter.prepareSuccessView(removeCategoryOutputData);
        }
    }
}
