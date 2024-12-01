package use_case.share_article;

import entity.User;

/**
 * Data Access Interface for the Share Article Use Case.
 */
public interface ShareArticleUserDataAccessInterface {
    /**
     * Gets the current user's name(email).
     * @return the current user's name(email).
     */
    String getCurrentUser();

    /**
     * Save user.
     * @param user user to save
     */
    void save(User user);

    /**
     * Set the current username.
     * @param paul paul?
     */
    void setCurrentUsername(String paul);
}
