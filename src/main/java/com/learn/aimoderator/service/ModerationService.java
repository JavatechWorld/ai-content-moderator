package com.learn.aimoderator.service;

import com.learn.aimoderator.model.ModerationRequest;
import com.learn.aimoderator.model.ModerationResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// Exercise 3: needs the spring-boot-starter-cache dependency (see pom.xml) and
// @EnableCaching on AiContentModeratorApplication.
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationService {


    private final ChatClient chatClient;

    private static final String USER_TEMPLATE = """
            Classify the following user-generated content.
            
            CONTENT:
            \"\"\"
            {text}
            \"\"\"
            """;

    @CircuitBreaker(name = "moderationService", fallbackMethod = "moderateFallback")
    @Retry(name = "moderationService")
    @TimeLimiter(name = "moderationService")
    public CompletableFuture<ModerationResult> moderate(ModerationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Calling AI model to moderate contentId={}", request.contentId());

            return chatClient.prompt()
                    .user(u -> u.text(USER_TEMPLATE).param("text", request.text()))
                    .call()
                    .entity(ModerationResult.class); // structured output -> parsed straight into the record
        });
    }

    private CompletableFuture<ModerationResult> moderateFallback(ModerationRequest request, Throwable t) {
        log.warn("Moderation fallback triggered for contentId={}: {}", request.contentId(), t.toString());
        return CompletableFuture.completedFuture(ModerationResult.failClosedFallback(t.getClass().getSimpleName()));
    }

}
