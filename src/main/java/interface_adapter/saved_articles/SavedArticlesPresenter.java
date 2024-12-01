package interface_adapter.saved_articles;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.add_category.AddCategoryOutputBoundary;
import use_case.add_category.AddCategoryOutputData;
import use_case.news.NewsOutputBoundary;
import use_case.remove_category.RemoveCategoryOutputBoundary;
import use_case.remove_category.RemoveCategoryOutputData;
import use_case.save_article.SaveArticleOutputBoundary;
import use_case.save_article.SaveArticleOutputData;
import use_case.unsave_article.UnsaveArticleOutputBoundary;
import use_case.unsave_article.UnsaveArticleOutputData;

public class SavedArticlesPresenter implements  NewsOutputBoundary,
                                                AddCategoryOutputBoundary,
                                                RemoveCategoryOutputBoundary,
                                                UnsaveArticleOutputBoundary,
                                                SaveArticleOutputBoundary {
    private ViewManagerModel viewManagerModel;
    private LoggedInViewModel loggedInViewModel;
    private SavedArticlesViewModel savedArticlesViewModel;

    public SavedArticlesPresenter(ViewManagerModel viewManagerModel,
                                  LoggedInViewModel loggedInViewModel,
                                  SavedArticlesViewModel savedArticlesViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.savedArticlesViewModel = savedArticlesViewModel;
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
     * Prepares the success view for the AddCategory Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareSuccessView(AddCategoryOutputData outputData) {
        final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        savedArticlesState.addCategory(outputData.getCategory());
        loggedInViewModel.firePropertyChanged("add category: " + outputData.getCategory());
    }

    /**
     * Prepares the success view for the RemoveCategory Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareSuccessView(RemoveCategoryOutputData outputData) {
        final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        savedArticlesState.removeCategory(outputData.getCategory());
        loggedInViewModel.firePropertyChanged("remove category: " + outputData.getCategory());
    }

    /**
     * Prepares the success view for the SaveArticle Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareSuccessView(SaveArticleOutputData outputData) {

    }

    /**
     * Prepares the success view for the UnsaveArticle Use Case.
     *
     * @param outputData the output data
     */
    @Override
    public void prepareSuccessView(UnsaveArticleOutputData outputData) {
        final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        savedArticlesState.removeArticle(outputData.getArticle());
        savedArticlesViewModel.firePropertyChanged("articles");
    }

    /**
     * Prepares the failure view for Use Case.
     *
     * @param errorMessage the explanation of the failure
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final SavedArticlesState savedArticlesState = savedArticlesViewModel.getState();
        savedArticlesState.setSavedArticlesError(errorMessage);
        savedArticlesViewModel.firePropertyChanged("save articles error: " + errorMessage);
    }
}
