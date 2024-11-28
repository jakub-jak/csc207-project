package interface_adapter.saved_articles;

import entity.Article;

import java.util.ArrayList;
import java.util.List;

public class SavedArticlesState {
    private String username = "";
    private String savedArticlesError;
    private List<Article> articleList = new ArrayList<>();
    private List<String> categoriesFilterList = new ArrayList<>();

    public SavedArticlesState(SavedArticlesState copy) {
        this.username = copy.username;
        this.savedArticlesError = copy.savedArticlesError;
        this.articleList = copy.articleList;
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

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<String> getCategoriesFilterList() {
        return categoriesFilterList;
    }

    public void setCategoriesFilterList(List<String> categoriesFilterList) {
        this.categoriesFilterList = categoriesFilterList;
    }
}
