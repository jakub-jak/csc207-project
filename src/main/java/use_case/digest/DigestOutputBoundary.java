package use_case.digest;

public interface DigestOutputBoundary {
    void prepareFailView(String errorMessage);

    void prepareSuccessView(DigestOutputData outputData);
}
