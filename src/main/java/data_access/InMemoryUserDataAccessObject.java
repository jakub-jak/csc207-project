package data_access;

import entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject {

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
}
