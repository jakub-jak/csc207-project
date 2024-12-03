package use_case.share_article;

import entity.Article;

public class ShareArticleInputData2 {
    private final Article article;
    private final String email;

    public ShareArticleInputData2(Article article, String email) {
        this.article = article;
        this.email = email;
    }

    public Article getArticle() {
        return article;
    }

    public String getEmail() {
        return email;
    }
}
