package interface_adapter.logged_in;

import use_case.saved_articles.SavedArticlesInputBoundary;

public class SavedArticlesController {
    private SavedArticlesInputBoundary savedArticlesInputBoundary;

    public SavedArticlesController(SavedArticlesInputBoundary savedArticlesInputBoundary) {
        this.savedArticlesInputBoundary = savedArticlesInputBoundary;
    }

    public void execute() { savedArticlesInputBoundary.execute(); }
}
