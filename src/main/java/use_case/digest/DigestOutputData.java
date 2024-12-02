package use_case.digest;

import java.util.List;

import entity.Article;

/**
 * Digest Output Data object.
 */
public class DigestOutputData {

    private final List<Article> articles;

    public DigestOutputData(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
