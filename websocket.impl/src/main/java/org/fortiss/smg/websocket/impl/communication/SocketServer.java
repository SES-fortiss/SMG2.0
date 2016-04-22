package org.fortiss.smg.websocket.impl.communication;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;


/**
 * It is responsible for handling client connections
 *
 */
public class SocketServer extends Server {
    private boolean _verbose;

    WebSocket _websocket;
    private SelectChannelConnector _connector;
    /**
     * looks for WebSocket handshake requests and handles them by calling the
     * doWebSocketConnect method, which we have extended to create a WebSocket
     * depending on the sub protocol passed:
     * 
     */
    private WebSocketHandler _wsHandler;
    /**
     * is responsible for serving the static content like HTML and Javascript.
     */
    private ResourceHandler _rHandler;

    /**
     * 
     */
    private ConcurrentLinkedQueue<EchoBroadcastWebSocket> _broadcast = new ConcurrentLinkedQueue<EchoBroadcastWebSocket>();

    /**
     * Constructor for creating server instance.
     * It 
     * @param port
     */
    public SocketServer(int port) {
        _connector = new SelectChannelConnector();
        _connector.setPort(port);

        addConnector(_connector);
        _wsHandler = new APIWebSocketHandler(_broadcast);


        setHandler(_wsHandler);

        _rHandler = new ResourceHandler();
        _rHandler.setDirectoriesListed(true);
        _rHandler.setResourceBase("frontapi");
        _wsHandler.setHandler(_rHandler);
    }

   

    /* ------------------------------------------------------------ */
    public boolean isVerbose() {
        return _verbose;
    }

    public ConcurrentLinkedQueue<EchoBroadcastWebSocket> get_broadcast() {
        return _broadcast;
    }

    /* ------------------------------------------------------------ */
    public void setVerbose(boolean verbose) {
        _verbose = verbose;
    }

    /* ------------------------------------------------------------ */
    public void setResourceBase(String dir) {
        _rHandler.setResourceBase(dir);
    }

    /* ------------------------------------------------------------ */
    public String getResourceBase() {
        return _rHandler.getResourceBase();
    }

    private static void usage() {
        System.err.println("java -cp CLASSPATH " + SocketServer.class
                + " [ OPTIONS ]");
        System.err.println("  -p|--port PORT    (default 7070)");
        System.err.println("  -v|--verbose ");
        System.err.println("  -d|--docroot file (default 'src/test/webapp')");
        System.exit(1);
    }



    public boolean isRunning() {
        // TODO Auto-generated method stub
        return this.isRunning();
    }





    public void destroy() {
        this.destroy();
        
    }

    // public static void main(String... args) {
    // try {
    // int port = 7070;
    // boolean verbose = false;
    // String docroot = "frontendapi";
    //
    // for (int i = 0; i < args.length; i++) {
    // String a = args[i];
    // if ("-p".equals(a) || "--port".equals(a))
    // port = Integer.parseInt(args[++i]);
    // else if ("-v".equals(a) || "--verbose".equals(a))
    // verbose = true;
    // else if ("-d".equals(a) || "--docroot".equals(a))
    // docroot = args[++i];
    // else if (a.startsWith("-"))
    // usage();
    // }
    //
    // SocketServer server = new SocketServer(port);
    // server.setVerbose(verbose);
    // server.setResourceBase(docroot);
    // server.start();
    // // server.join();
    // } catch (Exception e) {
    // Log.warn(e);
    // }
    // }

}