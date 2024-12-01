package use_case.share_article;

/**
 * Share Article Data access interface.
 */
public interface ShareArticleEmailDataAccessInterface {

    /**
     * Sends mail.
     * @param subject subject
     * @param body body
     * @param recipient recipient
     * @throws Exception excpetion
     */
    void sendMail(String subject, String body, String recipient) throws Exception;
}
