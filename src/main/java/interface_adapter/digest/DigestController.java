package interface_adapter.digest;

import use_case.digest.DigestInputBoundary;
import use_case.digest.DigestInputData;
import use_case.digest.DigestOutputBoundary;
import use_case.digest.DigestOutputData;
import java.time.LocalDate;

public class DigestController {

    private final DigestInputBoundary digestUseCaseInteractor;
    private final DigestOutputBoundary digestPresenter;

    public DigestController(DigestInputBoundary digestUseCaseInteractor, DigestOutputBoundary digestPresenter) {
        this.digestUseCaseInteractor = digestUseCaseInteractor;
        this.digestPresenter = digestPresenter;
    }

    public void execute(String[] keywords, String fromDate, String toDate, String language, String sortBy) {
        final DigestInputData digestInputData = new DigestInputData(keywords, fromDate, toDate, language, sortBy);

        digestUseCaseInteractor.execute(digestInputData);

        if (digestPresenter.getErrorMessage() != null) {
            throw new RuntimeException("Error: " + digestPresenter.getErrorMessage());
        }
    }

    public void execute(String[] keywords) {
        final String oneWeekAgo = java.time.LocalDate.now().minusWeeks(1).toString();
        final String today = java.time.LocalDate.now().toString();
        final String english = "en";
        final String relevancy  = "relevancy";

        this.execute(keywords, oneWeekAgo, today, english, relevancy);
    }
}
