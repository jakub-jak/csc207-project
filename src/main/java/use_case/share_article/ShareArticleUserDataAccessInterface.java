package use_case.share_article;

import entity.User;

public interface ShareArticleUserDataAccessInterface {
    /**
     * Gets the current user's name(email).
     * @return the current user's name(email).
     */
    String getCurrentUser();

    void save(User user);

    void setCurrentUsername(String paul);
}
