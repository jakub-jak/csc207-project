package data_access;

import entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.digest.DigestNewsDataAccessInterface;

import java.io.IOException;
import java.util.List;

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
    public void testFetchFirstArticleSuccessfully() throws IOException {
        // Arrange
        String keyword = "technology";
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String toDate = null;
        String language = "en";
        String sortBy = "popularity";

        // Act
        Article article = ((NewsDataAccessObject) newsDataAccess).fetchFirstArticle(keyword, fromDate, toDate, language, sortBy);

        // Assert
        assertNotNull(article, "Article should not be null");
        assertNotNull(article.getTitle(), "Article title should not be null");
        assertFalse(article.getTitle().isEmpty(), "Article title should not be empty");
    }

    @Test
    public void testFetchFirstArticleFailure() {
        // Arrange
        String invalidKeyword = ""; // Invalid input to simulate failure
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String toDate = null;
        String language = "en";
        String sortBy = "popularity";

        // Act & Assert
        assertThrows(IOException.class, () -> {
            ((NewsDataAccessObject) newsDataAccess).fetchFirstArticle(invalidKeyword, fromDate, toDate, language, sortBy);
        }, "Should throw IOException when no articles are found");
    }

    @Test
    public void testFetchFirstMultipleSuccessfully() throws IOException {
        // Arrange
        String[] keywords = {"technology", "science", "health"};
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String toDate = null;
        String language = "en";
        String sortBy = "popularity";

        // Act
        List<Article> articles = ((NewsDataAccessObject) newsDataAccess).fetchFirstMultiple(keywords, fromDate, toDate, language, sortBy);

        // Assert
        assertNotNull(articles, "Articles list should not be null");
        assertEquals(keywords.length, articles.size(), "Articles list size should match keywords array length");

        for (Article article : articles) {
            assertNotNull(article, "Article should not be null");
            assertNotNull(article.getTitle(), "Article title should not be null");
            assertFalse(article.getTitle().isEmpty(), "Article title should not be empty");
        }
    }

    @Test
    public void testFetchFirstMultipleWithSomeFailures() throws IOException {
        // Arrange
        String[] keywords = {"technology", "", "health", "nonexistentkeyword123456"};
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String toDate = null;
        String language = "en";
        String sortBy = "popularity";

        // Act
        List<Article> articles = ((NewsDataAccessObject) newsDataAccess).fetchFirstMultiple(keywords, fromDate, toDate, language, sortBy);

        // Assert
        assertNotNull(articles, "Articles list should not be null");
        assertTrue(articles.size() > 0, "Articles list should not be empty");
        assertTrue(articles.size() <= keywords.length, "Articles list size should be less than or equal to keywords array length");

        for (Article article : articles) {
            assertNotNull(article, "Article should not be null");
            assertNotNull(article.getTitle(), "Article title should not be null");
            assertFalse(article.getTitle().isEmpty(), "Article title should not be empty");
        }
    }

    @Test
    public void testFetchFirstMultipleFailure() throws IOException {
        // Arrange
        String[] invalidKeywords = {"nonexistentkeyword123456", "anothernonexistentkeyword987654"};
        String fromDate = java.time.LocalDate.now().toString(); // Current date
        String toDate = null;
        String language = "en";
        String sortBy = "popularity";

        // Act
        List<Article> articles = ((NewsDataAccessObject) newsDataAccess).fetchFirstMultiple(invalidKeywords, fromDate, toDate, language, sortBy);

        // Assert
        assertNotNull(articles, "Articles list should not be null");
        assertEquals(0, articles.size(), "Articles list should be empty when no articles are found");
    }
}
