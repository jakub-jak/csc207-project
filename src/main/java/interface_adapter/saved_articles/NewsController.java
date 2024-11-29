package interface_adapter.saved_articles;

import use_case.news.NewsInputBoundary;

public class NewsController {
    private NewsInputBoundary newsUseCaseInteractor;

    public NewsController(NewsInputBoundary newsUseCaseInteractor) {
        this.newsUseCaseInteractor = newsUseCaseInteractor;
    }

    /**
     * Executes the News Use Case.
     */
    public void execute() {
        newsUseCaseInteractor.execute();
    }
}
