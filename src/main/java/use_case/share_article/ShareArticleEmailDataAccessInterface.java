package use_case.share_article;

public interface ShareArticleEmailDataAccessInterface {
    void sendMail(String subject, String body, String recipient) throws Exception;
}
