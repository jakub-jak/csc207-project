package interface_adapter.digest;

import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;

public class DigestPresenter implements DigestOutputBoundary {
    private DigestOutputData outputData;
    private String errorMessage;

    public DigestPresenter() {}

    @Override
    public void handleError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void processOutput(DigestOutputData outputData) {
        this.outputData = outputData;
    }

    @Override
    public DigestOutputData getOutputData() {
        return this.outputData;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
