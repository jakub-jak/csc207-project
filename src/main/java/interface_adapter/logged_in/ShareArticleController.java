package interface_adapter.logged_in;

import entity.Article;
import use_case.share_article.ShareArticleInputBoundary;
import use_case.share_article.ShareArticleInputData;

/**
 * Share Article Controller.
 */
public class ShareArticleController {
    private final ShareArticleInputBoundary shareArticleInputBoundary;

    public ShareArticleController(ShareArticleInputBoundary shareArticleInputBoundary) {
        this.shareArticleInputBoundary = shareArticleInputBoundary;
    }

    /**
     * Executes the ShareArticle Use Case.
     * @param article the article to add
     * @throws Exception exception
     */
    public void execute(Article article) throws Exception {
        final ShareArticleInputData shareArticleInputData = new ShareArticleInputData(article);
        shareArticleInputBoundary.execute(shareArticleInputData);
    }
}