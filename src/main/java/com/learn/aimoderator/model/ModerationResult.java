package com.learn.aimoderator.model;

/**
 * Structured output shape we ask the LLM to fill in.
 * Spring AI's BeanOutputConverter builds a JSON-schema instruction from this
 * record and parses the model's JSON response straight back into it.
 */
public record ModerationResult(
        ModerationCategory category,
        int severity,          // 1 (harmless) - 5 (severe)
        boolean blocked,        // true if content should be blocked/hidden
        String reasoning,       // short justification, useful for human review / audit
        String suggestedAction  // e.g. "ALLOW", "FLAG_FOR_REVIEW", "AUTO_REMOVE"
) {

    public static ModerationResult failClosedFallback(String reason) {
        return new ModerationResult(
                ModerationCategory.OTHER_POLICY_VIOLATION,
                5,
                true,
                "Moderation service unavailable, blocked by default (fail-closed): " + reason,
                "AUTO_REMOVE"
        );
    }
}
