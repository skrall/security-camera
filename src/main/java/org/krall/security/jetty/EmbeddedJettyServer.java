package org.krall.security.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.krall.security.commandline.AppOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedJettyServer {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJettyServer.class);

    public void startEmbeddedServer() {
        try {
            final Server server = new Server(8080);
            ServletContextHandler handler = new ServletContextHandler();
            handler.setResourceBase(AppOptions.getInstance().getOutputDirectory().getAbsolutePath());

            SessionManager sm = new HashSessionManager();
            SessionHandler sh = new SessionHandler(sm);
            handler.setSessionHandler(sh);

            DefaultServlet defaultServlet = new DefaultServlet();
            ServletHolder holder = new ServletHolder(defaultServlet);
            holder.setInitParameter("useFileMappedBuffer", "false");
            holder.setInitParameter("maxCachedFiles", "0");
            holder.setInitParameter("maxCacheSize", "0");
            handler.addServlet(holder, "/");

            server.setHandler(handler);
            server.start();
            //server.join();
        } catch (Exception e) {
            logger.info("Error while running jetty server.", e);
            throw new RuntimeException(e);
        }
    }

}
