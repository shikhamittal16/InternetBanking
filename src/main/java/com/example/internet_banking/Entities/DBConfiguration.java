package com.example.internet_banking.Entities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DBConfiguration {
    private String driver = "org.postgresql.Driver";
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;
    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String showSql;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hbm2ddl_auto;
// The packages to scan to find your JPA annotated objects. These are the objects that the session factory needs to manage,
// will generally be POJO's and annotated with @ Entity.
    private String packageToScan = "com.example.internet_banking.Entities";

// The data source is basically the database that Hibernate is going to use to persist your objects. Generally one transaction manager
// will have one data source. If you want Hibernate to talk to multiple data sources then you have multiple transaction managers .
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource =new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

// This is the thing that Hibernate uses to create and manage your transactions, and actually talks to the database.
    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setPackagesToScan(packageToScan);
        Properties properties = new Properties();
        properties.put("hibernate.dialect",dialect);
        properties.put("hibernate.show_sql",showSql);
        properties.put("hibernate.hbm2ddl.auto",hbm2ddl_auto);
        localSessionFactoryBean.setHibernateProperties(properties);
        return localSessionFactoryBean;
    }

//    @Bean
//    public HibernateTransactionManager transactionManager(){
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//        transactionManager.setSessionFactory(sessionFactory().getObject());
//        return transactionManager;
//    }
}
