/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Client;

import java.io.IOException;
import pa3_g22.Communication.SClient;
import pa3_g22.Communication.Message;

/**
 *
 * @author joaoc
 */
public class Client extends Thread{
    // Communication client.
    private final SClient socketClient;
    // Client ID
    private final long clientId;
    // Client GUI
    private final ClientGUI clientGUI;
    
    
    public Client(long Id, SClient socketClient, ClientGUI clientGUI) {
        this.clientId = Id;
        System.out.println("Client Id: "+clientId);
        this.socketClient = socketClient;
        this.clientGUI = clientGUI;
    }
    
    @Override
    public void run() {
        Message msg;
        try{
            while((msg = socketClient.readObject()) != null)         
               new MSGThread(msg).start();
        }
        catch (Exception e) {System.out.println(e);}
    } 
    
    class MSGThread extends Thread{
        
        // Reply msg
        private final Message msg;
        
        public MSGThread(Message msg){
            this.msg = msg;
        }
        
        @Override
        public void run() {
            System.out.println("REPLY: "+this.msg);
            if(msg.getMsgCode().equals("02")){
                clientGUI.appendReplay(msg.getRequestId(), msg.getNum_iterations(), msg.getDeadline(), msg.getPIValue());
            }else{
                clientGUI.appendReplay(msg.getRequestId(), msg.getNum_iterations(), msg.getDeadline(), "Rejected!");
            }
            
        }
    }
}
