package interface_adapter.logged_in;

import use_case.saved_articles.SavedArticlesInputBoundary;

/**
 * Saved articles controller.
 */
public class SavedArticlesController {
    private SavedArticlesInputBoundary savedArticlesInputBoundary;

    public SavedArticlesController(SavedArticlesInputBoundary savedArticlesInputBoundary) {
        this.savedArticlesInputBoundary = savedArticlesInputBoundary;
    }

    /**
     * Execute the use case.
     */
    public void execute() {
        savedArticlesInputBoundary.execute();
    }
}
