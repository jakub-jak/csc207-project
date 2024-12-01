package interface_adapter.digest;

import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;

/**
 * Digest Presenter for the digest use case.
 */
public class DigestPresenter implements DigestOutputBoundary {
    private DigestOutputData outputData;
    private String errorMessage;

    public DigestPresenter() {
    }

    @Override
    public void handleError(String error) {
        this.errorMessage = error;
    }

    @Override
    public void processOutput(DigestOutputData digestOutputData) {
        this.outputData = digestOutputData;
    }

    @Override
    public DigestOutputData getOutputData() {
        return this.outputData;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public void prepareSuccessView(DigestOutputData digestOutputData) {
    }

    @Override
    public void prepareFailView(String error) {
    }
}
