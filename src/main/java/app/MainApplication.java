package app;

import javax.swing.*;

public class MainApplication {
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
                .addShareArticleUseCase()
                .addSavedArticlesUseCase()
                .build();

        application.pack();
        application.setVisible(true);
    }
}
