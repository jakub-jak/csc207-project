package data_access;

import entity.Article;
import com.google.gson.*;
import entity.CommonArticle;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import use_case.digest.DigestNewsDataAccessInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
    public List<Article> fetchArticlesByKeyword(String keyword, String fromDate, String toDate, String language, String sortBy, int page, int pageSize) throws IOException {
        final List<Article> articles = new ArrayList<>();
        try {
            // URL encode the keyword and build the endpoint URL
            final String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
            final String endpoint = String.format("%severything?q=%s&from=%s&to=%s&language=%s&sortBy=%s&page=%d&pageSize=%d&apiKey=%s",
                    BASE_URL, encodedKeyword,
                    fromDate != null ? fromDate : "",
                    toDate != null ? toDate : "",
                    language != null ? language : "en",
                    sortBy != null ? sortBy : "relevancy",
                    page, pageSize, API_KEY);

            // Create a request object
            final Request request = new Request.Builder()
                    .url(endpoint)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute the request and get the response
            try (Response response = CLIENT.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    final String jsonResponse = response.body().string();

                    // Parse the JSON response
                    final Gson gson = new Gson();
                    final JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                    final JsonArray articlesArray = jsonObject.getAsJsonArray("articles");

                    for (JsonElement articleElement : articlesArray) {
                        final JsonObject articleObject = articleElement.getAsJsonObject();

                        final String title = articleObject.has("title") && !articleObject.get("title").isJsonNull()
                                ? articleObject.get("title").getAsString() : "";
                        final String author = articleObject.has("author") && !articleObject.get("author").isJsonNull()
                                ? articleObject.get("author").getAsString() : "";
                        final String link = articleObject.has("url") && !articleObject.get("url").isJsonNull()
                                ? articleObject.get("url").getAsString() : "";
                        final String date = articleObject.has("publishedAt") && !articleObject.get("publishedAt").isJsonNull()
                                ? articleObject.get("publishedAt").getAsString() : "";
                        final String description = "";

                        // Fetch the article content from the URL
                        String content = "";
                        boolean isContentFetched = false;
                        try {
                            // Fetch the HTML content of the article URL
                            Request articleRequest = new Request.Builder()
                                    .url(link)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .build();

                            try (Response articleResponse = CLIENT.newCall(articleRequest).execute()) {
                                if (articleResponse.isSuccessful() && articleResponse.body() != null) {
                                    final String htmlContent = articleResponse.body().string();

                                    // Parse the HTML content using jsoup
                                    final Document doc = Jsoup.parse(htmlContent, link);

                                    // Attempt to extract the main content
                                    content = extractMainContent(doc);
                                    isContentFetched = true;

                                }
                                else {
                                    // Handle error
                                    System.err.println("Failed to fetch article content for URL: " + link);
                                }
                            }
                        }
                        catch (Exception exception) {
                            System.err.println("Error fetching article content for URL: " + link);
                            exception.printStackTrace();
                        }

                        // Only add the article if content was successfully fetched
                        if (isContentFetched && content != null && !content.trim().isEmpty()) {
                            // Category is not available in the JSON, so set to an empty string
                            final String category = keyword;

                            final Article article = new CommonArticle(title, author, category, content, link, date, description);
                            articles.add(article);
                        }
                        else {
                            System.err.println("Skipping article due to empty content for URL: " + link);
                        }
                    }
                    return articles;
                }
                else {
                    // Handle error
                    final String errorBody = response.body() != null ? response.body().string() : "No response body";
                    throw new IOException("Error: HTTP response code " + response.code() + "\n" + errorBody);
                }

            }
        }
        catch (Exception exception) {
            throw new IOException("Error fetching articles: " + exception.getMessage(), exception);
        }
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
        final int pageSize = 10;
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
    public List<Article> fetchFirstMultiple(String[] keywords, String fromDate, String toDate, String language, String sortBy) {
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
