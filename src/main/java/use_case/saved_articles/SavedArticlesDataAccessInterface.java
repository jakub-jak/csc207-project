package use_case.saved_articles;

import entity.User;

public interface SavedArticlesDataAccessInterface {
    public User get(String username);
    public String getCurrentUsername();
}
