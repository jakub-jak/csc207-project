package use_case.digest;

import entity.CommonArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import data_access.NewsDataAccessObject;
import data_access.CohereDataAccessObject;
import entity.Article;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DigestInteractorTest {

    private DigestNewsDataAccessInterface newsDataAccess;
    private DigestCohereDataAccessInterface cohereDataAccess;

    @BeforeEach
    public void setUp() {
        newsDataAccess = new NewsDataAccessObject();
        cohereDataAccess = new CohereDataAccessObject();
    }

    @Test
    public void testDigestInteractorSuccess() {
        // Arrange
        DigestInputData inputData = new DigestInputData(new String[]{"technology", "health"},
                java.time.LocalDate.now().minusWeeks(1).toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity");

        // Create a successPresenter that tests whether the test case is as we expect.
        DigestOutputBoundary successPresenter = new DigestOutputBoundary() {

            @Override
            public void prepareSuccessView(DigestOutputData outputData) {
                assertNotNull(outputData, "Output data should not be null");
                List<Article> articles = outputData.getArticles();
                assertNotNull(articles, "Articles list should not be null");
                assertFalse(articles.isEmpty(), "Articles list should not be empty");

                for (Article article : articles) {
                    assertNotEquals("Error in summarizing article", article.getDescription(),
                            "Article description should not be 'Error in summarizing article'");
                }
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Error in fetching articles");
            }
        };

        DigestInputBoundary interactor = new DigestInteractor(newsDataAccess, cohereDataAccess, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    public void testDigestInteractorAllFail() {
        // Arrange
        DigestInputData inputData = new DigestInputData(
                new String[]{"nonexistentkeyword123456", "anothernonexistentkeyword987654"},
                java.time.LocalDate.now().minusWeeks(1).toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity"
        );

        DigestOutputBoundary failurePresenter = new DigestOutputBoundary() {
            @Override
            public void prepareSuccessView(DigestOutputData outputData) {
                assertNotNull(outputData, "Output data should not be null");
                List<Article> articles = outputData.getArticles();
                assertTrue(articles.isEmpty(), "Articles list should be empty");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Error in fetching articles");
            }
        };

        DigestInputBoundary interactor = new DigestInteractor(newsDataAccess, cohereDataAccess, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    public void testDigestInteractorPartialFail() {
        // Arrange
        DigestInputData inputData = new DigestInputData(
                new String[]{"technology", "nonexistentkeyword123456"},
                java.time.LocalDate.now().minusWeeks(1).toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity"
        );

        DigestOutputBoundary partialSuccessPresenter = new DigestOutputBoundary() {
            @Override
            public void prepareSuccessView(DigestOutputData outputData) {
                assertNotNull(outputData, "Output data should not be null");
                List<Article> articles = outputData.getArticles();
                assertNotNull(articles, "Articles list should not be null");
                assertFalse(articles.isEmpty(), "Articles list should not be empty");
                assertTrue(articles.size() >= 1, "At least one article should be fetched");

                for (Article article : articles) {
                    assertNotNull(article.getTitle(), "Article title should not be null");
                    assertFalse(article.getTitle().isEmpty(), "Article title should not be empty");
                    assertNotNull(article.getDescription(), "Article description should not be null");
                    assertFalse(article.getDescription().isEmpty(), "Article description should not be empty");
                }
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Expected partial success, but received failure: " + errorMessage);
            }
        };

        DigestInputBoundary interactor = new DigestInteractor(newsDataAccess, cohereDataAccess, partialSuccessPresenter);
        interactor.execute(inputData);
    }
    @Test
    public void testNewsDataAccessIOException() throws IOException {
        // Arrange
        DigestNewsDataAccessInterface newsDataAccess = Mockito.mock(DigestNewsDataAccessInterface.class);
        DigestCohereDataAccessInterface cohereDataAccess = Mockito.mock(DigestCohereDataAccessInterface.class);
        DigestOutputBoundary presenter = Mockito.mock(DigestOutputBoundary.class);

        // Simulate IOException when fetching articles
        Mockito.when(newsDataAccess.fetchFirstMultiple(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenThrow(new IOException("Simulated news data access failure"));

        // Create the interactor
        DigestInputBoundary interactor = new DigestInteractor(
                newsDataAccess,
                cohereDataAccess,
                presenter
        );

        // Create input data
        DigestInputData inputData = new DigestInputData(
                new String[]{"technology"},
                java.time.LocalDate.now().minusWeeks(1).toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        Mockito.verify(presenter).prepareFailView(Mockito.eq("Error in fetching articles"));
        Mockito.verify(presenter, Mockito.never()).prepareSuccessView(Mockito.any());
    }

    @Test
    public void testCohereDataAccessIOException() throws IOException {
        // Arrange
        DigestNewsDataAccessInterface newsDataAccess = Mockito.mock(DigestNewsDataAccessInterface.class);
        DigestCohereDataAccessInterface cohereDataAccess = Mockito.mock(DigestCohereDataAccessInterface.class);
        DigestOutputBoundary presenter = Mockito.mock(DigestOutputBoundary.class);

        // Prepare a list of articles to be returned by news data access
        List<Article> articles = new ArrayList<>();
        Article article = new CommonArticle("", "", "", "Some content", "", "", "");
        articles.add(article);

        // Simulate successful news data access
        Mockito.when(newsDataAccess.fetchFirstMultiple(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(articles);

        // Simulate IOException during summarization
        Mockito.when(cohereDataAccess.summarize(Mockito.anyString()))
                .thenThrow(new IOException("Simulated Cohere data access failure"));

        // Create the interactor
        DigestInputBoundary interactor = new DigestInteractor(
                newsDataAccess,
                cohereDataAccess,
                presenter
        );

        // Create input data
        DigestInputData inputData = new DigestInputData(
                new String[]{"technology"},
                java.time.LocalDate.now().minusWeeks(1).toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity"
        );

        // Act
        interactor.execute(inputData);

        // Assert
        // Verify that prepareSuccessView is called with an article with error description
        Mockito.verify(presenter).prepareSuccessView(Mockito.argThat(outputData -> {
            List<Article> processedArticles = outputData.getArticles();
            return !processedArticles.isEmpty() &&
                    processedArticles.get(0).getDescription().equals("Error in summarizing article");
        }));
    }
}
