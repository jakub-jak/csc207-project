package data_access;

// Standard Java imports
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Third-party imports
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.digest.DigestCohereDataAccessInterface;

/**
 * The DAO for interacting with the Cohere API.
 */
public class CohereDataAccessObject implements DigestCohereDataAccessInterface {
    // Constants
    private static final String BASE_URL = "https://api.cohere.ai/v1/";
    private static final String API_KEY;
    private static final OkHttpClient CLIENT = new OkHttpClient();

    static {
        API_KEY = loadApiKey();
    }

    private static String loadApiKey() {
        final Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            properties.load(reader);
            return properties.getProperty("COHERE_API_KEY");
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
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
        final String endpoint = BASE_URL + "summarize";

        // Create JSON payload with additional parameters
        final JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("text", inputText);
        jsonBody.addProperty("length", "short");

        // Optionally, you can set other parameters like 'temperature', 'model', etc.
        // jsonBody.addProperty("temperature", 0.5);

        // Build the request
        final RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json")
        );

        final Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and get the response
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                final String jsonResponse = response.body().string();

                // Parse the JSON response
                final Gson gson = new Gson();
                final JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
                final String summary = jsonObject.get("summary").getAsString();

                return summary;
            }
            else {
                // Handle error
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
}
