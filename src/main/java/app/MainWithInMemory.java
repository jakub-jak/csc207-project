package app;

import data_access.CohereDataAccessObject;
import data_access.EmailDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.NewsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;


/**
 * The version of Main with in memory DAO.
 */
public class MainWithInMemory {

    /**
     * The main method for starting the program.
     * @param args input to main
     */
    public static void main(String[] args) throws Exception {

        // Build the main program window, the main panel containing the
        // various cards, and the layout, and stitch them together.

        // The main application window.
        final JFrame application = new JFrame("Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final CardLayout cardLayout = new CardLayout();

        // The various View objects. Only one view is visible at a time.
        final JPanel views = new JPanel(cardLayout);
        application.add(views);


        // This keeps track of and manages which view is currently showing.
        final ViewManagerModel viewManagerModel = new ViewManagerModel();
        new ViewManager(views, cardLayout, viewManagerModel);

        // The data for the views, such as username and password, are in the ViewModels.
        // This information will be changed by a presenter object that is reporting the
        // results from the use case. The ViewModels are "observable", and will
        // be "observed" by the Views.
        final LoginViewModel loginViewModel = new LoginViewModel();
        final LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        final SignupViewModel signupViewModel = new SignupViewModel();

        final InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();
        final NewsDataAccessObject newsDataAccessObject = new NewsDataAccessObject();
        final CohereDataAccessObject cohereDataAccessObject = new CohereDataAccessObject();
        final EmailDataAccessObject emailDataAccessObject = new EmailDataAccessObject();

        final SignupView signupView = SignupUseCaseFactory.create(viewManagerModel, loginViewModel,
                signupViewModel, userDataAccessObject);
        views.add(signupView, signupView.getViewName());

        final LoginView loginView = LoginUseCaseFactory.create(viewManagerModel, loginViewModel,
                loggedInViewModel, userDataAccessObject);
        views.add(loginView, loginView.getViewName());

        final LoggedInView loggedInView = LoggedinUseCasesFactory.create(viewManagerModel, loggedInViewModel, loginViewModel,
                userDataAccessObject, newsDataAccessObject, cohereDataAccessObject, emailDataAccessObject);
        views.add(loggedInView, loggedInView.getViewName());

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChanged();

        application.pack();
        application.setVisible(true);

    }
}
