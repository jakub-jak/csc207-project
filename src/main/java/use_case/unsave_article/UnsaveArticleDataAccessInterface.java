package use_case.unsave_article;

import entity.Article;

import java.util.List;
import java.util.Map;

/**
 * Unsave Article Data Acess Interface.
 */
public interface UnsaveArticleDataAccessInterface {
    /**
     * Removes the article from the user's articles.
     * @param article the article to remove
     */
    void removeArticle(Article article);

    /**
     * Gets the given user's articles.
     * @return the current user's articles.
     */
    Map<String, List<Article>> getUserArticles();
}
