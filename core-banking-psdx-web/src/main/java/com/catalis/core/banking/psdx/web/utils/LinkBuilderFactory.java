package com.catalis.core.banking.psdx.web.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory for creating LinkBuilders.
 */
@Component
@RequiredArgsConstructor
public class LinkBuilderFactory {

    private final String baseUrl;

    /**
     * Create a new LinkBuilder.
     *
     * @return A new LinkBuilder
     */
    public LinkBuilder create() {
        return LinkBuilder.fromBaseUrl(baseUrl);
    }
}
