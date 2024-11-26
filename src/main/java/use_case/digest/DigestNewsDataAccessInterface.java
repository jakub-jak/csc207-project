package use_case.digest;

import entity.Article;

import java.io.IOException;
import java.util.List;

public interface DigestNewsDataAccessInterface {

    List<Article> fetchArticlesByKeyword(String keyword,
                                         String fromDate,
                                         String toDate,
                                         String language,
                                         String sortBy,
                                         int page,
                                         int pageSize) throws IOException;

    Article fetchFirstArticle(String keyword, String fromDate, String toDate, String language, String sortBy) throws IOException;

    List<Article> fetchFirstMultiple(String[] keywords, String fromDate, String toDate, String language, String sortBy) throws IOException;
}
