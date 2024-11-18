package use_case.digest;

public interface DigestOutputBoundary {
    void handleError(String errorMessage);

    void processOutput(DigestOutputData outputData);

    DigestOutputData getOutputData();

    String getErrorMessage();
}
