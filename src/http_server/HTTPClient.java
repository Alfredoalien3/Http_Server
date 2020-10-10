/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class HTTPClient {
    
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String ACCEPT_TEXT = "Accept: text/html, application/xhtml+xml, application/xml";
    private static final String ACCEPT_IMG = "Accept: image/*";
    
    private static final Logger LOG = Logger.getLogger(HTTPClient.class.getName());
    
    
    public static void main(String[] args) {
        
        URL url = null;
        
        if( args.length == 1 ) {            
            try {
                url = new URL( args[0] );
            } catch (MalformedURLException ex) {
                LOG.log(Level.SEVERE, null, ex);
                System.exit(-1);
            }            
        }
        
        System.out.println( url );
        System.out.println( url.getFile() );
        System.out.println( url.getPath() );
        
    }
    
}
