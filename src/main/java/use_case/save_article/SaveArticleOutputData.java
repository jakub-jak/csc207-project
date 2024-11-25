package use_case.save_article;

import entity.Article;

/**
 * Output Data for the SaveArticle Use Case.
 */
public class SaveArticleOutputData {
    private final Article article;
    private final boolean useCaseFailed;

    public SaveArticleOutputData(Article article, boolean useCaseFailed) {
        this.article = article;
        this.useCaseFailed = useCaseFailed;
    }

    public Article getArticle() { return article; }
}
