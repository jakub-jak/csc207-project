package use_case.digest;

/**
 * Digest Input Boundary.
 */
public interface DigestOutputBoundary {
    /**
     * Handles Error.
     * @param errorMessage error message
     */
    void handleError(String errorMessage);

    /**
     * Process the output.
     * @param outputData output data
     */
    void processOutput(DigestOutputData outputData);

    /**
     * Gets the digest output data.
     * @return Digest output data
     */
    DigestOutputData getOutputData();

    /**
     * Gets the error message.
     * @return error message
     */
    String getErrorMessage();

    /**
     * Prepares the success view for the Digest Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(DigestOutputData outputData);

    /**
     * Prepares the failure view for the Digest Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
