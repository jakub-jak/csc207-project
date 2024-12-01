package interface_adapter.saved_articles;

import entity.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * State for the saved articles view.
 */
public class SavedArticlesState {
    private String username = "";
    private String savedArticlesError;
    private Map<String, List<Article>> articleMap = new HashMap<>();
    private List<String> categoriesFilterList = new ArrayList<>();

    public SavedArticlesState(SavedArticlesState copy) {
        this.username = copy.username;
        this.savedArticlesError = copy.savedArticlesError;
        this.articleMap = copy.articleMap;
        this.categoriesFilterList = copy.categoriesFilterList;
    }

    public SavedArticlesState() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSavedArticlesError() {
        return savedArticlesError;
    }

    public void setSavedArticlesError(String savedArticlesError) {
        this.savedArticlesError = savedArticlesError;
    }

    /**
     * Gets the specific article by category.
     * @param category category
     * @return list of aticles
     */
    public List<Article> getArticlesByCategory(String category) {
        return articleMap.get(category);
    }

    /**
     * Gets a list of articles.
     * @return list of articles
     */
    public List<Article> getArticleList() {
        final List<Article> articles = new ArrayList<>();
        for (String key : articleMap.keySet()) {
            articles.addAll(articleMap.get(key));
        }
        return articles;
    }

    public List<String> getCategoriesFilterList() {
        return categoriesFilterList;
    }

    public void setCategoriesFilterList(List<String> categoriesFilterList) {
        this.categoriesFilterList = categoriesFilterList;
    }

    /**
     * Add category to list.
     * @param category category
     */
    public void addCategory(String category) {
        this.categoriesFilterList.add(category);
    }

    /**
     * Remove category from list.
     * @param category category
     */
    public void removeCategory(String category) {
        this.categoriesFilterList.remove(category);
    }

    /**
     * Remove article from map.
     * @param article artilce
     */
    public void removeArticle(Article article) {
        articleMap.get(article.getCategory()).remove(article);
    }

    /**
     * Set the article map.
     * @param articleMap article map
     */
    public void setArticleMap(Map<String, List<Article>> articleMap) {
        this.articleMap = articleMap;
    }
}
