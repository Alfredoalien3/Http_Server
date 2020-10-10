/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class Http_server {
    
    public static final int HTTP_PORT = 8080;
    
    private static final Logger LOG = Logger.getLogger(Http_server.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        //http://localhost:8080
        try {
            ServerSocket serverSocket
                    = new ServerSocket( HTTP_PORT  );
            
            // 
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOG.info("CONNECT");
                
                Thread serviceProcess = new Thread(new HTTPService(clientSocket));
                
                serviceProcess.start();
            }

        } catch (IOException e) {
            LOG.warning("Exception caught when trying to listen on port "
                    + HTTP_PORT + " or listening for a connection");
            LOG.warning(e.getMessage());
        }
    }
    
}
