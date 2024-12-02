package use_case.login;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUser;
import entity.User;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class LoginInteractorTest {

    @Test
    public void successTest() {
        LoginInputData inputData = new LoginInputData("Ali@gmail.com", "password");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        User user = new CommonUser("Ali@gmail.com", "password", Collections.emptyList(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                assertEquals("Ali@gmail.com", user.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignupView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void successUserLoggedInTest() {
        LoginInputData inputData = new LoginInputData("Ali@gmail.com", "password");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Paul to the data access repository before we log in.
        User user = new CommonUser("Ali@gmail.com", "password", Collections.emptyList(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                assertEquals("Ali@gmail.com", userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignupView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        assertNull(userRepository.getCurrentUsername());
        interactor.execute(inputData);
    }


    @Test
    public void failurePasswordMismatchTest() {
        LoginInputData inputData = new LoginInputData("Ali@gmail.com", "wrong");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For this failure test, we need to add Paul to the data access repository before we log in, and
        // the passwords should not match.
        User user = new CommonUser("Ali@gmail.com", "pwd", Collections.emptyList(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"Ali@gmail.com\".", error);
            }

            @Override
            public void switchToSignupView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureUserDoesNotExistTest() {
        LoginInputData inputData = new LoginInputData("Ali@gmail.com", "password");
        LoginUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Ali@gmail.com: Account does not exist.", error);
            }

            /**
             * Switches to the Signup View.
             */
            @Override
            public void switchToSignupView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}