package use_case.digest;

import entity.Article;

import java.util.List;

public class DigestOutputData {

    private final List<Article> articles;

    public DigestOutputData(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
