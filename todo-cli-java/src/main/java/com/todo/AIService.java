package com.todo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import java.io.IOException;

/**
 * Service for AI enhancement of task descriptions using DeepSeek API.
 */
public class AIService {
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String DEEPSEEK_MODEL = "deepseek-chat";
    private final OkHttpClient client;
    private final String apiKey;
    
    /**
     * Creates a new AIService instance.
     * 
     * @param apiKey DeepSeek API key (can be null for placeholder mode)
     */
    public AIService(String apiKey) {
        this.client = new OkHttpClient();
        this.apiKey = apiKey;
    }
    
    /**
     * Enhances a task description using AI.
     * If no API key is provided, uses a placeholder implementation.
     */
    public String enhanceTaskDescription(String description) throws IOException {
        System.out.println("\u001B[33mOriginal task\u001B[0m: " + description);
        
        if (apiKey == null || apiKey.isEmpty()) {
            // Placeholder implementation
            String enhanced = enhanceWithPlaceholder(description);
            System.out.println("\u001B[32mAI-enhanced task\u001B[0m: " + enhanced);
            return enhanced;
        } else {
            // Real API implementation
            return enhanceWithDeepSeekAPI(description);
        }
    }
    
    /**
     * Placeholder implementation that simulates AI enhancement.
     */
    private String enhanceWithPlaceholder(String description) {
        String lowerDesc = description.toLowerCase();
        
        if (lowerDesc.contains("learn")) {
            return description + " - Break down into weekly study goals with practice exercises";
        } else if (lowerDesc.contains("buy") || lowerDesc.contains("groceries")) {
            return description + " - Create a shopping list and check pantry inventory first";
        } else if (lowerDesc.contains("write") || lowerDesc.contains("document")) {
            return description + " - Outline key sections first, then write introduction and conclusion";
        } else {
            return description + " - Make it SMART (Specific, Measurable, Achievable, Relevant, Time-bound)";
        }
    }
    
    /**
     * Real implementation using DeepSeek API.
     */
    private String enhanceWithDeepSeekAPI(String description) throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", DEEPSEEK_MODEL);
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a helpful assistant that improves task descriptions to make them more specific, actionable, and clear.");
        
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", "Please improve this task description: '" + description + "'. Make it more specific and actionable.");
        
        JsonObject[] messages = {systemMessage, userMessage};
        requestBody.add("messages", JsonParser.parseString(new Gson().toJson(messages)).getAsJsonArray());
        requestBody.addProperty("max_tokens", 100);
        requestBody.addProperty("temperature", 0.7);
        
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
            .url(DEEPSEEK_API_URL)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                
                String enhancedDescription = jsonResponse
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();
                
                System.out.println("\u001B[32mAI-enhanced task\u001B[0m: " + enhancedDescription);
                return enhancedDescription;
            } else {
                throw new IOException("API request failed: " + response.code() + " " + response.message());
            }
        }
    }
    
    /**
     * Gets the API key from environment variable.
     */
    public static String getApiKeyFromEnv() {
        return System.getenv("DEEPSEEK_API_KEY");
    }
}