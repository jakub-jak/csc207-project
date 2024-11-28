package data_access;

import entity.Article;
import entity.User;
import use_case.add_category.AddCategoryDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.remove_category.RemoveCategoryDataAccessInterface;
import use_case.save_article.SaveArticleDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.unsave_article.UnsaveArticleDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface, AddCategoryDataAccessInterface, RemoveCategoryDataAccessInterface,
        SaveArticleDataAccessInterface, UnsaveArticleDataAccessInterface {

    private String currentUserName;
    private final List<User> Users = new ArrayList<User>();

    @Override
    public boolean existsByName(String name) {
        for (User user : Users) {
            if (user.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    public void save(User user) { Users.add(user); }


    @Override
    public User get(String name) {
        for (User user : Users) {
            if (user.getName().equals(this.currentUserName)) return user;
        }
        return null;
    }


    @Override
    public void setCurrentUser(String name) {
        this.currentUserName = name;
    }

    @Override
    public String getCurrentUser() {
        if (currentUserName == null) return null;
        return currentUserName;
    }

    @Override
    public void saveCategory(String category) {
        User currentUser = this.get(this.getCurrentUser());
        currentUser.addCategory(category);
    }

    @Override
    public void removeCategory(String category) {
        User currentUser = this.get(this.getCurrentUser());
        currentUser.deleteCategory(category);
    }

    @Override
    public List<String> getUserCategories() {
        User currentUser = this.get(this.getCurrentUser());
        return currentUser.getCategories();
    }

    @Override
    public void saveArticle(Article article) {
        User currentUser = this.get(this.getCurrentUser());
        currentUser.addArticle(article);
    }

    @Override
    public void removeArticle(Article article) {
        User currentUser = this.get(this.getCurrentUser());
        currentUser.deleteArticle(article);
    }

    @Override
    public Map<String, List<Article>> getUserArticles() {
        User currentUser = this.get(this.getCurrentUser());
        return currentUser.getArticles();
    }
}
