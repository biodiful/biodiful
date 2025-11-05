package org.biodiful;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.biodiful.config.AsyncSyncConfiguration;
import org.biodiful.config.EmbeddedSQL;
import org.biodiful.config.JacksonConfiguration;
import org.biodiful.config.S3TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BiodifulApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, S3TestConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
