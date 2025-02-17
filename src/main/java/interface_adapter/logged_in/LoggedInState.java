package interface_adapter.logged_in;

import java.util.ArrayList;
import java.util.List;

import entity.Article;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";
    private String loggedInError;
    private List<String> categoriesList = new ArrayList<>();
    private List<Article> articleList = new ArrayList<>();

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public List<String> getCategoriesList() {
        return categoriesList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setCategoriesList(List<String> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoggedInError() {
        return loggedInError;
    }

    public void setLoggedInError(String loggedInError) {
        this.loggedInError = loggedInError;
    }

    /**
     * Adds a category to the list.
     * @param category category to add
     */
    public void addCategory(String category) {
        this.categoriesList.add(category);
    }

    /**
     * Removes a category to the list.
     * @param category category to remove
     */
    public void removeCategory(String category) {
        this.categoriesList.remove(category);
    }
}
