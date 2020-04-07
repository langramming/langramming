package com.github.langramming.httpserver;

import com.github.langramming.util.StreamUtil;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

import java.io.InputStream;
import java.io.OutputStream;

public class StaticAssetsHandler extends CLStaticHttpHandler {

    /**
     * @inheritDoc
     */
    public StaticAssetsHandler() {
        super(StaticAssetsHandler.class.getClassLoader(), "/assets/");
    }

    @Override
    protected void onMissingResource(Request request, Response response) throws Exception {
        try (InputStream inputStream = StaticAssetsHandler.class.getResourceAsStream("/assets/index.html")) {
            if (inputStream != null) {
                response.setStatus(HttpStatus.OK_200);
                response.setContentType("text/html");

                try (OutputStream outputStream = response.getOutputStream()) {
                    StreamUtil.copy(inputStream, outputStream);
                }
            } else {
                response.setStatus(HttpStatus.NOT_FOUND_404);
            }
        }
    }

}
