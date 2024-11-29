package interface_adapter.saved_articles;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.news.NewsOutputBoundary;

public class SavedArticlesPresenter implements NewsOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private LoggedInViewModel loggedInViewModel;

    public SavedArticlesPresenter(ViewManagerModel viewManagerModel,
                                  LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    /**
     * Prepares the success view for the News Case.
     */
    @Override
    public void prepareSuccessView() {
        // Update to the logged in view
        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    /**
     * Prepares the failure view for the News Use Case.
     *
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {

    }
}
