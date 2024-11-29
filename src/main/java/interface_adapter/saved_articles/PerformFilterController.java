package interface_adapter.saved_articles;

import use_case.perform_filter.PerformFilterInputBoundary;
import use_case.perform_filter.PerformFilterInputData;

/**
 * The controller for the PerformFilter Use Case.
 */
public class PerformFilterController {
    private final PerformFilterInputBoundary performFilterInputBoundary;
    private final SavedArticlesViewModel savedArticlesViewModel;

    public PerformFilterController(PerformFilterInputBoundary performFilterInputBoundary,
                                   SavedArticlesViewModel savedArticlesViewModel) {
        this.performFilterInputBoundary = performFilterInputBoundary;
        this.savedArticlesViewModel = savedArticlesViewModel;
    }

    /**
     * Executes the PerformFilter Use Case.
     */
    public void execute() {
        SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        PerformFilterInputData performFilterInputData =
                new PerformFilterInputData(savedArticlesState.getCategoriesFilterList());
        performFilterInputBoundary.execute(performFilterInputData);
    }
}
