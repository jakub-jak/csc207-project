package command;

import data_access.NewsDataAccessObject;
import entity.Article;

public class FetchArticleCommand implements ArticleCommand {
    private final NewsDataAccessObject dao;
    private final String title;
    private final String author;
    private final String link;
    private final String date;
    private final String description;
    private final String keyword;

    public FetchArticleCommand(NewsDataAccessObject dao, String title, String author, String link,
                               String date, String description, String keyword) {
        this.dao = dao;
        this.title = title;
        this.author = author;
        this.link = link;
        this.date = date;
        this.description = description;
        this.keyword = keyword;
    }

    @Override
    public Article execute() {
        return dao.fetchArticleContent(title, author, link, date, description, keyword);
    }
}
