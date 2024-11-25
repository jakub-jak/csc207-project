package use_case.digest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import data_access.NewsDataAccessObject;
import data_access.CohereDataAccessObject;
import interface_adapter.digest.DigestPresenter;
import entity.Article;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DigestInteractorTest {

    private DigestNewsDataAccessInterface newsDataAccess;
    private DigestCohereDataAccessInterface cohereDataAccess;
    private DigestPresenter digestPresenter;
    private DigestInteractor digestInteractor;

    @BeforeEach
    public void setUp() {
        // Use the actual NewsAPI and CohereAPI implementations
        newsDataAccess = new NewsDataAccessObject();
        cohereDataAccess = new CohereDataAccessObject();
        digestPresenter = new DigestPresenter();
        digestInteractor = new DigestInteractor(newsDataAccess, cohereDataAccess, digestPresenter);
    }

    @Test
    public void testDigestInteractor() {
        // Arrange
        DigestInputData inputData = new DigestInputData("health",
                java.time.LocalDate.now().toString(),
                java.time.LocalDate.now().toString(),
                "en",
                "popularity",
                1,
                2);
        // Act
        digestInteractor.execute(inputData);

        // Assert
        String errorMessage = digestPresenter.getErrorMessage();

        if (!"Error in fetching articles".equals(errorMessage)) {
            DigestOutputData outputData = digestPresenter.getOutputData();
            assertNotNull(outputData, "Output data should not be null");
            List<Article> articles = outputData.getArticles();
            assertNotNull(articles, "Articles list should not be null");
            assertFalse(articles.isEmpty(), "Articles list should not be empty");

            for (Article article : articles) {
                assertNotEquals("Error in summarizing article", article.getDescription(),
                        "Article description should not be 'Error in summarizing article'");
            }
        } else {
            fail("Error in fetching articles");
        }
    }
}
