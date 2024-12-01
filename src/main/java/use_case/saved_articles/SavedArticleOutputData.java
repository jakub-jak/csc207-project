package use_case.saved_articles;

import entity.User;

public class SavedArticleOutputData {
    private User user;

    public SavedArticleOutputData(User user) {
        this.user = user;
    }

    public User getUser() { return this.user; }
}
