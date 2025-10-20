package br.com.erudio.integrationtests.testcontainers;


import java.util.stream.Stream;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.0");

        private static void startContainers() {
            try {
                System.out.println("[Testcontainers] Iniciando container MySQL...");
                Startables.deepStart(Stream.of(mysql)).join();
                System.out.println("[Testcontainers] Container MySQL iniciado: " + mysql.getJdbcUrl());
            } catch (Exception e) {
                System.err.println("[Testcontainers] Erro ao iniciar container MySQL: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }



        @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getSystemProperties().put("spring.datasource.url", mysql.getJdbcUrl());
            environment.getSystemProperties().put("spring.datasource.username", mysql.getUsername());
            environment.getSystemProperties().put("spring.datasource.password", mysql.getPassword());
        }
    }
}
