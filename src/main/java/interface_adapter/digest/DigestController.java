package interface_adapter.digest;

import use_case.digest.DigestInputBoundary;
import use_case.digest.DigestInputData;

public class DigestController {

    private final DigestInputBoundary digestUseCaseInteractor;

    public DigestController(DigestInputBoundary digestUseCaseInteractor) {
        this.digestUseCaseInteractor = digestUseCaseInteractor;
    }

    public void execute(String keyword, String fromDate, String toDate, String language, String sortBy, int page, int pageSize) {
        final DigestInputData digestInputData = new DigestInputData(keyword, fromDate, toDate, language, sortBy, page, pageSize);

        digestUseCaseInteractor.execute(digestInputData);
    }
}
