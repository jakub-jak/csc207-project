package use_case.unsave_article;

import data_access.InMemoryUserDataAccessObject;
import entity.Article;
import entity.CommonArticle;
import entity.CommonUser;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class UnsaveArticleInteractorTest {

    @Test
    public void testUnsaveArticle() {
        Article article = new CommonArticle("t", "au", "horror", "con", "li", "d", "a");
        UnsaveArticleInputData inputData = new UnsaveArticleInputData(article);

        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), new HashMap<>());
        userRepository.save(user);
        userRepository.setCurrentUser("Paul");

        // add the test article
        userRepository.saveArticle(article);

        // This creates a successPresenter that test whether the test case is as we expect
        UnsaveArticleOutputBoundary successPresenter = new UnsaveArticleOutputBoundary() {
            @Override
            public void prepareSuccessView(UnsaveArticleOutputData outputData) {
                assertTrue(user.getArticles().containsKey(article.getCategory()));
                assertFalse(user.getArticles().get(article.getCategory()).contains(article));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected error: " + errorMessage);
            }
        };

        UnsaveArticleInputBoundary interactor = new UnsaveArticleInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void failureArticleNotSaved() {
        Article article = new CommonArticle("t", "au", "horror", "con", "li", "d", "a");
        UnsaveArticleInputData inputData = new UnsaveArticleInputData(article);

        InMemoryUserDataAccessObject userRepository = new InMemoryUserDataAccessObject();

        User user = new CommonUser("Paul", "password", new ArrayList<>(), new HashMap<>());
        userRepository.save(user);
        userRepository.setCurrentUser("Paul");

        // This creates a successPresenter that test whether the test case is as we expect.
        UnsaveArticleOutputBoundary successPresenter = new UnsaveArticleOutputBoundary() {
            @Override
            public void prepareSuccessView(UnsaveArticleOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Article is not saved.", errorMessage);
            }
        };

        UnsaveArticleInputBoundary interactor = new UnsaveArticleInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }
}
