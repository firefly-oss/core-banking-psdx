package com.firefly.core.banking.psdx.models.config;

import com.firefly.core.banking.psdx.interfaces.security.EncryptionService;
import com.firefly.core.banking.psdx.models.converters.DecryptedStringConverter;
import com.firefly.core.banking.psdx.models.converters.EncryptedStringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for R2DBC converters.
 */
@Configuration
@RequiredArgsConstructor
public class R2dbcConverterConfig {

    private final EncryptionService encryptionService;

    /**
     * Create a bean for R2DBC custom conversions.
     *
     * @return The R2DBC custom conversions
     */
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(new EncryptedStringConverter(encryptionService));
        converters.add(new DecryptedStringConverter(encryptionService));
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }
}
