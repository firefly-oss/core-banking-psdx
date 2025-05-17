package com.catalis.core.banking.psdx.web.utils;

import com.catalis.core.banking.psdx.interfaces.dtos.PSDLinksDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the LinkBuilderFactory.
 */
class LinkBuilderFactoryTest {

    private static final String BASE_URL = "https://api.bank.com/v1";

    @Test
    void create_shouldCreateLinkBuilder() {
        // Given
        LinkBuilderFactory factory = new LinkBuilderFactory(BASE_URL);

        // When
        LinkBuilder linkBuilder = factory.create();
        PSDLinksDTO links = linkBuilder.withSelf("/accounts/12345").build();

        // Then
        assertEquals(BASE_URL + "/accounts/12345", links.getSelf().getHref());
    }
}
