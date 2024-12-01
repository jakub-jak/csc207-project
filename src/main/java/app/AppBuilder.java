package app;


import data_access.CohereDataAccessObject;
import data_access.EmailDataAccessObject;
import data_access.MongoDBUserDataAccessObject;
import data_access.NewsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.digest.DigestController;
import interface_adapter.logged_in.*;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.saved_articles.NewsController;
import interface_adapter.saved_articles.SavedArticlesPresenter;
import interface_adapter.saved_articles.SavedArticlesViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.add_category.AddCategoryInputBoundary;
import use_case.add_category.AddCategoryInteractor;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.digest.DigestInputBoundary;
import use_case.digest.DigestInteractor;
import use_case.digest.DigestOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.news.NewsInputBoundary;
import use_case.news.NewsInteractor;
import use_case.news.NewsOutputBoundary;
import use_case.remove_category.RemoveCategoryInputBoundary;
import use_case.remove_category.RemoveCategoryInteractor;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import use_case.save_article.SaveArticleInputBoundary;
import use_case.save_article.SaveArticleInteractor;
import use_case.save_article.SaveArticleOutputBoundary;
import use_case.saved_articles.SavedArticlesInputBoundary;
import use_case.saved_articles.SavedArticlesInteractor;
import use_case.saved_articles.SavedArticlesOutputBoundary;
import use_case.share_article.ShareArticleInteractor;
import use_case.share_article.ShareArticleOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.unsave_article.UnsaveArticleInputBoundary;
import use_case.unsave_article.UnsaveArticleInteractor;
import use_case.unsave_article.UnsaveArticleOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // DAOs
    private final MongoDBUserDataAccessObject mongoDBUserDataAccessObject = new MongoDBUserDataAccessObject();
    final NewsDataAccessObject newsDataAccessObject = new NewsDataAccessObject();
    final CohereDataAccessObject cohereDataAccessObject = new CohereDataAccessObject();
    final EmailDataAccessObject emailDataAccessObject = new EmailDataAccessObject();

    // Views & View Models
    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoggedInView loggedInView;
    private LoggedInViewModel loggedInViewModel;
    private SavedArticlesView savedArticlesView;
    private SavedArticlesViewModel savedArticlesViewModel;

    public AppBuilder() throws Exception {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Signup View to the application.
     * @return this builder
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Adds the Login View to the application.
     * @return this builder
     */
    public AppBuilder addLoginView(){
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the Logged In View to the application.
     * @return this builder
     */
    public AppBuilder addLoggedInView(){
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    /**
     * Adds the Saved Articles View to the application.
     * @return this builder
     */
    public AppBuilder addSavedArticlesView(){
        savedArticlesViewModel = new SavedArticlesViewModel();
        savedArticlesView = new SavedArticlesView(savedArticlesViewModel);
        cardPanel.add(savedArticlesView, savedArticlesView.getViewName());
        return this;
    }

    /**
     * Adds the Signup Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                mongoDBUserDataAccessObject, signupOutputBoundary);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Adds the Login Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(mongoDBUserDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Adds the Log-out Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel, savedArticlesViewModel);
        final LogoutInputBoundary logoutInputBoundary = new LogoutInteractor(mongoDBUserDataAccessObject, logoutOutputBoundary);
        final LogoutController controller = new LogoutController(logoutInputBoundary);
        // TODO: uncomment when logout is done
        //loggedInView.setLogoutController(controller);
        return this;
    }

    /**
     * Adds the Add Category Use Case to the application.
     * @return this builder
     */
    public AppBuilder addAddCategoryUseCase() {
        // Logged In View
        AddCategoryOutputBoundary addCategoryOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        AddCategoryInputBoundary addCategoryInteractor = new AddCategoryInteractor(mongoDBUserDataAccessObject,
                addCategoryOutputBoundary);

        AddCategoryController controller = new AddCategoryController(addCategoryInteractor);
        loggedInView.setAddCategoryController(controller);

        // Saved Articles View
        AddCategoryOutputBoundary addCategoryOutputBoundary1 = new SavedArticlesPresenter(viewManagerModel,
                loggedInViewModel , savedArticlesViewModel);
        AddCategoryInputBoundary addCategoryInteractor1 = new AddCategoryInteractor(mongoDBUserDataAccessObject,
                addCategoryOutputBoundary1);

        AddCategoryController controller1 = new AddCategoryController(addCategoryInteractor1);
        savedArticlesView.setAddCategoryController(controller1);
        return this;
    }

    /**
     * Adds the Add Category Use Case to the application.
     * @return this builder
     */
    public AppBuilder addRemoveCategoryUseCase() {
        // Logged In View
        RemoveCategoryOutputBoundary removeCategoryOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        RemoveCategoryInputBoundary removeCategoryInputBoundary = new RemoveCategoryInteractor(mongoDBUserDataAccessObject,
                removeCategoryOutputBoundary);

        RemoveCategoryController controller = new RemoveCategoryController(removeCategoryInputBoundary);
        loggedInView.setRemoveCategoryController(controller);

        // Saved Articles View
        RemoveCategoryOutputBoundary removeCategoryOutputBoundary1 = new SavedArticlesPresenter(viewManagerModel,
                loggedInViewModel , savedArticlesViewModel);
        RemoveCategoryInputBoundary removeCategoryInputBoundary1 = new RemoveCategoryInteractor(mongoDBUserDataAccessObject,
                removeCategoryOutputBoundary1);

        RemoveCategoryController controller1 = new RemoveCategoryController(removeCategoryInputBoundary1);
        savedArticlesView.setRemoveCategoryController(controller1);
        return this;
    }

    /**
     * Adds the Digest Use Case to the application.
     * @return this builder
     */
    public AppBuilder addDigestUseCase() {
        DigestOutputBoundary digestOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        DigestInputBoundary digestInputBoundary = new DigestInteractor(newsDataAccessObject, cohereDataAccessObject,
                digestOutputBoundary);
        DigestController controller = new DigestController(digestInputBoundary);
        loggedInView.setDigestController(controller);
        return this;
    }

    /**
     * Adds the News Use Case to the application.
     * @return this builder
     */
    public AppBuilder addNewsUseCase() {
        NewsOutputBoundary newsOutputBoundary = new SavedArticlesPresenter(viewManagerModel,
                loggedInViewModel , savedArticlesViewModel);
        NewsInputBoundary newsInputBoundary = new NewsInteractor(newsOutputBoundary);
        NewsController controller = new NewsController(newsInputBoundary);
        savedArticlesView.setNewsController(controller);
        return this;
    }

    /**
     * Adds the Save Article Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSaveArticlesUseCase() {
        SaveArticleOutputBoundary saveArticleOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        SaveArticleInputBoundary saveArticleInputBoundary = new SaveArticleInteractor(mongoDBUserDataAccessObject,
                saveArticleOutputBoundary);
        SaveArticleController controller = new SaveArticleController(saveArticleInputBoundary);
        loggedInView.setSaveArticleController(controller);
        return this;
    }

    /**
     * Adds the Save Article Use Case to the application.
     * @return this builder
     */
    public AppBuilder addUnsaveArticlesUseCase() {
        // Logged In View
        UnsaveArticleOutputBoundary unsaveArticleOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        UnsaveArticleInputBoundary unsaveArticleInputBoundary = new UnsaveArticleInteractor(mongoDBUserDataAccessObject,
                unsaveArticleOutputBoundary);
        UnsaveArticleController controller = new UnsaveArticleController(unsaveArticleInputBoundary);
        loggedInView.setUnsaveArticleController(controller);

        // Saved Articles View
        UnsaveArticleOutputBoundary unsaveArticleOutputBoundary1 = new SavedArticlesPresenter(viewManagerModel,
                loggedInViewModel , savedArticlesViewModel);
        UnsaveArticleInputBoundary unsaveArticleInputBoundary1 = new UnsaveArticleInteractor(mongoDBUserDataAccessObject,
                unsaveArticleOutputBoundary1);
        UnsaveArticleController controller1 = new UnsaveArticleController(unsaveArticleInputBoundary1);
        savedArticlesView.setUnsaveArticleController(controller1);

        return this;
    }

    /**
     * Adds the Share Article Use Case to the application.
     * @return this builder
     */
    public AppBuilder addShareArticleUseCase() {

        // Instantiate Interactor
        ShareArticleInteractor shareArticleInteractor = new ShareArticleInteractor(
                mongoDBUserDataAccessObject, emailDataAccessObject);

        // Instantiate Controller
        ShareArticleController shareArticleController = new ShareArticleController(shareArticleInteractor);

        // Set the Controller in LoggedInView
        loggedInView.setShareArticleController(shareArticleController);

        // Set the Controller in Saved Articles View
        savedArticlesView.setShareArticleController(shareArticleController);
        return this;
    }


    /**
     * Adds the Saved Articles Use Case to the application.
     * @return this builder
     */
    public AppBuilder addSavedArticlesUseCase() {
        SavedArticlesOutputBoundary savedArticlesOutputBoundary = new LoggedInPresenter(viewManagerModel,
                loggedInViewModel , loginViewModel, savedArticlesViewModel);
        SavedArticlesInputBoundary savedArticlesInputBoundary = new SavedArticlesInteractor(mongoDBUserDataAccessObject,
                savedArticlesOutputBoundary);
        SavedArticlesController controller = new SavedArticlesController(savedArticlesInputBoundary);
        loggedInView.setSavedArticlesController(controller);
        return this;
    }

    /**
     * Creates the JFrame for the application and initially sets the SignupView to be displayed.
     * @return the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("Test");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        return application;
    }
}
