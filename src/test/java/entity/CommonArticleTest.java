package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommonArticleTest {

    private CommonArticle article;

    @BeforeEach
    void setUp() {
        // Initialize an instance of CommonArticle before each test
        article = new CommonArticle(
                "Title Example",
                "Author Name",
                "Technology",
                "This is the content of the article.",
                "https://example.com/article-link",
                "2024-12-01",
                "Short description of the article."
        );
    }

    @Test
    void testGetTitle() {
        assertEquals("Title Example", article.getTitle());
    }

    @Test
    void testGetAuthor() {
        assertEquals("Author Name", article.getAuthor());
    }

    @Test
    void testGetCategory() {
        assertEquals("Technology", article.getCategory());
    }

    @Test
    void testGetContent() {
        assertEquals("This is the content of the article.", article.getContent());
    }

    @Test
    void testGetLink() {
        assertEquals("https://example.com/article-link", article.getLink());
    }

    @Test
    void testGetDate() {
        assertEquals("2024-12-01", article.getDate());
    }

    @Test
    void testGetDescription() {
        assertEquals("Short description of the article.", article.getDescription());
    }

    @Test
    void testSetDescription() {
        // Change the description
        article.setDescription("Updated description of the article.");

        // Verify the description is updated
        assertEquals("Updated description of the article.", article.getDescription());
    }

    @Test
    void testInitialDescription() {
        // Ensure that the initial description is as expected
        assertEquals("Short description of the article.", article.getDescription());
    }
}
