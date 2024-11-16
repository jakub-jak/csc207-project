package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.add_category.AddCategoryOutputData;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import use_case.remove_category.RemoveCategoryOutputData;

import javax.swing.text.View;

/**
 * The Presenter for the Change Password Use Case.
 */
public class LoggedInPresenter implements LogoutOutputBoundary, AddCategoryOutputBoundary, RemoveCategoryOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoggedInPresenter(ViewManagerModel viewManagerModel,
                             LoggedInViewModel loggedInViewModel,
                             LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
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

    // TODO: Uncomment after implementing use cases for the prepareSuccessView Overloading
//    // Update LoggedInState with new articles following the updateArticles UseCase
//    @Override
//    public void prepareSuccessView(UpdateArticlesOuputData response) {
//        final LoggedInState loggedInState = loggedInViewModel.getState();
//        loggedInState.setArticleList(response);
//        loggedInViewModel.firePropertyChanged("articles");
//    }

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
    public void prepareFailView(String error) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setLoggedInError(error);
        loggedInViewModel.firePropertyChanged();
    }
}
