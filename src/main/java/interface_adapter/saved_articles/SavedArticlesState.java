package interface_adapter.saved_articles;

import entity.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SavedArticlesState() {}

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

    public List<Article> getArticlesByCategory(String category) {
        return articleMap.get(category);
    }

    public List<Article> getArticleList() {
        List<Article> articles = new ArrayList<>();
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

    public void addCategory(String category) {
        this.categoriesFilterList.add(category);
    }

    public void removeCategory(String category) {
        this.categoriesFilterList.remove(category);
    }
}
