package use_case.digest;

public interface DigestOutputBoundary {
    void handleError(String errorMessage);

    void processOutput(DigestOutputData outputData);

    DigestOutputData getOutputData();

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
