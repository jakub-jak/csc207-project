package use_case.logout;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUser;
import entity.User;
import org.junit.Test;
import use_case.login.*;

import java.util.Collections;

import static org.junit.Assert.*;

public class LogoutInteractorTest {

    @Test
    public void successTest() {
        LogoutInputData inputData = new LogoutInputData("Ali@gmail.com");
        LogoutUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Ali to the data access repository before we log in.
        User user = new CommonUser("Ali@gmail.com", "password", Collections.emptyList(), Collections.emptyMap());
        userRepository.save(user);
        
        // This creates a successPresenter that tests whether the test case is as we expect.
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                assertEquals("Ali@gmail.com", user.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void successUserLoggedOutTest() {
        LogoutInputData inputData = new LogoutInputData("Ali@gmail.com");
        LogoutUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Ali to the data access repository before we log in.
        User user = new CommonUser("Ali@gmail.com", "password", Collections.emptyList(), Collections.emptyMap());
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                assertNull(userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, successPresenter);
        assertNull(userRepository.getCurrentUsername());
        interactor.execute(inputData);
    }


    @Test
    public void failureUserDoesNotExistTest() {
        LogoutInputData inputData = new LogoutInputData("Ali@gmail.com");
        LogoutUserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();


        // This creates a presenter that tests whether the test case is as we expect.
        LogoutOutputBoundary failurePresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Ali@gmail.com: Account does not exist.", error);
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}