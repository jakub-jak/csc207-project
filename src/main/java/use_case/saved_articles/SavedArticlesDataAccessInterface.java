package use_case.saved_articles;

import entity.User;

/**
 * DAI for the saved articles use case.
 */
public interface SavedArticlesDataAccessInterface {
    /**
     * Gets the user.
     * @param username username to get
     * @return user
     */
    User get(String username);

    /**
     * Gets the current username.
     * @return username
     */
    String getCurrentUsername();
}
