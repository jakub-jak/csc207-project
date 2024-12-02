package use_case.digest;

import entity.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Digest interactor.
 */
public class DigestInteractor implements DigestInputBoundary {
    private final DigestNewsDataAccessInterface digestNewsDataAccessInterface;
    private final DigestCohereDataAccessInterface digestCohereDataAccessInterface;
    private final DigestOutputBoundary digestPresenter;

    public DigestInteractor(DigestNewsDataAccessInterface digestNewsDataAccessInterface, DigestCohereDataAccessInterface digestCohereDataAccessInterface, DigestOutputBoundary digestPresenter) {
        this.digestNewsDataAccessInterface = digestNewsDataAccessInterface;
        this.digestCohereDataAccessInterface = digestCohereDataAccessInterface;
        this.digestPresenter = digestPresenter;
    }

    @Override
    public void execute(DigestInputData digestInputData) {
        final String[] keywords = digestInputData.getKeywords();
        final String fromDate = digestInputData.getFromDate();
        final String toDate = digestInputData.getToDate();
        final String language = digestInputData.getLanguage();
        final String sortBy = digestInputData.getSortBy();

        List<Article> articles = new ArrayList<>();

        try {
            articles = digestNewsDataAccessInterface.fetchFirstMultiple(keywords, fromDate, toDate, language, sortBy);
        }
        catch (IOException ioException) {
            digestPresenter.prepareFailView("Error in fetching articles");
            ioException.printStackTrace();
        }

        for (Article article : articles) {
            try {
                article.setDescription(digestCohereDataAccessInterface.summarize(article.getContent()));
            }
            catch (IOException ioException) {
                article.setDescription("Error in summarizing article");
                ioException.printStackTrace();
            }
        }

        final DigestOutputData digestOutputData = new DigestOutputData(articles);
        digestPresenter.prepareSuccessView(digestOutputData);
    }
}
