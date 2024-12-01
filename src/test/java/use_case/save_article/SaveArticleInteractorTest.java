package use_case.save_article;

import data_access.InMemoryUserDataAccessObject;
import entity.Article;
import entity.CommonArticle;
import entity.CommonUser;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SaveArticleInteractorTest {
    @Test
    public void testSaveArticle() {
        Article article = new CommonArticle("t", "au", "horror", "con", "li", "d", "a");
        SaveArticleInputData inputData = new SaveArticleInputData(article);

        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), new HashMap<>());
        userRepository.save(user);
        userRepository.setCurrentUsername("Paul");

        // This creates a successPresenter that test whether the test case is as we expect.
        SaveArticleOutputBoundary successPresenter = new SaveArticleOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveArticleOutputData outputData) {
                assertTrue(user.getArticles().containsKey(article.getCategory()));
                assertTrue(user.getArticles().get(article.getCategory()).contains(article));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected error: " + errorMessage);
            }
        };

        SaveArticleInputBoundary interactor = new SaveArticleInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureArticleAlreadySaved() {
        Article article = new CommonArticle("t", "au", "horror", "con", "li", "d", "a");
        SaveArticleInputData inputData = new SaveArticleInputData(article);
        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), new HashMap<>());
        userRepository.save(user);
        userRepository.setCurrentUsername("Paul");

        // add the test article
        userRepository.saveArticle(article);

        // This creates a successPresenter that test whether the test case is as we expect.
        SaveArticleOutputBoundary successPresenter = new SaveArticleOutputBoundary() {
            @Override
            public void prepareSuccessView(SaveArticleOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Article already saved.", errorMessage);
            }
        };

        SaveArticleInputBoundary interactor = new SaveArticleInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }
}
