package com.github.langramming.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.inject.Singleton;
import java.util.function.BiFunction;

@Singleton
public class LangrammingDatabase {

    private SessionFactory sessionFactory;

    public void start() {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate.cfg.xml");

        Reflections databaseEntitiesFinder = new Reflections(
                new ConfigurationBuilder()
                        .forPackages("com.github.langramming.database.model")
                        .addScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        );

        databaseEntitiesFinder
                .getTypesAnnotatedWith(javax.persistence.Entity.class)
                .forEach(configuration::addAnnotatedClass);

        this.sessionFactory = configuration.buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public <T> T runInTransaction(BiFunction<Session, Transaction, T> func) {
        T result;
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                result = func.apply(session, transaction);
                transaction.commit();
            } catch (Exception ex) {
                transaction.rollback();
                ex.printStackTrace();
                throw ex;
            }
        }
        return result;
    }
}
