package entity;

public class CommonArticle implements Article {
    private final String title;
    private final String author;
    private final String category;
    private final String content;
    private final String link;
    private final String date;

    public CommonArticle(String title, String author, String category, String content, String link, String date) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.content = content;
        this.link = link;
        this.date = date;
    }


    @Override
    public String getTitle() { return title; }

    @Override
    public String getAuthor() { return author; }

    @Override
    public String getCategory() { return category; }

    @Override
    public String getContent() { return content; }

    @Override
    public String getLink() { return link; }

    @Override
    public String getDate() { return date; }

    @Override
    public String getDescription() { return ""; }
}
