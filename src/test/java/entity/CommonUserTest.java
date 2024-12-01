package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommonUserTest {
    private CommonUser user;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        List<String> initialCategories = new ArrayList<>();
        initialCategories.add("Sports");
        initialCategories.add("Technology");

        Map<String, List<Article>> initialArticles = new HashMap<>();
        initialArticles.put("Sports", new ArrayList<>());
        initialArticles.put("Technology", new ArrayList<>());

        user = new CommonUser("JohnDoe", "password123", initialCategories, initialArticles);
    }

    @Test
    public void testGetName() {
        assertEquals("JohnDoe", user.getName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testGetCategories() {
        List<String> expectedCategories = new ArrayList<>();
        expectedCategories.add("Sports");
        expectedCategories.add("Technology");

        assertEquals(expectedCategories, user.getCategories());
    }

    @Test
    public void testGetArticles() {
        Map<String, List<Article>> expectedArticles = new HashMap<>();
        expectedArticles.put("Sports", new ArrayList<>());
        expectedArticles.put("Technology", new ArrayList<>());

        assertEquals(expectedArticles, user.getArticles());
    }

    @Test
    public void testAddCategory() {
        user.addCategory("Health");
        assertTrue(user.getCategories().contains("Health"));
    }

    @Test
    public void testDeleteCategory() {
        user.deleteCategory("Sports");
        assertFalse(user.getCategories().contains("Sports"));
    }

    @Test
    public void testAddArticle() {
        Article article = new CommonArticle("t", "t", "Sports", "t", "t", "t", "t");
        user.addArticle(article);

        assertTrue(user.getArticles().get("Sports").contains(article));
    }

    @Test
    public void testDeleteArticle() {
        Article article = new CommonArticle("t", "t", "Technology", "t", "t", "t", "t");
        user.addArticle(article);

        // Verify the article is added
        assertTrue(user.getArticles().get("Technology").contains(article));

        user.deleteArticle(article);

        // Verify the article is removed
        assertFalse(user.getArticles().get("Technology").contains(article));
    }

    @Test
    public void testSetCategories() {
        List<String> newCategories = new ArrayList<>();
        newCategories.add("Science");
        newCategories.add("Art");

        user.setCategories(newCategories);

        assertEquals(newCategories, user.getCategories());
    }

    @Test
    public void testSetArticles() {
        Map<String, List<Article>> newArticles = new HashMap<>();
        newArticles.put("Science", new ArrayList<>());
        user.setArticles(newArticles);

        assertEquals(newArticles, user.getArticles());
    }
}
