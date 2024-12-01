package app;

import javax.swing.*;

/**
 * The Main Program Application.
 */
public class MainApplication {
    /**
     * Builds the Main Program when this file is run.
     * @param args args
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addSignupView()
                .addLoginView()
                .addLoggedInView()
                .addSavedArticlesView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addAddCategoryUseCase()
                .addRemoveCategoryUseCase()
                .addDigestUseCase()
                .addNewsUseCase()
                .addSaveArticlesUseCase()
                .addUnsaveArticlesUseCase()
                .addShareArticleUseCase()
                .addSavedArticlesUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}
