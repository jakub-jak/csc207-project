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
    private static final OkHttpClient client = new OkHttpClient();

    static {
        API_KEY = loadApiKey();
    }

    private static String loadApiKey() {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            properties.load(reader);
            return properties.getProperty("NEWS_API_KEY");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load API key from .env file");
        }
    }

    public List<Article> fetchArticlesByKeyword(String keyword, String fromDate, String toDate, String language, String sortBy, int page, int pageSize) throws IOException {
        List<Article> articles = new ArrayList<>();
        try {
            // URL encode the keyword and build the endpoint URL
            String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
            String endpoint = String.format("%severything?q=%s&from=%s&to=%s&language=%s&sortBy=%s&page=%d&pageSize=%d&apiKey=%s",
                    BASE_URL, encodedKeyword,
                    fromDate != null ? fromDate : "",
                    toDate != null ? toDate : "",
                    language != null ? language : "en",
                    sortBy != null ? sortBy : "relevancy",
                    page, pageSize, API_KEY);

            // Create a request object
            Request request = new Request.Builder()
                    .url(endpoint)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute the request and get the response
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();

                    // Parse the JSON response
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                    JsonArray articlesArray = jsonObject.getAsJsonArray("articles");

                    for (JsonElement articleElement : articlesArray) {
                        JsonObject articleObject = articleElement.getAsJsonObject();

                        String title = articleObject.has("title") && !articleObject.get("title").isJsonNull()
                                ? articleObject.get("title").getAsString() : "";
                        String author = articleObject.has("author") && !articleObject.get("author").isJsonNull()
                                ? articleObject.get("author").getAsString() : "";
                        String link = articleObject.has("url") && !articleObject.get("url").isJsonNull()
                                ? articleObject.get("url").getAsString() : "";
                        String date = articleObject.has("publishedAt") && !articleObject.get("publishedAt").isJsonNull()
                                ? articleObject.get("publishedAt").getAsString() : "";
                        String description = articleObject.has("description") && !articleObject.get("description").isJsonNull()
                                ? articleObject.get("description").getAsString() : "";

                        // Fetch the article content from the URL
                        String content = "";
                        try {
                            // Fetch the HTML content of the article URL
                            Request articleRequest = new Request.Builder()
                                    .url(link)
                                    .addHeader("User-Agent", "Mozilla/5.0")
                                    .build();

                            try (Response articleResponse = client.newCall(articleRequest).execute()) {
                                if (articleResponse.isSuccessful() && articleResponse.body() != null) {
                                    String htmlContent = articleResponse.body().string();

                                    // Parse the HTML content using jsoup
                                    Document doc = Jsoup.parse(htmlContent, link);

                                    // Attempt to extract the main content
                                    content = extractMainContent(doc);

                                } else {
                                    // Handle error
                                    System.err.println("Failed to fetch article content for URL: " + link);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error fetching article content for URL: " + link);
                            e.printStackTrace();
                        }

                        // Category is not available in the JSON, so set to an empty string
                        String category = "";

                        Article article = new CommonArticle(title, author, category, content, link, date);
                        articles.add(article);
                    }
                    return articles;
                } else {
                    // Handle error
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    throw new IOException("Error: HTTP response code " + response.code() + "\n" + errorBody);
                }

            }
        } catch (Exception e) {
            throw new IOException("Error fetching articles: " + e.getMessage(), e);
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
}
