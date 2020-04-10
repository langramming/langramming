package com.github.langramming.database;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.BiFunction;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DatabaseContext {
    private final SessionFactory sessionFactory;

    public static DatabaseContext getDatabaseContext() {
        return DatabaseHttpServerProbe.getOrCreateDatabaseContext();
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
                throw ex;
            }
        }
        return result;
    }
}
