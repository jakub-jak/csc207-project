package use_case.unsave_article;

import entity.Article;

/**
 * The Input Data for the UnsaveArticle Use Case.
 */
public class UnsaveArticleInputData {
    private final Article article;

    public UnsaveArticleInputData(Article article) { this.article = article; }

    public Article getArticle() { return article; }
}
