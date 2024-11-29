package use_case.perform_filter;

import entity.Article;

import java.util.List;

public interface PerformFilterDataAccessInterface {

    List<Article> getArticles();
}
