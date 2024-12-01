package entity;

import java.util.List;
import java.util.Map;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Returns the article categories of the user.
     * @return the article categories of the user.
     */
    List<String> getCategories();

    /**
     * Returns the articles of the user.
     * @return the articles of the user.
     */
    Map<String, List<Article>> getArticles();

    /**
     * Update user category preferences.
     * @param categories categories.
     */
    void setCategories(List<String> categories);

    /**
     * Update user articles.
     * @param articles articles
     */
    void setArticles(Map<String, List<Article>> articles);

    /**
     * Adds one category.
     * @param category category
     */
    void addCategory(String category);

    /**
     * Deletes one category.
     * @param category category
     */
    void deleteCategory(String category);

    /**
     * Adds one article.
     * @param article article
     */
    void addArticle(Article article);

    /**
     * Deletes one article.
     * @param article article
     */
    void deleteArticle(Article article);
}
