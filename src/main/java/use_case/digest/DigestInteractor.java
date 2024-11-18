package use_case.digest;

import entity.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DigestInteractor implements DigestInputBoundary{

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
        final String keyword = digestInputData.getKeyword();
        final String fromDate = digestInputData.getFromDate();
        final String toDate =  digestInputData.getToDate();
        final String language = digestInputData.getLanguage();
        final String sortBy = digestInputData.getSortBy();
        final int page = digestInputData.getPage();
        final int pageSize = digestInputData.getPageSize();

        List<Article> articles = new ArrayList<>();

        try {
            articles = digestNewsDataAccessInterface.fetchArticlesByKeyword(keyword, fromDate, toDate, language, sortBy, page, pageSize);
        } catch (IOException e) {
            digestPresenter.handleError("Error in fetching articles");
            e.printStackTrace();
        }

        for (Article article : articles) {
            try {
                article.setDescription(digestCohereDataAccessInterface.summarize(article.getContent()));
            } catch (IOException e) {
                article.setDescription("Error in summarizing article");
                e.printStackTrace();
            }
        }

        final DigestOutputData digestOutputData = new DigestOutputData(articles);
        digestPresenter.processOutput(digestOutputData);
    }
}
