package use_case.digest;

/**
 * Input Boundary for the Digest Use Case.
 */
public interface DigestInputBoundary {

    /**
     * Execute the digest use case.
     * @param digestInputData input data for the use case.
     */
    void execute(DigestInputData digestInputData);
}
