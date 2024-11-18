package use_case.digest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.digest.DigestCohereDataAccessInterface;
import use_case.digest.DigestNewsDataAccessInterface;
import data_access.NewsDataAccessObject;
import data_access.CohereDataAccessObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DigestInteractorTest {

    private DigestNewsDataAccessInterface newsDataAccess;
    private DigestCohereDataAccessInterface cohereDataAccess;

    @BeforeEach
    public void setUp() {
        // Use the actual NewsAPI and CohereAPI implementations
        newsDataAccess = new NewsDataAccessObject();
        cohereDataAccess = new CohereDataAccessObject();
    }

    @Test
    public void testFetchArticlesSuccessfully() throws IOException {
        // Arrange
        String keyword = "technology";
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String language = "en";
        String sortBy = "popularity";
        int page = 1;
        int pageSize = 5;

        // Act
        var articles = newsDataAccess.fetchArticlesByKeyword(keyword, fromDate, null, language, sortBy, page, pageSize);

        // Assert
        assertNotNull(articles, "Articles should not be null");
        assertTrue(articles.size() > 0, "Articles list should not be empty");
        assertEquals(5, articles.size(), "Articles list should contain 5 items");
    }

    @Test
    public void testFetchArticlesFailure() {
        // Arrange
        String invalidKeyword = ""; // Invalid input to simulate failure
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String language = "en";
        String sortBy = "popularity";
        int page = 1;
        int pageSize = 5;

        // Act & Assert
        assertThrows(IOException.class, () -> {
            newsDataAccess.fetchArticlesByKeyword(invalidKeyword, fromDate, null, language, sortBy, page, pageSize);
        }, "Should throw IOException for invalid input");
    }

    @Test
    public void testSummarizeSuccessfully() throws IOException {
        // Arrange
        String content = "This is a sample text for summarization, " +
                "note that this text must be longer than 250 characters. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum eu fermentum eros. " +
                "Vivamus ac nisi malesuada, varius metus et, tincidunt lacus. Fusce cursus congue nulla non pulvinar.";

        // Act
        String summary = cohereDataAccess.summarize(content);

        // Assert
        assertNotNull(summary, "Summary should not be null");
        assertFalse(summary.isEmpty(), "Summary should not be empty");
    }

    @Test
    public void testSummarizeFailure() {
        // Arrange
        String invalidContent = ""; // Invalid input to simulate failure

        // Act & Assert
        assertThrows(IOException.class, () -> {
            cohereDataAccess.summarize(invalidContent);
        }, "Should throw IOException for invalid input");
    }
}
