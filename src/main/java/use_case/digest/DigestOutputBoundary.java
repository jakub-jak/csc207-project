package use_case.digest;

/**
 * Digest Input Boundary.
 */
public interface DigestOutputBoundary {
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
