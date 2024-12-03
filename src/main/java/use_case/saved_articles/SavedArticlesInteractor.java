package use_case.saved_articles;

/**
 * Saved articles use case interactor.
 */
public class SavedArticlesInteractor implements SavedArticlesInputBoundary {
    private final SavedArticlesOutputBoundary savedArticlesOutputBoundary;
    private final SavedArticlesDataAccessInterface savedArticlesDataAccessInterface;

    public SavedArticlesInteractor(SavedArticlesDataAccessInterface savedArticlesDataAccessInterface,
                                   SavedArticlesOutputBoundary savedArticlesOutputBoundary) {
        this.savedArticlesDataAccessInterface = savedArticlesDataAccessInterface;
        this.savedArticlesOutputBoundary = savedArticlesOutputBoundary;
    }

    @Override
    public void execute() {
        final SavedArticleOutputData savedArticleOutputData =
                new SavedArticleOutputData(savedArticlesDataAccessInterface
                        .get(savedArticlesDataAccessInterface.getCurrentUsername()));

        try {
            savedArticleOutputData.getUser().getName();
            savedArticlesOutputBoundary.prepareSuccessView(savedArticleOutputData);
        }
        catch (NullPointerException nullPointerException) {
            savedArticlesOutputBoundary.prepareFailView("No user logged in.");
        }
    }
}
