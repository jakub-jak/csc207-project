package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.AddCategoryController;
import interface_adapter.logged_in.LoggedInPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.RemoveCategoryController;
import interface_adapter.login.LoginViewModel;
import use_case.add_category.AddCategoryDataAccessInterface;
import use_case.add_category.AddCategoryInputBoundary;
import use_case.add_category.AddCategoryInteractor;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.remove_category.RemoveCategoryDataAccessInterface;
import use_case.remove_category.RemoveCategoryInputBoundary;
import use_case.remove_category.RemoveCategoryInteractor;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import view.LoggedInView;

public class LoggedinUseCasesFactory {

    /** Prevent instantiation. */
    private LoggedinUseCasesFactory() { }

    /**
     * Factory function for creating the LoggedInView.
     * @param viewManagerModel the ViewManagerModel to inject into the LoggedInView
     * @param loggedInViewModel the loggedInViewModel to inject into the LoggedInView
     * @param userDataAccessObject the ChangePasswordUserDataAccessInterface to inject into the LoggedInView
     * @return the LoggedInView created for the provided input classes
     */
    public static LoggedInView create(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            AddCategoryDataAccessInterface userDataAccessObject,
            RemoveCategoryDataAccessInterface userDataAccessObject2) {

        final AddCategoryController addCategoryController =
                createAddCategoryUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject);
        final RemoveCategoryController removeCategoryController =
                createRemoveCategoryUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject2);
        return new LoggedInView(loggedInViewModel, addCategoryController, removeCategoryController);

    }

    private static AddCategoryController createAddCategoryUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            AddCategoryDataAccessInterface userDataAccessObject) {

        // Notice how we pass this method's parameters through to the Presenter.
        final AddCategoryOutputBoundary addCategoryOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final AddCategoryInputBoundary addCategoryInteractor =
                new AddCategoryInteractor(userDataAccessObject, addCategoryOutputBoundary);

        return new AddCategoryController(addCategoryInteractor);
    }

    private static RemoveCategoryController createRemoveCategoryUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            RemoveCategoryDataAccessInterface userDataAccessObject) {

        // Notice how we pass this method's parameters through to the Presenter.
        final RemoveCategoryOutputBoundary removeCategoryOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final RemoveCategoryInputBoundary removeCategoryInteractor =
                new RemoveCategoryInteractor(userDataAccessObject, removeCategoryOutputBoundary);

        return new RemoveCategoryController(removeCategoryInteractor);
    }
}
