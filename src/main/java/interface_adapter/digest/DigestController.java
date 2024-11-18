package interface_adapter.digest;

import use_case.digest.DigestInputBoundary;
import use_case.digest.DigestInputData;
import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;

public class DigestController {

    private final DigestInputBoundary digestUseCaseInteractor;
    private final DigestOutputBoundary digestPresenter;

    public DigestController(DigestInputBoundary digestUseCaseInteractor, DigestOutputBoundary digestPresenter) {
        this.digestUseCaseInteractor = digestUseCaseInteractor;
        this.digestPresenter = digestPresenter;
    }

    public void execute(String keyword, String fromDate, String toDate, String language, String sortBy, int page, int pageSize) {
        final DigestInputData digestInputData = new DigestInputData(keyword, fromDate, toDate, language, sortBy, page, pageSize);

        digestUseCaseInteractor.execute(digestInputData);

        if (digestPresenter.getErrorMessage() != null) {
            throw new RuntimeException("Error: " + digestPresenter.getErrorMessage());
        }
    }
}
