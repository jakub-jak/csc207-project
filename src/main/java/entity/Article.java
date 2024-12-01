package entity;

/**
 * The representation of an article in our program.
 */
public interface Article {
    /**
     * Returns the title of the article.
     * @return the title of the article.
     */
    String getTitle();

    /**
     * Returns the author of the article.
     * @return the author of the article.
     */
    String getAuthor();

    /**
     * Returns the category of the article.
     * @return the category of the article.
     */
    String getCategory();

    /**
     * Returns the content of the article.
     * @return the content of the article.
     */
    String getContent();

    /**
     * Returns the link of the article.
     * @return the link of the article.
     */
    String getLink();

    /**
     * Returns the date of the article.
     * @return the date of the article.
     */
    String getDate();

    /**
     * Returns the description of the article.
     * @return the description of the article.
     */
    String getDescription();

    /**
     * Sets the description of the article.
     * @param description the description to set.
     */
    void setDescription(String description);
}
