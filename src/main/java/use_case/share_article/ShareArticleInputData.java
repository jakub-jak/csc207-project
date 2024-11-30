package use_case.share_article;

import entity.Article;

/**
 * The Input Data for the ShareArticle Use Case.
 */
public class ShareArticleInputData {
    private final Article article;

    public ShareArticleInputData(Article article) { this.article = article; }

    public Article getArticle() { return article; }
}

