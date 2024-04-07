package org.zalex.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalex.adapter.outbound.db.CertificateRepository;

import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    CertificateService certificateService(CertificateRepository repository) {
        return new CertificateService(repository);
    }

    @Bean
    IdGenerator idGenerator() {
        return UUID::randomUUID;
    }

}
