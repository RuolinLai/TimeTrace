package TimeTrace;

import java.time.Duration;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class AiService {
    private static final String API_KEY = "";
    private static final String BASE_URL = "https://api.groq.com/openai/v1"; 

    public interface Assistant {
        String chat(String userMessage);
    }

    private static Assistant assistant;
    private static ChatMemory chatMemory;

    public static void resetChat(String taskDataJsonOrSummary) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .modelName("openai/gpt-oss-120b")
                .timeout(Duration.ofSeconds(30))
                .build();

        chatMemory = MessageWindowChatMemory.withMaxMessages(15);

        if (taskDataJsonOrSummary != null && !taskDataJsonOrSummary.isBlank()) {
            String systemPrompt = "You are a friendly and concise time management and focus analysis assistant. " +
                "You have access to the user's task data for today:\n" + taskDataJsonOrSummary + "\n\n" +
                "INSTRUCTIONS FOR YOUR RESPONSES:\n" +
                "1. Do NOT dump, list, or print all the raw task data in your answers. Keep the data internal.\n" +
                "2. Provide natural, conversational, and human-friendly summaries or insights.\n" +
                "3. Only mention specific data points (such as exact times, durations, or distraction reasons) when they are strictly necessary to support your point or answer the user's specific question.\n" +
                "4. Keep your answers clear, concise, and easy to read. Do not use Markdown, bold text, or headings.";
            
            chatMemory.add(SystemMessage.from(systemPrompt));
        }

        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .build();
    }

    public static String getAiReading(String question) {
        if (question == null || question.isBlank()){
            return "No blank input allowed";
        }
        try {
            String rawResponse = assistant.chat(question);
            

            String cleanResponse = rawResponse
                .replaceAll("[#*`_]", "")
                .replaceAll("(?m)^\\*\\s+", "");

            return cleanResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "error: plz check your internet connection or API key.";
        }
    }
}