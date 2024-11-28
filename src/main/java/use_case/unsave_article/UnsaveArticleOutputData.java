package use_case.unsave_article;

import entity.Article;

/**
 * Output Data for the UnsaveArticle Use Case.
 */
public class UnsaveArticleOutputData {
    private final Article article;
    private final boolean useCaseFailed;

    public UnsaveArticleOutputData(Article article, boolean useCaseFailed) {
        this.article = article;
        this.useCaseFailed = useCaseFailed;
    }

    public Article getArticle() { return article; }
}
