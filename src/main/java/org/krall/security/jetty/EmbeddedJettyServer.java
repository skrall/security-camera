package org.krall.security.jetty;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.krall.security.commandline.AppOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedJettyServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJettyServer.class);

    public void startEmbeddedServer() {
        try {
            Server server = new Server(8080);
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setCacheControl("max-age=0,public");
            resourceHandler.setResourceBase(AppOptions.getInstance().getOutputDirectory().getAbsolutePath());
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
            server.setHandler(handlers);
            server.start();
            //server.join();
        } catch (Exception e) {
            logger.info("Error while running jetty server.", e);
            throw new RuntimeException(e);
        }
    }

}
