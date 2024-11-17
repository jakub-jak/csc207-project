package interface_adapter.digest;

import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;

public class DigestPresenter implements DigestOutputBoundary {

    @Override
    public void prepareFailView(String errorMessage) {

    }

    @Override
    public void prepareSuccessView(DigestOutputData outputData) {

    }
}
