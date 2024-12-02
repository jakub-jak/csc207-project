package use_case.digest;

import java.io.IOException;
import java.util.List;

import entity.Article;

/**
 * Data Access Interface for the Digest Use Case for the News API.
 */
public interface DigestNewsDataAccessInterface {
    /**
     * Fetch the article by a keyword.
     * @param keyword keyword
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @param page page
     * @param pageSize page size
     * @return List of Articles
     * @throws IOException exception
     */
    List<Article> fetchArticlesByKeyword(String keyword,
                                         String fromDate,
                                         String toDate,
                                         String language,
                                         String sortBy,
                                         int page,
                                         int pageSize) throws IOException;

    /**
     * Fetch the first article by a keyword.
     * @param keyword keyword
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @return List of Articles
     * @throws IOException exception
     */
    Article fetchFirstArticle(String keyword, String fromDate, String toDate, String language, String sortBy)
            throws IOException;

    /**
     * Fetch the first article by multiple keywords.
     * @param keywords keywords
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @return List of Articles
     * @throws IOException exception
     */
    List<Article> fetchFirstMultiple(String[] keywords, String fromDate, String toDate, String language, String sortBy)
            throws IOException;
}
