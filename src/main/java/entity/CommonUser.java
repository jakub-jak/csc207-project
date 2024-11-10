package entity;

import java.util.List;
import java.util.Map;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    private final String name;
    private final String password;
    private List<String> categories;
    private Map<String,List<Article>> articles;

    public CommonUser(String name, String password, List<String> categories, Map<String,List<Article>> articles) {
        this.name = name;
        this.password = password;
        this.categories = categories;
        this.articles = articles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<String> getCategories() { return categories; }

    @Override
    public Map<String, List<Article>> getArticles() { return articles; }

    @Override
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public void setArticles(Map<String,List<Article>> articles) {
        this.articles = articles;
    }

    @Override
    public void addArticle(Article article) {
        articles.get(article.getCategory()).add(article);
    }

    @Override
    public void deleteArticle(Article article) {
        articles.get(article.getCategory()).remove(article);
    }
}
