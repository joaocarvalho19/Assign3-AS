/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Communication;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaoc
 */
public class SClient implements Serializable{
    
    private String host;
    
    private int port;
    
    private Socket socket;
    
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    

    public SClient(String hostName, int portNumb) {
        this.host = hostName;
        this.port = portNumb;
    }
    
    public SClient(Socket socket) {
        this.socket = socket;
        //createSocket();
    }
    

    public boolean createSocket() {
        try {
            //socket = new Socket(this.host, this.port);
            out = new ObjectOutputStream (this.socket.getOutputStream ());
            in = new ObjectInputStream (this.socket.getInputStream ());

            return true;
        }
        catch(Exception e) {
            System.err.println(e);
            return false;
        }
    }
    
    public void end(){
        try {
            this.socket.close();
            this.out.close();
            this.in.close();

        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    

    public Message readObject() throws IOException, ClassNotFoundException {
        Message obj = null;
        try {
            obj = (Message)in.readObject();
        }
        catch(SocketException e) {
            System.err.println(e+" LB failed");
            socket.close();
            /*try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {}
            System.out.println(" Lets try again");
            
            socket = new Socket("localhost", 1000);
            createSocket();*/
        }
        
        return obj;
    }
    

    public void writeObject(Message obj){
        try {
            out.reset();
            out.writeObject(obj);
        }
        catch(SocketException e) {
            System.err.println(e);
            //System.exit(1);
        }catch(IOException e) {
            System.err.println(e);
            //System.exit(1);
        }
    }
}
