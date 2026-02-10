package com.napptilus.challenge.infrastructure.http.controller.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String code,
        String message
) {
}
