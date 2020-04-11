package com.github.langramming.database;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.http.server.*;
import org.hibernate.SessionFactory;

public class DatabaseHttpServerProbe extends HttpServerProbe.Adapter {
    static final ThreadLocal<DatabaseContext> localDatabaseContext = new ThreadLocal<>();

    static DatabaseContext getOrCreateDatabaseContext() {
        DatabaseContext ctx = localDatabaseContext.get();
        if (ctx == null) {
            throw new IllegalStateException("Database context was expected but was not available for the current thread.");
        }
        return ctx;
    }

    /* begin filter implementation */

    private final SessionFactory sessionFactory;

    public DatabaseHttpServerProbe(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void onBeforeServiceEvent(HttpServerFilter filter, Connection connection, Request request, HttpHandler httpHandler) {
        if (localDatabaseContext.get() == null) {
            DatabaseContext databaseContext = new DatabaseContext(sessionFactory);
            localDatabaseContext.set(databaseContext);
        } else {
            throw new IllegalStateException("Thread already has database context!");
        }

        request.addAfterServiceListener(r -> {
            localDatabaseContext.remove();
        });
    }
}
