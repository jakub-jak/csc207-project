package interface_adapter.saved_articles;

import interface_adapter.ViewModel;

public class SavedArticlesViewModel extends ViewModel<SavedArticlesState> {

    public SavedArticlesViewModel() {
        super("saved articles");
        setState(new SavedArticlesState());
    }
}
