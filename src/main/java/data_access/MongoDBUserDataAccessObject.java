package data_access;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import entity.Article;
import entity.CommonArticle;
import entity.CommonUser;
import entity.User;
import org.bson.conversions.Bson;
import use_case.add_category.AddCategoryDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.remove_category.RemoveCategoryDataAccessInterface;
import use_case.save_article.SaveArticleDataAccessInterface;
import use_case.saved_articles.SavedArticlesDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import use_case.unsave_article.UnsaveArticleDataAccessInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MongoDBUserDataAccessObject implements AddCategoryDataAccessInterface,
                                                    LoginUserDataAccessInterface,
                                                    LogoutUserDataAccessInterface,
                                                    RemoveCategoryDataAccessInterface,
                                                    SignupUserDataAccessInterface,
                                                    SaveArticleDataAccessInterface,
                                                    UnsaveArticleDataAccessInterface,
                                                    SavedArticlesDataAccessInterface {
    private String currentUsername;
    private final MongoCollection<Document> userCollection;
    private static String connectionString;

    static {
        connectionString = loadApiKey();
    }

    private static String loadApiKey() {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            properties.load(reader);
            return properties.getProperty("MONGO_API_KEY");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load API key from .env file");
        }
    }

    public MongoDBUserDataAccessObject() {
        currentUsername = "";

        // Connection String to connect to database

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("Project");
        database.runCommand(new Document("ping", 1));
        System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        userCollection = database.getCollection("User");
    }

    public static void main(String[] args) {
        // Testing Code
        MongoDBUserDataAccessObject db = new MongoDBUserDataAccessObject();

        User tempUser = new CommonUser("tempName",
                "tempPass",
                new ArrayList<>(),
                new HashMap<>());

        Article tempArticle = new CommonArticle("tempArticle",
                "tempAuthor",
                "tech",
                "tempContent",
                "tempLink",
                "tempDate",
                "tempDescription");

        db.save(tempUser);
        db.setCurrentUsername(tempUser.getName());
        db.saveCategory("tech");
        db.saveCategory("tech2");
        System.out.println(db.getUserCategories());
        db.saveArticle(tempArticle);
        db.removeCategory("tech");
        System.out.println(db.getUserCategories());
        db.removeArticle(tempArticle);

    }

    /**
     * Saves the category to the user's categories. Will not add duplicate categories.
     *
     * @param category the category to save
     */
    @Override
    public void saveCategory(String category) {
        Bson query = Filters.eq("name", currentUsername);
        Bson update = Updates.addToSet("categories", category);
        userCollection.updateOne(query, update);
    }

    /**
     * Removes the category to the user's categories.
     *
     * @param category the category to save
     */
    @Override
    public void removeCategory(String category) {
        Bson query = Filters.eq("name", currentUsername);
        Bson update = Updates.pull("categories", category);
        userCollection.updateOne(query, update);
    }

    /**
     * Saves the article to the user's articles.
     *
     * @param article the article to save
     */
    @Override
    public void saveArticle(Article article) {
        Document articleDoc = new Document()
                .append("title", article.getTitle())
                .append("author", article.getAuthor())
                .append("category", article.getCategory())
                .append("content", article.getContent())
                .append("link", article.getLink())
                .append("date", article.getDate())
                .append("description", article.getDescription());

        Bson query = Filters.eq("name", currentUsername);
        Bson update = Updates.addToSet("articles." + article.getCategory(), articleDoc);
        userCollection.updateOne(query, update);
    }

    /**
     * Returns the map of articles for the current user.
     *
     * @return the given user's articles.
     */
    @Override
    public Map<String, List<Article>> getUserArticles() {
        Document userDoc = userCollection.find(Filters.eq("name", currentUsername)).first();
        if (userDoc == null) { return null; }

        Map<String,List<Article>> articles = new HashMap<>();

        Map<String,List<Document>> articlesDoc = userDoc.get("articles", Map.class);

        for (String category: articlesDoc.keySet()){
            List<Article> currArticles = new ArrayList<>();
            for (Document articleDoc: articlesDoc.get(category)) {
                currArticles.add(new CommonArticle(articleDoc.get("title", String.class),
                        articleDoc.get("author", String.class),
                        articleDoc.get("category", String.class),
                        articleDoc.get("content", String.class),
                        articleDoc.get("link", String.class),
                        articleDoc.get("date", String.class),
                        articleDoc.get("description", String.class)));
            }
            articles.put(category, currArticles);
        }
        return articles;
    }

    /**
     * Remove the article from the user's articles.
     *
     * @param article the article to save
     */
    @Override
    public void removeArticle(Article article) {
        Bson query = Filters.eq("name", currentUsername);
        Bson update = Updates.pull("articles." + article.getCategory(), new Document("title", article.getTitle()));
        userCollection.updateOne(query, update);
    }

    /**
     * Gets the given user's category list.
     *
     * @return the given user's category list.
     */
    @Override
    public List<String> getUserCategories() {
        Bson query = Filters.eq("name", currentUsername);
        Document result = userCollection.find(query).first();

        if (result == null) { return new ArrayList<>(); }

        return result.getList("categories", String.class);
    }

    /**
     * Checks if the given username exists.
     *
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    @Override
    public boolean existsByName(String username) {
        Document existsDoc = userCollection.find(Filters.eq("name", username)).first();
        return existsDoc != null;
    }

    /**
     * Saves the user.
     *
     * @param user the user to save
     */
    @Override
    public void save(User user) {
        Document existsDoc = userCollection.find(Filters.eq("name", user.getName())).first();
        if (existsDoc != null) { return; }

        Document userDocument = new Document()
                .append("name", user.getName())
                .append("password", user.getPassword())
                .append("categories", user.getCategories())
                .append("articles", user.getArticles());

        userCollection.insertOne(userDocument);
    }

    /**
     * Returns the user with the given username.
     *
     * @return the user with the given username
     */
    @Override
    public User get(String username) {
        Document userDoc = userCollection.find(Filters.eq("name", username)).first();
        if (userDoc == null) { return null; }

        Map<String,List<Article>> articles = new HashMap<>();

        Map<String,List<Document>> articlesDoc = userDoc.get("articles", Map.class);

        for (String category: articlesDoc.keySet()){
            List<Article> currArticles = new ArrayList<>();
            for (Document articleDoc: articlesDoc.get(category)) {
                currArticles.add(new CommonArticle(articleDoc.get("title", String.class),
                        articleDoc.get("author", String.class),
                        articleDoc.get("category", String.class),
                        articleDoc.get("content", String.class),
                        articleDoc.get("link", String.class),
                        articleDoc.get("date", String.class),
                        articleDoc.get("description", String.class)));
            }
            articles.put(category, currArticles);
        }

        return new CommonUser(userDoc.get("name", String.class),
                userDoc.get("password", String.class),
                userDoc.getList("categories", String.class),
                articles);
    }

    /**
     * Returns the username of the curren user of the application.
     *
     * @return the username of the current user
     */
    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Sets the username indicating who is the current user of the application.
     *
     * @param username the new current username
     */
    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
