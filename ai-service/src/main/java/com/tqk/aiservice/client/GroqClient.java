package com.tqk.aiservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroqClient {

    @Value("${groq.api.key}")
    private String apiKey;

    private final OkHttpClient http = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generate(String model, String prompt) throws Exception {
        String json = """
        {
          "model": "%s",
          "messages": [
            {"role": "user", "content": %s }
          ],
          "temperature": 0
        }
        """.formatted(model, mapper.writeValueAsString(prompt));

        Request request = new Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        Response response = http.newCall(request).execute();
        String body = response.body().string();

        JsonNode root = mapper.readTree(body);

        return root.get("choices").get(0).get("message").get("content").asText();
    }
}
