package interface_adapter.saved_articles;

import interface_adapter.ViewModel;

/**
 * Saved Articles View Model.
 */
public class SavedArticlesViewModel extends ViewModel<SavedArticlesState> {

    public SavedArticlesViewModel() {
        super("saved articles");
        setState(new SavedArticlesState());
    }
}
