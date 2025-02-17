package use_case.save_article;

import java.util.List;
import java.util.Map;

import entity.Article;

/**
 * Save Article DAI.
 */
public interface SaveArticleDataAccessInterface {
    /**
     * Saves the article to the user's articles.
     * @param article the article to save
     */
    void saveArticle(Article article);

    /**
     * Gets the given user's articles.
     * @return the given user's articles.
     */
    Map<String, List<Article>> getUserArticles();
}
