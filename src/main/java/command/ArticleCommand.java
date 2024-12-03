package command;

import entity.Article;

/**
 * Article command design pattern.
 */
public interface ArticleCommand {
    /**
     * The abstract execute method.
     * @return Article article
     */
    Article execute();
}
