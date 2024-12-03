package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.saved_articles.SavedArticlesState;
import interface_adapter.saved_articles.SavedArticlesViewModel;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.add_category.AddCategoryOutputData;
import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import use_case.remove_category.RemoveCategoryOutputData;
import use_case.save_article.SaveArticleOutputBoundary;
import use_case.save_article.SaveArticleOutputData;
import use_case.saved_articles.SavedArticleOutputData;
import use_case.saved_articles.SavedArticlesOutputBoundary;
import use_case.unsave_article.UnsaveArticleOutputBoundary;
import use_case.unsave_article.UnsaveArticleOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class LoggedInPresenter implements LogoutOutputBoundary,
        AddCategoryOutputBoundary,
        RemoveCategoryOutputBoundary,
        DigestOutputBoundary,
        SaveArticleOutputBoundary,
        UnsaveArticleOutputBoundary,
        SavedArticlesOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;
    private final LoginViewModel loginViewModel;
    private final SavedArticlesViewModel savedArticlesViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoggedInPresenter(ViewManagerModel viewManagerModel,
                             LoggedInViewModel loggedInViewModel,
                             LoginViewModel loginViewModel, SavedArticlesViewModel savedArticlesViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.savedArticlesViewModel = savedArticlesViewModel;
    }

    // Update LoggedInState with added category following the AddCategory UseCase
    @Override
    public void prepareSuccessView(AddCategoryOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.addCategory(response.getCategory());
        loggedInViewModel.firePropertyChanged("add category: " + response.getCategory());
    }

    // Update LoggedInState with added category following the AddCategory UseCase
    @Override
    public void prepareSuccessView(RemoveCategoryOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.removeCategory(response.getCategory());
        loggedInViewModel.firePropertyChanged("remove category: " + response.getCategory());
    }

    // Switch to Login View when the logout is pressed
    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        final LoginState loginState = loginViewModel.getState();

        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChanged();

        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView(DigestOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setArticleList(response.getArticles());
        loggedInViewModel.firePropertyChanged("articles");
    }

    @Override
    public void prepareSuccessView(SaveArticleOutputData response) {
        loggedInViewModel.firePropertyChanged("articles add");
    }

    @Override
    public void prepareSuccessView(UnsaveArticleOutputData response) {
        loggedInViewModel.firePropertyChanged("articles remove");
    }

    @Override
    public void prepareSuccessView(SavedArticleOutputData savedArticleOutputData) {
        final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        savedArticlesState.setArticleMap(savedArticleOutputData.getUser().getArticles());
        this.viewManagerModel.setState(savedArticlesViewModel.getViewName());
        this.savedArticlesViewModel.firePropertyChanged("articles");
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setLoggedInError(error);
        loggedInViewModel.firePropertyChanged();
    }

}
