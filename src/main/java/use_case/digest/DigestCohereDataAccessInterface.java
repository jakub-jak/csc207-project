package use_case.digest;

import java.io.IOException;

/**
 * Data Access Interface for the Cohere model.
 */
public interface DigestCohereDataAccessInterface {

    /**
     * Sumarie the given input text.
     * @param inputText the text that should be summarized
     * @throws IOException exception
     */
    String summarize(String inputText) throws IOException;
}
