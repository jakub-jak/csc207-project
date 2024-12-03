package data_access;

// Standard Java imports
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

// Third-party imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// Ungrouped imports
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import command.ArticleCommand;
import command.ArticleInvoker;
import command.FetchArticleCommand;
import entity.Article;
import entity.CommonArticle;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.digest.DigestNewsDataAccessInterface;

/**
 * The DAO for news data using OkHttp.
 */
public class NewsDataAccessObject implements DigestNewsDataAccessInterface {
    // Constants
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY;
    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        API_KEY = loadApiKey();
    }

    private static String loadApiKey() {
        final Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            properties.load(reader);
            return properties.getProperty("NEWS_API_KEY");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException("Failed to load API key from .env file");
        }
    }

    /**
     * Fetches articles by keyword.
     * @param keyword keyword
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @param page page
     * @param pageSize page size
     * @return list of articles
     * @throws IOException exception
     */
    public List<Article> fetchArticlesByKeyword(String keyword, String fromDate, String toDate,
                                                String language, String sortBy, int page, int pageSize)
            throws IOException {
        final List<Article> articles = new ArrayList<>();
        try {
            final String endpoint = buildEndpointUrl(keyword, fromDate, toDate, language, sortBy, page, pageSize);

            final Request request = new Request.Builder()
                    .url(endpoint)
                    .addHeader("Content-Type", "application/json")
                    .build();

            final String jsonResponse = executeHttpRequest(request);

            final List<JsonObject> articleJsonObjects = parseArticlesFromJson(jsonResponse);

            final List<ArticleCommand> commands = createArticleFetchCommands(articleJsonObjects, keyword);

            articles.addAll(executeArticleFetchCommands(commands));

            return articles;

        }
        catch (IOException | InterruptedException ioException) {
            throw new IOException("Error fetching articles: " + ioException.getMessage(), ioException);
        }
    }

    private String buildEndpointUrl(String keyword, String fromDate, String toDate,
                                    String language, String sortBy, int page, int pageSize) throws IOException {
        final String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());

        final String fromDateParam;
        if (fromDate != null && !fromDate.isEmpty()) {
            fromDateParam = fromDate;
        }
        else {
            fromDateParam = "";
        }

        final String toDateParam;
        if (toDate != null && !toDate.isEmpty()) {
            toDateParam = toDate;
        }
        else {
            toDateParam = "";
        }

        final String languageParam;
        if (language != null && !language.isEmpty()) {
            languageParam = language;
        }
        else {
            languageParam = "en";
        }

        final String sortByParam;
        if (sortBy != null && !sortBy.isEmpty()) {
            sortByParam = sortBy;
        }
        else {
            sortByParam = "relevancy";
        }

        return String.format(
                "%severything?q=%s&from=%s&to=%s&language=%s&sortBy=%s&page=%d&pageSize=%d&apiKey=%s",
                BASE_URL, encodedKeyword, fromDateParam, toDateParam, languageParam, sortByParam,
                page, pageSize, API_KEY);
    }

    private String executeHttpRequest(Request request) throws IOException {
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
            else {
                final String errorBody;
                if (response.body() != null) {
                    errorBody = response.body().string();
                }
                else {
                    errorBody = "No response body";
                }
                throw new IOException("Error: HTTP response code " + response.code() + "\n" + errorBody);
            }
        }
    }

    private List<JsonObject> parseArticlesFromJson(String jsonResponse) {
        final Gson gson = new Gson();
        final JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        final JsonArray articlesArray = jsonObject.getAsJsonArray("articles");

        final List<JsonObject> articleJsonObjects = new ArrayList<>();
        for (JsonElement articleElement : articlesArray) {
            articleJsonObjects.add(articleElement.getAsJsonObject());
        }
        return articleJsonObjects;
    }

    private List<ArticleCommand> createArticleFetchCommands(List<JsonObject> articleJsonObjects, String keyword) {
        final List<ArticleCommand> commands = new ArrayList<>();

        for (JsonObject articleObject : articleJsonObjects) {
            final String title = getJsonString(articleObject, "title");
            final String author = getJsonString(articleObject, "author");
            final String link = getJsonString(articleObject, "url");
            final String date = getJsonString(articleObject, "publishedAt");
            final String description = "";

            commands.add(new FetchArticleCommand(this, title, author, link, date, description, keyword));
        }
        return commands;
    }

    /**
     * Retrieves article content.
     * @param title title
     * @param author author
     * @param link link
     * @param date date
     * @param description description
     * @param keyword keyword
     * @return article
     */
    public Article fetchArticleContent(String title,
                                       String author,
                                       String link,
                                       String date,
                                       String description,
                                       String keyword) {
        Article result = null;

        if (isUrlReachable(link)) {
            final String htmlContent = fetchHtmlContent(link);
            if (htmlContent != null) {
                final String content = extractMainContent(Jsoup.parse(htmlContent, link));
                if (content != null && !content.trim().isEmpty()) {
                    final String category = keyword;
                    result = new CommonArticle(title, author, category, content, link, date, description);
                }
                else {
                    System.err.println("Skipping article due to empty content for URL: " + link);
                }
            }
        }
        return result;
    }

    private boolean isUrlReachable(String url) {
        boolean isReachable = false;

        final Request headRequest = new Request.Builder()
                .url(url)
                .head()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        try (Response headResponse = CLIENT.newCall(headRequest).execute()) {
            isReachable = headResponse.isSuccessful();
        }
        catch (IOException ioException) {
            System.err.println("Error checking URL: " + url);
        }
        return isReachable;
    }

    private String fetchHtmlContent(String url) {
        String htmlContent = null;

        final Request articleRequest = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        final int timeToWait = 10;

        final OkHttpClient clientWithTimeout = CLIENT.newBuilder()
                .connectTimeout(timeToWait, TimeUnit.SECONDS)
                .readTimeout(timeToWait, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();

        try (Response articleResponse = clientWithTimeout.newCall(articleRequest).execute()) {
            if (articleResponse.isSuccessful() && articleResponse.body() != null) {
                htmlContent = articleResponse.body().string();
            }
            else {
                System.err.println("Failed to fetch article content for URL: " + url);
            }
        }
        catch (IOException ioException) {
            System.err.println("Error fetching content for URL: " + url);
        }
        return htmlContent;
    }

    private String getJsonString(JsonObject jsonObject, String memberName) {
        String result = "";
        if (jsonObject.has(memberName) && !jsonObject.get(memberName).isJsonNull()) {
            result = jsonObject.get(memberName).getAsString();
        }
        return result;
    }

    private List<Article> executeArticleFetchCommands(List<ArticleCommand> commands) throws InterruptedException {
        final ArticleInvoker invoker = new ArticleInvoker(10);
        return invoker.executeCommands(commands);
    }

    private String extractMainContent(Document doc) {
        // Remove script and style elements
        doc.select("script, style, noscript").remove();

        // Attempt to select common content containers
        Elements articleElements = doc.select("article");
        if (articleElements.isEmpty()) {
            // Fallback to selecting main content area
            articleElements = doc.select("main");
        }
        if (articleElements.isEmpty()) {
            // Fallback to selecting content based on common CSS classes
            articleElements = doc.select("[class*=content], [class*=article], [id*=content], [id*=article]");
        }

        String textContent = articleElements.text();

        // If still empty, fallback to body text
        if (textContent.isEmpty()) {
            textContent = doc.body().text();
        }

        return textContent;
    }

    /**
     * Fetch first article.
     * @param keyword keyword
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @return article
     * @throws IOException exception
     */
    public Article fetchFirstArticle(String keyword, String fromDate, String toDate, String language, String sortBy)
            throws IOException {
        int page = 1;
        final int pageSize = 5;
        // Define a maximum number of pages to prevent infinite loops
        final int maxPages = 5;

        while (page <= maxPages) {
            final List<Article> articles = fetchArticlesByKeyword(keyword, fromDate, toDate, language, sortBy, page,
                    pageSize);
            if (!articles.isEmpty()) {
                return articles.get(0);
            }
            else {
                page++;
            }
        }

        // If no articles are found after checking the maximum number of pages
        throw new IOException("No articles found for the given criteria.");
    }

    /**
     * Fetch first multiple articles.
     * @param keywords keywords
     * @param fromDate from date
     * @param toDate to date
     * @param language language
     * @param sortBy sort by
     * @return list of articles
     */
    public List<Article> fetchFirstMultiple(String[] keywords,
                                            String fromDate,
                                            String toDate,
                                            String language,
                                            String sortBy) {
        final List<Article> articles = new ArrayList<>();

        for (String keyword : keywords) {
            try {
                final Article article = fetchFirstArticle(keyword, fromDate, toDate, language, sortBy);
                articles.add(article);
            }
            catch (IOException ioException) {
                System.err.println("No articles found for keyword: " + keyword);
            }
        }

        return articles;
    }
}
