package com.learn.aimoderator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModerationRequest(

        @NotBlank(message = "text must not be blank")
        @Size(max = 4000, message = "text must be at most 4000 characters")
        String text,

        String contentId // optional caller-supplied id, e.g. comment id, message id
) {
}
