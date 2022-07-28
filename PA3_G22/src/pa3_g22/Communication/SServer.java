/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Communication;

import java.io.*;
import java.net.*;

/**
 *
 * @author joaoc
 */
public class SServer  extends Thread{
    
    private ServerSocket listeningSocket;

    private final int serverPort;


    public SServer(int portNumb) {
      this.serverPort= portNumb;
   }

    
    public void open() {
        try {
            this.listeningSocket = new ServerSocket(this.serverPort);
            //this.listeningSocket.setSoTimeout(10*1000);
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    
        
    /*public void end() {
       try {
           this.listeningSocket.close();
       }
       catch(Exception e) {
           System.err.println(e);
       }
    }*/

    public Socket accept() throws SocketTimeoutException {
       Socket s = null;

       try {
           s = this.listeningSocket.accept();
           System.out.println("Accepted");
           
           //this._in = new ObjectInputStream(s.getInputStream());
           //this._out = new ObjectOutputStream(s.getOutputStream());
           
       }
       catch(Exception e) {
           System.err.println(e);
       }

       return s;
    }
    
    @Override
    public void run() {
        try { 
            while (true) {
	        new ClientHandler(this.listeningSocket.accept()).start();
	    }
	} catch (IOException e) {}
    }
}