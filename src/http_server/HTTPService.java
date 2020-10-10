/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class HTTPService implements Runnable {

    private static final Logger LOG = Logger.getLogger(HTTPService.class.getName());
    private Socket clientSocket;
    private BufferedReader in;
    private PrintStream out;

    public HTTPService(Socket c) {
        clientSocket = c;
        try{
            out = new PrintStream(clientSocket.getOutputStream(),true);
        }catch(IOException ex){
            Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
        }
        try{
            in=new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        }catch(IOException ex){
            Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    @Override
    public void run() {
        
        try {
           String requestLine;
           String commandLine = null;
            // leer la solicitud del cliente
            while ((requestLine = in.readLine()) != null) {   
                if(requestLine.startsWith("GET")){
                    LOG.info(requestLine);
                    commandLine = requestLine;
                }
                System.out.println(requestLine);
                
                //Si recibimos linea en blanco, es fin del la solicitud
                if( requestLine.isEmpty() ) {
                    break;
                }
            }
            String fileName = null;
            if(commandLine == null){
                notImplemented();
            }else{
                // GET / HTTP/1.1
                // GET / uno.html/
              String tokens[] = commandLine.split("\\s+");
              fileName = tokens[1].substring(tokens[1].lastIndexOf('/')+1);
              if(fileName.length()==0){
                 fileName = "Trabajo.html"; 
              }
            }
            Path fb = Paths.get(fileName);
            File filePointer = fb.toFile();
            if(!filePointer.exists()){
                notFound();
            }else if (fileName.endsWith(".ico") || fileName.endsWith(".png") 
                    || fileName.endsWith(".jpg")|| fileName.endsWith(".gif") ){
               LOG.info("IMAGE");
               sendIMGFile(filePointer);
            }else{
                LOG.info("TEXT");
                sendHTMLFile(filePointer);
            }
            clientSocket.close();
             
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
        } 
    }
    
    private String getExtension(String f){
        int p = f.lastIndexOf('.');
        return f.substring(p + 1);
    }
    
    private void notImplemented() {
     File f = new File("501.html");
     out.println("HTTP/1.1 501 Not Implemented");
     out.println("Content-Type: text/html; charset=utf-8");
     out.println("Content-Length: " + f.length());
     out.println();
     FileReader file = null;
     try{
         file = new FileReader(f);
         int data;
         while((data = file.read()) != -1){
           out.write(data);
           out.flush();
         }
         file.close();
     }catch(FileNotFoundException ex){
         Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
     }catch(IOException ex){
         Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex );
     }
    }

    private void notFound() {
     File f = new File("404.html");
     out.println("HTTP/1.1 404 Not Implemented");
     out.println("Content-Type: text/html; charset=utf-8");
     out.println("Content-Length: " + f.length());
     out.println();
     FileReader file = null;
     try{
         file = new FileReader(f);
         int data;
         while((data = file.read()) != -1){
           out.write(data);
           out.flush();
         }
         file.close();
     }catch(FileNotFoundException ex){
         Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
     }catch(IOException ex){
         Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex );
     }   
    }

    private void sendIMGFile(File filePointer) {
        System.out.println(filePointer.getName());
      out.println("HTTP/1.1 200 OK");
      out.println(lastModified(filePointer));
      String content = getExtension(filePointer.getName());
      
      if(content.equals("ico")){
          out.println("Content-Type: image/ico");
      }
      if(content.equals("png")){
          out.println("Content-Type: image/png");
      }
       if(content.equals("jpg")){
          out.println("Content-Type: image/jpeg");
      }
      if(content.equals("gif")){
          out.println("Content-Type: image/gif");
      }
      out.println("Content-Length: " + filePointer.length());
      out.println();
      out.flush();
      LOG.log(Level.INFO,"Contemt-Lenght: {0}" , filePointer.length());
      
      FileInputStream file;
      try{
          file = new FileInputStream(filePointer);
          int data;
          while((data = file.read()) != -1){
              out.write(data);
          }
          out.flush();
          file.close();
         
      }catch(FileNotFoundException ex){
          Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
         
      }catch(IOException ex){
          Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
      }
    }

    private void sendHTMLFile(File filePointer)  {
     out.println("HTTP/1.1 200 OK");
     out.println(lastModified(filePointer));
     
     out.println("Content-Type: text/html; charset=utf-8");
     out.println("Content-Length: " + filePointer.length());
     out.println();
     FileReader file;
     try{
         file = new FileReader(filePointer);
         int data;
         while((data = file.read()) != -1){
             out.write(data);

         }
         out.flush();
         file.close();
          
      }catch(FileNotFoundException ex){
          Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
         
      }catch(IOException ex){
          Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE,null,ex);
      }
     }
    

    private String lastModified(File f) {
    long d = f.lastModified(); 
    Date lastModified = new Date(d);
    return "Last-Modified" + lastModified.toString();
    }

}