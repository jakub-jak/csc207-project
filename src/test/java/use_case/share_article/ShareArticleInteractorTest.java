package use_case.share_article;

import data_access.MongoDBUserDataAccessObject;
import entity.Article;
import entity.CommonArticle;
import entity.CommonUser;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShareArticleInteractorTest {

    private ShareArticleUserDataAccessInterface userDataAccess;
    private ShareArticleEmailDataAccessInterface emailDataAccess;

    @BeforeEach
    public void setUp() {
        userDataAccess = new MongoDBUserDataAccessObject();
        emailDataAccess = new ShareArticleEmailDataAccessInterface() {
            @Override
            public void sendMail(String subject, String body, String recipient) throws Exception {
                // Simulate successful email sending
                assertNotNull(subject);
                assertNotNull(body);
                assertNotNull(recipient);
            }
        };
    }

    @Test
    public void testShareArticleSuccessfully() throws Exception {
        // Arrange
        User user = new CommonUser("Paul", "password", null, null);
        userDataAccess.save(user);
        userDataAccess.setCurrentUsername("Paul");

        Article article = new CommonArticle(
                "Amazing Article",
                "John Doe",
                "Technology",
                "This is a test description",
                "https://example.com",
                "2024-12-01",
                "Test Summary"
        );
        ShareArticleInputData inputData = new ShareArticleInputData(article);

        ShareArticleInteractor interactor = new ShareArticleInteractor(userDataAccess, emailDataAccess);

        // Act & Assert
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    public void testShareArticleFailEmailError() {
        // Arrange
        User user = new CommonUser("Paul", "password", null, null);
        userDataAccess.save(user);
        userDataAccess.setCurrentUsername("Paul");

        Article article = new CommonArticle(
                "Amazing Article",
                "John Doe",
                "Technology",
                "This is a test description",
                "https://example.com",
                "2024-12-01",
                "Test Summary"
        );
        ShareArticleInputData inputData = new ShareArticleInputData(article);

        ShareArticleEmailDataAccessInterface failingEmailService = new ShareArticleEmailDataAccessInterface() {
            @Override
            public void sendMail(String subject, String body, String recipient) throws Exception {
                throw new Exception("Email service is unavailable");
            }
        };

        ShareArticleInteractor interactor = new ShareArticleInteractor(userDataAccess, failingEmailService);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> interactor.execute(inputData));
        assertEquals("Email service is unavailable", exception.getMessage());
    }
}
