package com.github.langramming;

import com.github.langramming.client.telegram.TelegramStarter;
import com.github.langramming.database.LangrammingDatabase;
import com.github.langramming.httpserver.LangrammingServer;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class LangrammingMain {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();

        injector.getInstance(LangrammingDatabase.class).start();
        injector.getInstance(TelegramStarter.class).start();
        injector.getInstance(LangrammingServer.class).start();
    }
}

