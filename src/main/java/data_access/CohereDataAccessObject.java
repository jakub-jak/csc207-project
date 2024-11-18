package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The DAO for interacting with the Cohere API.
 */
public class CohereDataAccessObject {
    // Constants
    private static final String BASE_URL = "https://api.cohere.ai/v1/";
    private static final String API_KEY;
    private static final OkHttpClient client = new OkHttpClient();

    static {
        API_KEY = loadApiKey();
    }

    private static String loadApiKey() {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            properties.load(reader);
            return properties.getProperty("COHERE_API_KEY");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load API key from .env file");
        }
    }

    /**
     * Summarizes the given input text using Cohere's summarization API.
     *
     * @param inputText The text to summarize.
     * @return The summarized text.
     * @throws IOException If an I/O error occurs.
     */
    public String summarize(String inputText) throws IOException {
        // Build the endpoint URL
        String endpoint = BASE_URL + "summarize";

        // Create JSON payload with additional parameters
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("text", inputText);
        jsonBody.addProperty("length", "short"); // Specify single-sentence summary

        // Optionally, you can set other parameters like 'temperature', 'model', etc.
        // jsonBody.addProperty("temperature", 0.5);

        // Build the request
        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();

                // Parse the JSON response
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                String summary = jsonObject.get("summary").getAsString();

                return summary;
            } else {
                // Handle error
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                throw new IOException("Error: HTTP response code " + response.code() + "\n" + errorBody);
            }
        }
    }
}
