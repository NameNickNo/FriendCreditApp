package com.friend.app.config;

import com.friend.app.service.TelegramBotService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@ComponentScan("com.friend.app")
@Configuration
@EnableWebMvc
@EnableTransactionManagement
public class SpringConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Autowired
    public SpringConfig(Environment environment) {
        this.environment = environment;
    }

    @SneakyThrows
    @Bean
    @Autowired
    public TelegramBotsApi telegramBotsApi(TelegramBotService telegramBotService) {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBotService);
        return botsApi;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(getProperty("spring.datasource.url"));
        dataSource.setUsername(getProperty("spring.datasource.username"));
        dataSource.setPassword(getProperty("spring.datasource.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("spring.jpa.properties.hibernate.dialect", getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("spring.jpa.properties.hibernate.show_sql", getProperty("spring.jpa.properties.hibernate.show_sql"));
        return properties;
    }

    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.friend.app");
        factoryBean.setHibernateProperties(hibernateProperties());

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new HibernateTransactionManager(Objects.requireNonNull(sessionFactory().getObject()));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    private String getProperty(String propertyName) {
        return Objects.requireNonNull(environment.getProperty(propertyName));
    }
}
