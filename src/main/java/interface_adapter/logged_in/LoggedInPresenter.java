package interface_adapter.logged_in;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputData;

import javax.swing.text.View;

/**
 * The Presenter for the Change Password Use Case.
 */
public class LoggedInPresenter {

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
    // TODO: Uncomment after implementing use cases for the prepareSuccessView Overloading
//
//    // Update LoggedInState with new categories following the updateCategory UseCase
//    @Override
//    public void prepareSuccessView(UpdateCatagoriesOuputData response) {
//        final LoggedInState loggedInState = loggedInViewModel.getState();
//        loggedInState.setCategoriesList(response);
//        loggedInViewModel.firePropertyChanged("category");
//    }
//
//    // Update LoggedInState with new articles following the updateArticles UseCase
//    @Override
//    public void prepareSuccessView(UpdateArticlesOuputData response) {
//        final LoggedInState loggedInState = loggedInViewModel.getState();
//        loggedInState.setArticleList(response);
//        loggedInViewModel.firePropertyChanged("articles");
//    }

    // Switch to Login View when the logout is pressed
//    @Override
    public void prepareSuccessView(LogoutOutputData response) {
        final LoginState loginState = loginViewModel.getState();

        this.loginViewModel.setState(loginState);
        this.loginViewModel.firePropertyChanged();

        this.viewManagerModel.setState(loginViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }


//    @Override
    public void prepareFailView(String error) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setLoggedInError(error);
        loggedInViewModel.firePropertyChanged();
    }
}
