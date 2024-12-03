package interface_adapter.logged_in;

import entity.Article;
import use_case.share_article.ShareArticleInputBoundary;
import use_case.share_article.ShareArticleInputData;
import use_case.share_article.ShareArticleInputData2;

/**
 * Share Article Controller.
 */
public class ShareArticleController {
    private final ShareArticleInputBoundary shareArticleInputBoundary;

    public ShareArticleController(ShareArticleInputBoundary shareArticleInputBoundary) {
        this.shareArticleInputBoundary = shareArticleInputBoundary;
    }

    /**
     * Executes the ShareArticleToMyself  Use Case.
     * @param article the article to add
     * @throws Exception exception
     */
    public void execute(Article article) throws Exception {
        final ShareArticleInputData shareArticleInputData = new ShareArticleInputData(article);
        shareArticleInputBoundary.execute(shareArticleInputData);
    }

    /**
     * Executes the ShareArticleWithOthers  Use Case.
     * @param article the article to add
     * @param recipientEmail the email to send the article to
     * @throws Exception exception
     */

    public void executeToOtherEmail(Article article, String recipientEmail) throws Exception {
        final ShareArticleInputData2 shareArticleInputData2 = new ShareArticleInputData2(article, recipientEmail);
        shareArticleInputBoundary.executeToOtherEmail(shareArticleInputData2);
    }
}