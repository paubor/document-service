package org.yetanotherorg.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestError {
    // Could also include a logged UUID, which makes it easier for ops.
    @JsonProperty("errorMessage")
    private final String message;

    public RestError(final String message) {
        this.message = message;
    }
}
