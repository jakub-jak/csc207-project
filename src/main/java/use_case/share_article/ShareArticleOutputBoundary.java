package use_case.share_article;

public interface ShareArticleOutputBoundary {
    void prepareSuccessView(ShareArticleOutputData outputData);
    void prepareFailView(String errorMessage);
}
