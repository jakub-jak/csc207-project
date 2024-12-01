package use_case.share_article;

import entity.Article;

/**
 * Interactor for the share article use case.
 */
public class ShareArticleInteractor implements ShareArticleInputBoundary {
    private final ShareArticleUserDataAccessInterface shareArticleUserDataAccessObject;
    private final ShareArticleEmailDataAccessInterface shareArticleEmailDataAccessObject;

    public ShareArticleInteractor(ShareArticleUserDataAccessInterface shareArticleUserDataAccessInterface,
                                  ShareArticleEmailDataAccessInterface shareArticleEmailDataAccessInterface) {
        this.shareArticleUserDataAccessObject = shareArticleUserDataAccessInterface;
        this.shareArticleEmailDataAccessObject = shareArticleEmailDataAccessInterface;
    }

    @Override
    public void execute(ShareArticleInputData shareArticleInputData) throws Exception {
        final Article article = shareArticleInputData.getArticle();
        final String email = shareArticleUserDataAccessObject.getCurrentUser();
        final String subject = article.getTitle();
        final String emailBody = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        border: 1px solid #ddd;
                        border-radius: 8px;
                        background-color: #f9f9f9;
                    }
                    .article-title {
                        font-size: 1.5em;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .article-date {
                        font-size: 0.9em;
                        color: #777;
                        margin-bottom: 20px;
                    }
                    .article-description {
                        font-size: 1em;
                        margin-bottom: 20px;
                    }
                    .article-author {
                        font-size: 0.9em;
                        font-style: italic;
                        margin-bottom: 20px;
                    }
                    .article-link {
                        display: inline-block;
                        margin-top: 10px;
                        padding: 10px 20px;
                        font-size: 1em;
                        color: #fff;
                        background-color: #007BFF;
                        text-decoration: none;
                        border-radius: 4px;
                    }
                    .article-link:hover {
                        background-color: #0056b3;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="article-title">%s</div>
                    <div class="article-date">Published on: %s</div>
                    <div class="article-description">%s</div>
                    <div class="article-author">Written by: %s</div>
                    <a href="%s" class="article-link">Read the Full Article</a>
                </div>
            </body>
            </html>
            """.formatted(
                        article.getTitle(),
                        article.getDate(),
                        article.getDescription(),
                        article.getAuthor(),
                        article.getLink()
                );

        shareArticleEmailDataAccessObject.sendMail(subject, emailBody, email);
    }
}
