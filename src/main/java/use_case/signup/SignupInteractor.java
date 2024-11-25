package use_case.signup;

import entity.Article;
import entity.CommonUser;
import entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Signup Interactor.
 */
public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;


    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
    }

    @Override
    public void execute(SignupInputData signupInputData) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(signupInputData.getUsername());
        boolean matchFound = m.matches();
        if(!matchFound) {
            userPresenter.prepareFailView("This has to be a valid email address");
        }
        if (userDataAccessObject.existsByName(signupInputData.getUsername())) {
            userPresenter.prepareFailView("User already exists.");
        }
        else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        else {
            final User user = new CommonUser(signupInputData.getUsername(), signupInputData.getPassword(), new ArrayList<>(), Collections.emptyMap());
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName(), false);
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
