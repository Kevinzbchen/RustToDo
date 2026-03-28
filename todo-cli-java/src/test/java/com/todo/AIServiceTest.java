package com.todo;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
class AIServiceTest {
    @Test
    void testEnhanceWithPlaceholder() throws IOException {
        // Test with empty API key (should use placeholder)
        AIService aiService = new AIService("");
        String description = "Learn Java programming";
        String enhanced = aiService.enhanceTaskDescription(description);
        
        assertNotNull(enhanced);
        assertTrue(enhanced.contains(description));
        assertTrue(enhanced.contains("weekly study goals") || enhanced.contains("practice exercises"));
    }
    
    @Test
    void testPlaceholderEnhancementPatterns() throws IOException {
        // Test different patterns in placeholder implementation
        AIService service = new AIService(null);
        
        // Learning task
        String learnResult = service.enhanceTaskDescription("Learn something");
        assertTrue(learnResult.contains("weekly study goals") || learnResult.contains("practice exercises"));
        
        // Shopping task
        String buyResult = service.enhanceTaskDescription("Buy groceries");
        assertTrue(buyResult.contains("shopping list") || buyResult.contains("pantry inventory"));
        
        // Writing task
        String writeResult = service.enhanceTaskDescription("Write documentation");
        assertTrue(writeResult.contains("key sections") || writeResult.contains("introduction and conclusion"));
        
        // Default task
        String defaultResult = service.enhanceTaskDescription("Random task");
        assertTrue(defaultResult.contains("SMART") || defaultResult.contains("Specific, Measurable"));
    }
    
    @Test
    void testGetApiKeyFromEnv() {
        // This test just verifies the method doesn't throw exceptions
        String apiKey = AIService.getApiKeyFromEnv();
        // Could be null if env var is not set, that's OK
        assertTrue(apiKey == null || !apiKey.isEmpty());
    }
    
    @Test
    void testConstructorWithApiKey() {
        // Test that constructor accepts API key
        AIService serviceWithKey = new AIService("test-api-key");
        assertNotNull(serviceWithKey);
        
        AIService serviceWithoutKey = new AIService(null);
        assertNotNull(serviceWithoutKey);
        
        AIService serviceWithEmptyKey = new AIService("");
        assertNotNull(serviceWithEmptyKey);
    }
}