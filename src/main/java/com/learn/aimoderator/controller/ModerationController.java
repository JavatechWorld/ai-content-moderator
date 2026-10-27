package com.learn.aimoderator.controller;

import com.learn.aimoderator.model.ModerationRequest;
import com.learn.aimoderator.model.ModerationResult;
import com.learn.aimoderator.service.ModerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/moderate")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @PostMapping
    public CompletableFuture<ResponseEntity<ModerationResult>> moderate(@Valid @RequestBody ModerationRequest request) {
        return moderationService.moderate(request).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/batch")
    public CompletableFuture<ResponseEntity<List<ModerationResult>>> moderateBatch(
            @Valid @RequestBody List<ModerationRequest> requests) {
        List<CompletableFuture<ModerationResult>> futures = requests.stream()
                .map(moderationService::moderate)
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
                .thenApply(ResponseEntity::ok);
    }
}
