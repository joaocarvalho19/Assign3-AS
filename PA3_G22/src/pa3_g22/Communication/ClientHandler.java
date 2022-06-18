
package pa3_g22.Communication;

import java.net.*;
import java.io.*;


public class ClientHandler extends Thread {
    private final Socket clientSocket;
    
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
        
        @Override
        public void run()
        {
            
            try (// get the outputstream of client
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 // get the inputstream of client
                 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ){

                Object obj;
                while ((obj = in.readObject()) != null) {
  
                    // writing the received message from
                    // client
                    String msg = obj.toString();
                    
                    // if msg -> New REQ | New SERVER | HEATBEAT | 
                    
                    System.out.printf(
                        " Sent from the client: %s\n",
                        obj);
                    
                    
                    //out.writeObject(obj);
                }
            }
            catch (EOFException e) {
                System.out.println(e.toString());
            }
            catch (ClassNotFoundException e) {
                System.out.println(e.toString());
            }
            catch (IOException e) {
                System.out.println(e.toString());
            }
            finally {
                try {
                    clientSocket.close();
                }
                catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
        
}