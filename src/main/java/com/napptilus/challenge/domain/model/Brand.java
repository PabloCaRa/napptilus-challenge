package com.napptilus.challenge.domain.model;

import lombok.Builder;

@Builder
public record Brand(Short brandId, String name) {
}
