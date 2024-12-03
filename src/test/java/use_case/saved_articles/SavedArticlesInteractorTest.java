package use_case.saved_articles;

import data_access.InMemoryUserDataAccessObject;
import entity.CommonUser;
import entity.User;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class SavedArticlesInteractorTest {

    @Test
    public void successTest() {
        SavedArticlesDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        // For the success test, we need to add Ali to the data access repository before we log in.
        User user = new CommonUser("Ali@gmail.com", "password", Collections.emptyList(), Collections.emptyMap());
        ((InMemoryUserDataAccessObject) userRepository ).save(user);
        ((InMemoryUserDataAccessObject) userRepository ).setCurrentUsername("Ali@gmail.com");
        
        // This creates a successPresenter that tests whether the test case is as we expect.
        SavedArticlesOutputBoundary successPresenter = new SavedArticlesOutputBoundary() {
            @Override
            public void prepareSuccessView(SavedArticleOutputData user) {
                assertEquals("Ali@gmail.com", user.getUser().getName());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SavedArticlesInputBoundary interactor = new SavedArticlesInteractor(userRepository, successPresenter);
        interactor.execute();
    }

    @Test
    public void failureUserDoesNotExistTest() {
        SavedArticlesDataAccessInterface userRepository = new InMemoryUserDataAccessObject();


        // This creates a presenter that tests whether the test case is as we expect.
        SavedArticlesOutputBoundary failurePresenter = new SavedArticlesOutputBoundary() {
            @Override
            public void prepareSuccessView(SavedArticleOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No user logged in.", error);
            }
        };

        SavedArticlesInputBoundary interactor = new SavedArticlesInteractor(userRepository, failurePresenter);
        interactor.execute();
    }
}