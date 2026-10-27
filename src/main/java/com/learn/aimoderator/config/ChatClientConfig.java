package com.learn.aimoderator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    /**
     * Spring AI auto-configures a ChatClient.Builder wired to whichever
     * model starter is on the classpath (Ollama here). Swapping to OpenAI,
     * Azure, Bedrock, etc. is a pom.xml + application.yml change only -
     * none of the business code below needs to change. That indirection
     * is the whole point of coding against Spring AI's ChatClient API.
     */

    @Bean
    ChatClient chatClient(ChatClient.Builder builder){
        return builder.defaultSystem("""
                You are strict but fair content moderation assistant for a
                general-purpose social platform. You will be given a single piece of 
                user-generated text. Classify it accurately according to the request schema
                Do not add commentary outside the schema. Be conservative: only mark content as severe when it 
                clearly warrants it, and prefer FLAG_FOR_REVIEW over AUTO_REMOVE when unsure.
                
                Every field in the schema is mandatory and must be a concrete,
                        non-null value - never omit a field or use null/None:
                        - severity is ALWAYS a whole number from 1 to 5, even for safe
                          content (use 1 for harmless content, never null).
                        - blocked is ALWAYS a literal true or false, never null.
                        - reasoning and suggestedAction are ALWAYS non-empty string.
                """).build();
    }
}
