package data_access;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailDataAccessTest {

    private EmailDataAccessObject emailDataAccessObject;

    @BeforeEach
    public void setUp() throws Exception {
        emailDataAccessObject = new EmailDataAccessObject();
    }

    @Test
    public void testSendMailSuccessfully() throws Exception {
        // Arrange
        String subject = "Test Email";
        String message = "<html><body><h1>This is a test email</h1><p>Sent using EmailDataAccessObject</p></body>" +
                "</html>";
        String recipientEmail = "hridaycollege03@gmail.com";

        // Act & Assert
        assertDoesNotThrow(() -> {
            emailDataAccessObject.sendMail(subject, message, recipientEmail);
        }, "Sending email should not throw any exception for valid inputs.");
    }

    @Test
    public void testSendMailWithInvalidEmail() {
        // Arrange
        String subject = "Test Email";
        String message = "<html><body><h1>This is a test email</h1><p>Sent using EmailDataAccessObject</p></body>" +
                "</html>";
        String invalidEmail = "invalidEmail";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            emailDataAccessObject.sendMail(subject, message, invalidEmail);
        }, "Sending email to an invalid email address should throw an exception.");
    }




}
