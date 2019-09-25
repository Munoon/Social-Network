package com.train4game.social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.train4game.social.**.repository"})
@ComponentScan(basePackages = {"com.train4game.social.**.repository"})
@PropertySource({"classpath:db/postgres.properties"})
public class DbConfig {

    @Autowired
    private Environment env;

    @Bean
    @Description("Data Source")
    public DataSource dataSource() {
        final var dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getRequiredProperty("database.url"));
        dataSource.setDriverClassName(env.getRequiredProperty("database.driverClassName"));
        dataSource.setUsername(env.getRequiredProperty("database.username"));
        dataSource.setPassword(env.getRequiredProperty("database.password"));
        return dataSource;
    }

    @PostConstruct
    protected void initializeDb() {
        final var populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/initDB.sql"));
        populator.addScript(new ClassPathResource("db/populateDB.sql"));
        populator.setContinueOnError(true);
        DatabasePopulatorUtils.execute(populator, dataSource());
    }

    @Bean
    @Description("Entity Manager Factory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final var jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);

        Properties properties = new Properties();
        properties.setProperty(FORMAT_SQL, env.getRequiredProperty("hibernate.format_sql"));
        properties.setProperty(USE_SQL_COMMENTS, env.getRequiredProperty("hibernate.use_sql_comments"));
        properties.setProperty(GENERATE_STATISTICS, env.getRequiredProperty("hibernate.generate_statistics"));


        final var entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource());
        entityManagerFactory.setPackagesToScan("com.train4game.social.**.model");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setJpaProperties(properties);

        return entityManagerFactory;
    }

    @Bean
    @Description("Transaction Manager")
    public PlatformTransactionManager transactionManager() {
        final var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
