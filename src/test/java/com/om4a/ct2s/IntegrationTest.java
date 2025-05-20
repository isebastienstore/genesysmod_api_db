package com.om4a.ct2s;

import com.om4a.ct2s.config.AsyncSyncConfiguration;
import com.om4a.ct2s.config.EmbeddedElasticsearch;
import com.om4a.ct2s.config.EmbeddedMongo;
import com.om4a.ct2s.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GenesysmodApiDbApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedMongo
@EmbeddedElasticsearch
public @interface IntegrationTest {
}
