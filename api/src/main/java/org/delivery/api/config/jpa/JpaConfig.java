package org.delivery.api.config.jpa;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "org.delivery.db") //엔티티 어노테이션이 붙은 애들을 다 스캔하겠다.
@EnableJpaRepositories(basePackages = "org.delivery.db") //레파지토리도 가져가겠다.
public class JpaConfig {
}
