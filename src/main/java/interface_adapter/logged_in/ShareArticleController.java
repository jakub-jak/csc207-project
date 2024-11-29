package interface_adapter.logged_in;

import entity.Article;
import use_case.share_article.ShareArticleInputBoundary;
import use_case.share_article.ShareArticleInputData;


public class ShareArticleController {
    private final ShareArticleInputBoundary ShareArticleUseCaseInteractor;

    public ShareArticleController(ShareArticleInputBoundary ShareArticleUseCaseInteractor) {
        this.ShareArticleUseCaseInteractor = ShareArticleUseCaseInteractor;
    }

    /**
     * Executes the ShareArticle Use Case.
     * @param article the article to add
     */
    public void execute(Article article) throws Exception {
        final ShareArticleInputData ShareArticleInputData = new ShareArticleInputData(article);
        ShareArticleUseCaseInteractor.execute(ShareArticleInputData);
    }
}