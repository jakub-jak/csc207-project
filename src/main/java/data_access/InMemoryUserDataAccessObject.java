package data_access;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {

    private String currentUserName;
    private final List<User> Users = new ArrayList<User>();

    public boolean existsByName(String name) {
        for (User user : Users) {
            if (user.getName().equals(name)) return true;
        }
        return false;
    }

    public void save(User user) { Users.add(user); }

    public User get(String name) {
        for (User user : Users) {
            if (user.getName().equals(name)) return user;
        }
        return null;
    }

    public void setCurrentUser(String name) {
        this.currentUserName = name;
    }

    public String getCurrentUser() {
        if (currentUserName == null) return null;
        return currentUserName;
    }

}
