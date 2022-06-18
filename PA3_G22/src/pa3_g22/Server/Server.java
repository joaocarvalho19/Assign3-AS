/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Server;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import pa3_g22.Communication.SClient;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import pa3_g22.Communication.Message;

/**
 *
 * @author joaoc
 */
public class Server extends Thread{
    
    // PI decimal numbers
    private static final String decimal_numbers = "1415926589793";
    
    // Communication client. LB
    private final SClient socketLB;
    // Communication client. Monitor
    private final SClient socketMonitor;
    // ServerId ID
    private final long serverId;
    // Client GUI
    private final ServerGUI serverGUI;
    // FIFO
    private final MFIFO fifo;
    private final ReentrantLock rl;
    
    // Number of simultaneous active threads
    private final int run_Threads_limit = 3;
    
    // Maximum number of current iterations
    private final int max_curr_iterations = 20;
    
    // Sum number of current iterations  - Max 20
    private int current_num_iterations;
    
    // List of requests in queue.
    private final List<Message> reqQueue;
    
    
    public Server(long Id, SClient socketLB, SClient socketMonitor, ServerGUI serverGUI) {
        this.serverId = Id;
        System.out.println("Server Id: "+serverId);
        this.socketLB = socketLB;
        this.socketMonitor = socketMonitor;
        this.serverGUI = serverGUI;
        reqQueue = new ArrayList<>();
        this.fifo = new MFIFO(3);
        rl = new ReentrantLock(true);
        
        // Threads to process the requests
        for (int i = 1; i <= run_Threads_limit; i++) 
            new MSGThread(i).start();
        
        // Thread to send heartBeats to Monitor
        new HeartBeatThread().start();
        current_num_iterations = 0;
    }
    
    @Override
    public void run() {
        Message msg;
        try{
            while((msg = this.socketLB.readObject()) != null){
                current_num_iterations += msg.getNum_iterations();
                if(!queueIsFull() && current_num_iterations <= max_curr_iterations){
                    socketMonitor.writeObject(new Message("SERVER_REQ_IN", serverId , current_num_iterations));
                    addRequestToQueue(msg);
                    serverGUI.appendRequest(msg.getRequestId(), msg.getNum_iterations(), msg.getDeadline(), msg.getClientId(), "Waiting...");
                }
                else{
                    current_num_iterations -= msg.getNum_iterations();
                    Message reply = new Message("REPLY",msg.getClientId(), serverId, msg.getRequestId(), "03", msg.getNum_iterations(), msg.getDeadline());
                    this.socketLB.writeObject(reply);
                    serverGUI.appendRequest(msg.getRequestId(), msg.getNum_iterations(), msg.getDeadline(), msg.getClientId(), "Rejected!");
                }
            }      
               
        }
        catch (Exception e) {System.out.println(e);}
    } 
    
    public void addRequestToQueue(Message request){
        try {
            rl.lock();
                if(reqQueue.isEmpty()){
                    reqQueue.add(0,request);
                }else{
                    if(reqQueue.get(0).getDeadline() < request.getDeadline())
                        reqQueue.add(1,request);
                    else
                        reqQueue.add(0,request);
                }
            
        } finally {
            rl.unlock();
        }
        //fifo.out();
    }
    
    // Check if requests Queue is full 
    public boolean queueIsFull(){
        return reqQueue.size() >= 2;
    }
    /*
        Get the next request (with smallest deadline )
    */
    public Message getNextRequest(){
        Message msg;
        try {
            rl.lock();
            if(!reqQueue.isEmpty()){
                msg = reqQueue.remove(0);
            }
            else{
                msg = null;
            }
            
        } finally {
            rl.unlock();
        }
        return msg;
    }
    
    
    class MSGThread extends Thread{
        
        // Reply msg
        private final int id;
        
        public MSGThread(int id){
            this.id = id;
        }
        
        @Override
        public void run() {
            Message msg;
            while(true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                msg = getNextRequest();
                if(msg != null){
                    System.out.println(id+" ID: "+msg.getDeadline());
                    Message reply = processRequest(msg);
                    socketLB.writeObject(reply);
                    current_num_iterations -= msg.getNum_iterations();
                    socketMonitor.writeObject(new Message("SERVER_REQ_OUT", serverId , current_num_iterations));
                }
            }
        }
        
        public Message processRequest(Message msg){
            String pi_value = "3,";
            for (int i = 0; i < msg.getNum_iterations(); i++) {
                pi_value = pi_value + decimal_numbers.charAt(i);
                serverGUI.setRequestState(msg.getRequestId(), String.valueOf(i+1));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {}
                
            }
            
            serverGUI.setRequestState(msg.getRequestId(), "DONE");                
            Message reply = new Message("REPLY",msg.getClientId(), serverId, msg.getRequestId(), "02", msg.getNum_iterations(), pi_value, msg.getDeadline());
            return reply;
        }
    }
    /*
        Class to Send HeartBeats to Monitor
    */
    class HeartBeatThread extends Thread{
        
        // Reply msg
        private final int id;
        
        public HeartBeatThread(){
            this.id = 0;
        }
        @Override
        public void run() {
            Message heart_msg = new Message("HEARTBEAT", serverId, true);
            while(true) {
                try {
                    Thread.sleep(2000);
                    socketMonitor.writeObject(heart_msg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
