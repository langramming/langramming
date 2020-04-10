package com.github.langramming.database;

import com.github.langramming.database.migrations.Migration;
import com.github.langramming.database.model.CompletedMigration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseStarter {
    private DatabaseStarter() {}

    public static SessionFactory initializeDatabase() {
        // create session factory
        Configuration configuration = configureHibernate();
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        // find all migrations
        runMigrations(sessionFactory);

        return sessionFactory;
    }

    private static Configuration configureHibernate() {
        Configuration configuration = new Configuration();
        configuration.configure("/hibernate.cfg.xml");

        Reflections databaseEntitiesFinder = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(Migration.class.getPackageName())
                        .addScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
        );

        databaseEntitiesFinder
                .getTypesAnnotatedWith(javax.persistence.Entity.class)
                .forEach(configuration::addAnnotatedClass);

        return configuration;
    }

    private static void runMigrations(SessionFactory sessionFactory) {
        List<Migration> pendingMigrations = findPendingMigrations(sessionFactory);

        // run all migrations
        System.out.format("Running %d pending database migrations...\n", pendingMigrations.size());
        try (Session session = sessionFactory.openSession()) {
            for (Migration migration : pendingMigrations) {
                Transaction transaction = session.beginTransaction();
                try {
                    migration.run(session);
                    transaction.commit();
                } catch (Exception ex) {
                    transaction.rollback();
                    throw ex; // to ensure no pending migrations are run
                }
            }
        }
        System.out.println("Done running migrations!");
    }

    private static List<Migration> findPendingMigrations(SessionFactory sessionFactory) {
        List<Migration> allMigrations = findAllMigrations();

        Set<String> completedMigrations = findCompletedMigrations(sessionFactory).stream()
                .map(CompletedMigration::getMigrationClassName)
                .collect(Collectors.toSet());

        return allMigrations.stream()
                .filter(migration -> !completedMigrations.contains(migration.getClass().getName()))
                .collect(Collectors.toList());
    }

    private static List<Migration> findAllMigrations() {
        Reflections migrationsFinder = new Reflections(
                new ConfigurationBuilder()
                        .forPackages(Migration.class.getPackageName())
                        .addScanners(new SubTypesScanner())
        );

        Set<Class<? extends Migration>> migrationClasses =
                migrationsFinder.getSubTypesOf(Migration.class);

        return migrationClasses.stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .map(clazz -> {
                    try {
                        Constructor<? extends Migration> cons = clazz.getConstructor();
                        cons.setAccessible(true);
                        return cons.newInstance();
                    } catch (ReflectiveOperationException ex) {
                        throw new RuntimeException("Failed to instantiate " + clazz.getName(), ex);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<CompletedMigration> findCompletedMigrations(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<CompletedMigration> query = session.getCriteriaBuilder()
                    .createQuery(CompletedMigration.class);
            query = query.select(query.from(CompletedMigration.class));

            List<CompletedMigration> migrationEntities = session.createQuery(query).getResultList();

            System.out.println(migrationEntities);

            return migrationEntities;
        }
    }
}
