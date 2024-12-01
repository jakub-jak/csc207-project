package interface_adapter.digest;

import use_case.digest.DigestInputBoundary;
import use_case.digest.DigestInputData;

/**
 * Controller for the Digest Use Case.
 */
public class DigestController {

    private final DigestInputBoundary digestUseCaseInteractor;

    public DigestController(DigestInputBoundary digestUseCaseInteractor) {
        this.digestUseCaseInteractor = digestUseCaseInteractor;
    }

    /**
     * Execute the Digest Use Case.
     * @param keywords keywords
     * @param fromDate date start
     * @param toDate date end
     * @param language language
     * @param sortBy how to sort
     */
    public void execute(String[] keywords, String fromDate, String toDate, String language, String sortBy) {
        final DigestInputData digestInputData = new DigestInputData(keywords, fromDate, toDate, language, sortBy);

        digestUseCaseInteractor.execute(digestInputData);
    }

    /**
     * Execute the Digest Use Case.
     * @param keywords keyword
     */
    public void execute(String[] keywords) {
        final String oneWeekAgo = java.time.LocalDate.now().minusWeeks(1).toString();
        final String today = java.time.LocalDate.now().toString();
        final String english = "en";
        final String relevancy = "relevancy";

        this.execute(keywords, oneWeekAgo, today, english, relevancy);
    }
}
