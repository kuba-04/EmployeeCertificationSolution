package org.zalex.infra;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String apiKey;
    private List<String> allowedOrigins;

    public String getApiKey() {
        return apiKey;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    public void setAllowedOrigins(final List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
