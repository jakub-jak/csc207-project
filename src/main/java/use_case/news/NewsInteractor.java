package use_case.news;

public class NewsInteractor implements NewsInputBoundary{
    private final NewsOutputBoundary savedArticlesPresenter;

    public NewsInteractor(NewsOutputBoundary savedArticlesPresenter) {
        this.savedArticlesPresenter = savedArticlesPresenter;
    }
    /**
     * Executes the news use case.
     */
    @Override
    public void execute() {
        this.savedArticlesPresenter.prepareSuccessView();
    }
}
