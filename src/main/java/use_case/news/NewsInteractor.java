package use_case.news;

/**
 * Interactor for the news use case.
 */
public class NewsInteractor implements NewsInputBoundary {
    private final NewsOutputBoundary newsPresenter;

    public NewsInteractor(NewsOutputBoundary newsPresenter) {
        this.newsPresenter = newsPresenter;
    }

    /**
     * Executes the news use case.
     */
    @Override
    public void execute() {
        this.newsPresenter.prepareSuccessView();
    }
}
