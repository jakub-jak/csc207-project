package app;

import data_access.CohereDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.NewsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.digest.DigestController;
import interface_adapter.logged_in.*;
import interface_adapter.login.LoginViewModel;
import use_case.add_category.AddCategoryDataAccessInterface;
import use_case.add_category.AddCategoryInputBoundary;
import use_case.add_category.AddCategoryInteractor;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.digest.*;
import use_case.remove_category.RemoveCategoryDataAccessInterface;
import use_case.remove_category.RemoveCategoryInputBoundary;
import use_case.remove_category.RemoveCategoryInteractor;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import use_case.save_article.SaveArticleDataAccessInterface;
import use_case.save_article.SaveArticleInputBoundary;
import use_case.save_article.SaveArticleInteractor;
import use_case.save_article.SaveArticleOutputBoundary;
import use_case.unsave_article.UnsaveArticleDataAccessInterface;
import use_case.unsave_article.UnsaveArticleInputBoundary;
import use_case.unsave_article.UnsaveArticleInteractor;
import use_case.unsave_article.UnsaveArticleOutputBoundary;
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
            InMemoryUserDataAccessObject userDataAccessObject,
            NewsDataAccessObject newsDataAccessObject,
            CohereDataAccessObject cohereDataAccessObject) {

        final AddCategoryController addCategoryController =
                createAddCategoryUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject);
        final RemoveCategoryController removeCategoryController =
                createRemoveCategoryUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject);
        final DigestController digestController = createDigestUseCase(viewManagerModel, loggedInViewModel, loginViewModel,
                newsDataAccessObject, cohereDataAccessObject);
        final SaveArticleController saveArticleController =
                createSaveArticleUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject);
        final UnsaveArticleController unsaveArticleController =
                createUnsaveArticleUseCase(viewManagerModel, loggedInViewModel, loginViewModel, userDataAccessObject);

        return new LoggedInView(loggedInViewModel, addCategoryController, removeCategoryController,
                digestController, saveArticleController, unsaveArticleController);

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

    private static DigestController createDigestUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            DigestNewsDataAccessInterface newsDataAccessObject,
            DigestCohereDataAccessInterface cohereDataAccessObject) {

        final DigestOutputBoundary digestOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final DigestInputBoundary digestInteractor =
                new DigestInteractor(newsDataAccessObject, cohereDataAccessObject, digestOutputBoundary);

        return new DigestController(digestInteractor);
    }

    private static SaveArticleController createSaveArticleUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            SaveArticleDataAccessInterface userDataAccessObject) {

        // Notice how we pass this method's parameters through to the Presenter.
        final SaveArticleOutputBoundary saveArticleOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final SaveArticleInputBoundary saveArticleInteractor =
                new SaveArticleInteractor(userDataAccessObject, saveArticleOutputBoundary);

        return new SaveArticleController(saveArticleInteractor);
    }

    private static UnsaveArticleController createUnsaveArticleUseCase(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            LoginViewModel loginViewModel,
            UnsaveArticleDataAccessInterface userDataAccessObject) {

        // Notice how we pass this method's parameters through to the Presenter.
        final UnsaveArticleOutputBoundary unsaveArticleOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final UnsaveArticleInputBoundary unsaveArticleInteractor =
                new UnsaveArticleInteractor(userDataAccessObject, unsaveArticleOutputBoundary);

        return new UnsaveArticleController(unsaveArticleInteractor);
    }

}
