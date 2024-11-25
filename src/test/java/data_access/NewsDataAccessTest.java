package data_access;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.digest.DigestNewsDataAccessInterface;
import data_access.NewsDataAccessObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class NewsDataAccessTest {

    private DigestNewsDataAccessInterface newsDataAccess;

    @BeforeEach
    public void setUp() {
        // Use the actual News API implementation
        newsDataAccess = new NewsDataAccessObject();
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
}
