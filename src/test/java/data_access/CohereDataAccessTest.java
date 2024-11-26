package data_access;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.digest.DigestCohereDataAccessInterface;
import data_access.CohereDataAccessObject;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CohereDataAccessTest {

    private DigestCohereDataAccessInterface cohereDataAccess;

    @BeforeEach
    public void setUp() {
        // Use the actual Cohere API implementation
        cohereDataAccess = new CohereDataAccessObject();
    }

    @Test
    public void testSummarizeSuccessfully() throws IOException {
        // Arrange
        String content = "This is a sample text for summarization, " +
                "note that this text must be longer than 250 characters. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum eu fermentum eros. " +
                "Vivamus ac nisi malesuada, varius metus et, tincidunt lacus. Fusce cursus congue nulla non pulvinar.";

        // Act
        String summary = cohereDataAccess.summarize(content);

        // Assert
        assertNotNull(summary, "Summary should not be null");
        assertFalse(summary.isEmpty(), "Summary should not be empty");
    }

    @Test
    public void testSummarizeFailure() {
        // Arrange
        String invalidContent = ""; // Invalid input to simulate failure

        // Act & Assert
        assertThrows(IOException.class, () -> {
            cohereDataAccess.summarize(invalidContent);
        }, "Should throw IOException for invalid input");
    }
}
