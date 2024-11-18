package data_access;

import entity.User;
import use_case.add_category.AddCategoryDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.remove_category.RemoveCategoryDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface, LoginUserDataAccessInterface,
        AddCategoryDataAccessInterface, RemoveCategoryDataAccessInterface {

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
            if (user.getName().equals(name)) return user;
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
    public void saveAddedCategory(String username, String category) {
        this.get(username).addCategory(category);
    }

    @Override
    public void saveRemovedCategory(String username, String category) {
        this.get(username).deleteCategory(category);
    }

    @Override
    public List<String> getUserCategories(String username) {
        return this.get(username).getCategories();
    }
}
