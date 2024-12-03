package use_case.save_article;

import java.util.List;
import java.util.Map;

import entity.Article;

/**
 * Save Article Interactor.
 */
public class SaveArticleInteractor implements SaveArticleInputBoundary {
    private final SaveArticleDataAccessInterface saveArticleDataAccessObject;
    private final SaveArticleOutputBoundary saveArticlePresenter;

    public SaveArticleInteractor(SaveArticleDataAccessInterface saveArticleDataAccessInterface,
                                 SaveArticleOutputBoundary saveArticleOutputBoundary) {
        this.saveArticleDataAccessObject = saveArticleDataAccessInterface;
        this.saveArticlePresenter = saveArticleOutputBoundary;
    }

    @Override
    public void execute(SaveArticleInputData saveArticleInputData) {
        final Article article = saveArticleInputData.getArticle();
        final Map<String, List<Article>> articlesMap = saveArticleDataAccessObject.getUserArticles();

        if (containsArticle(articlesMap, article)) {
            saveArticlePresenter.prepareFailView("Article already saved.");
        }
        else {
            saveArticleDataAccessObject.saveArticle(article);
            final SaveArticleOutputData saveArticleOutputData = new SaveArticleOutputData(article, false);
            saveArticlePresenter.prepareSuccessView(saveArticleOutputData);
        }
    }

    /**
     * Checks if the given article exists in the provided map of articles.
     * @param articlesMap A map containing lists of articles, where the key is a string;
     *                    (category) and the value is a list of articles in that category.
     * @param article The article to check for existence in the map.
     * @return {@code true} if an article with the same title, author, date, and link, otherwise {@code false}.
     */
    private boolean containsArticle(Map<String, List<Article>> articlesMap, Article article) {
        for (List<Article> articlesList : articlesMap.values()) {
            for (Article compareArticle : articlesList) {
                if (compareArticle.getTitle().equals(article.getTitle())
                        && compareArticle.getAuthor().equals(article.getAuthor())
                        && compareArticle.getDate().equals(article.getDate())
                        && compareArticle.getLink().equals(article.getLink())) {
                    return true;
                }
            }
        }
        return false;
    }
}
