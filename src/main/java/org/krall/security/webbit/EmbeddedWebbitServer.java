package org.krall.security.webbit;

import org.krall.security.commandline.AppOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;

import java.util.concurrent.ExecutionException;

public class EmbeddedWebbitServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedWebbitServer.class);

    public void startEmbeddedServer() {
        try {
            StaticFileHandler handler = new StaticFileHandler(AppOptions.getInstance().getOutputDirectory().getAbsolutePath());
            handler.enableDirectoryListing(true);
            WebServers.createWebServer(8080)
                    .add(handler)
                    .start()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error in embedded web server.", e);
        }
    }
}
