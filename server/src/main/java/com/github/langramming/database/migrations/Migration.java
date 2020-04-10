package com.github.langramming.database.migrations;

import org.hibernate.Session;

public interface Migration {
    void run(Session session);
}
