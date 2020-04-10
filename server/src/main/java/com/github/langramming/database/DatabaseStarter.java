package com.github.langramming.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

public class DatabaseStarter {
    private DatabaseStarter() {}

    public static SessionFactory initializeDatabase() {
        // create session factory
        Configuration configuration = configureHibernate();
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }

    private static Configuration configureHibernate() {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate.cfg.xml");

        Reflections databaseEntitiesFinder = new Reflections(
                new ConfigurationBuilder()
                        .forPackages("com.langramming.database.model")
                        .addScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        );

        databaseEntitiesFinder
                .getTypesAnnotatedWith(javax.persistence.Entity.class)
                .forEach(configuration::addAnnotatedClass);

        return configuration;
    }
}
