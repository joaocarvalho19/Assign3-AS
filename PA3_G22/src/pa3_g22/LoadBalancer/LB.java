/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.LoadBalancer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import pa3_g22.Communication.SServer;
import pa3_g22.Communication.SClient;
import pa3_g22.Communication.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import pa3_g22.Server.Server;

/**
 *
 * @author joaoc
 */
public class LB extends Thread{
    // Communication server.
    private SServer server;
    // LB ID
    private final long lbId;
    // Client GUI
    private final LBGUI lbGUI;
    
    // SERVERS Map     
    private final HashMap<Long, SClient> serversMap;
    
    // CLIENTS Map     
    private final HashMap<Long, SClient> clientsMap;
    
    private final SClient[] serversList;
    
    private SClient socketMonitor;
    
    // Requests waiting to be assigned to a server.
    private final Map<Long, Message> waitingRequests;
    
    
    public LB(long Id, SServer server, SClient socketMonitor, LBGUI lbGUI) {
        this.lbId = Id;
        System.out.println("LB Id: "+Id);
        this.server = server;
        this.socketMonitor = socketMonitor;
        new ClientHandler(socketMonitor).start();
        this.lbGUI = lbGUI;
        this.serversMap = new HashMap<>();
        this.clientsMap = new HashMap<>();
        this.waitingRequests = new HashMap<>();
        serversList = new SClient[1];

    }
    
    @Override
    public void run() {
        Object msg;
        server.open();
        Socket socket;
        try{
            while((socket = server.accept()) != null)         
               try {
                    new ClientHandler(new SClient(socket)).start();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
            catch (Exception e) {System.out.println(e.toString());}
    } 
    
    /*
        Add a new server
    */
    public void addServer(long serverID, SClient client){
        serversMap.put(serverID, client);
    }
    
    /*
        Add a new client
    */
    public void addClient(long clientId, SClient client){
        clientsMap.put(clientId, client);
    }
    
    public void runMonitor(SClient client){
        this.socketMonitor = client;
    }
    
    public void newRequest(Message msg){
        waitingRequests.put(msg.getRequestId(), msg);
        this.socketMonitor.writeObject(msg);
        // Append to interface
        lbGUI.appendRequest(msg.getRequestId(), msg.getNum_iterations(), msg.getClientId(), -1);
    }
    
    public void replyMsg(Message msg){
        System.out.println("REPLY - " + msg.getPIValue());
        
        clientsMap.get(msg.getClientId()).writeObject(msg);
        
        /*SClient c2;
        Long c_id;
        for (HashMap.Entry<Long, SClient>set : clientsMap.entrySet()) {
            c2 = set.getValue();
            c_id = set.getKey();
            if(c_id == msg.getClientId()){
                c2.writeObject(msg);
                break;
            }
        }*/
    }
    
    public void serverAssignReq(Message msg){
        long right_server = getRightServer(msg.getCapacity_map());
        System.out.println("Right Server: "+right_server);
        msg.print();
        if (right_server == -1){
            
            Message m = waitingRequests.get(msg.getRequestId());
            Message reply = new Message("REPLY",m.getClientId(), m.getRequestId(), "03", m.getNum_iterations(), m.getDeadline());
            clientsMap.get(m.getClientId()).writeObject(reply);
        }
        else{
            Message m = waitingRequests.get(msg.getRequestId());
            serversMap.get(right_server).writeObject(m);
        }
    }
    
    
    private long getRightServer(Map<Long, Integer> capacity_map){
        long serverId = -1;
        int minCounter = -1;
        boolean isFirst = true;
        for (Map.Entry<Long, Integer>set : capacity_map.entrySet()) {
            
            if(isFirst){
                isFirst = false;
                serverId = set.getKey();
                minCounter = set.getValue();
            } else if(set.getValue() < minCounter){
                minCounter = set.getValue();
                serverId = set.getKey();
            }
        }
        return serverId;
    }
    
    
    class ClientHandler extends Thread {
        private final SClient client;
        
        // Constructor
        public ClientHandler(SClient client)
        {
            this.client = client;
            client.createSocket();
        }
        
        @Override
        public void run()
        {
            
            try{
                Message msg;
                while ((msg = client.readObject()) != null) {
                    System.out.printf(
                        " Sent from the client: %s\n",
                        msg.getRequestId());
                    // writing the received message from
                    // client
                    
                    // if msg -> New REQ | New SERVER | HEATBEAT | 
                    switch(msg.getType()){
                        case "NEW_SERVER":
                            addServer(msg.getClientId(), client);
                            serversList[0] = client;
                            //client.writeObject(msg);
                            break;
                            
                        case "NEW_CLIENT":
                            addClient(msg.getClientId(), client);
                            //client.writeObject(msg);
                            break; 
                        case "MONITOR":
                            runMonitor(client);
                            break; 
                            
                        case "REQ":
                            newRequest(msg);
                            break;
                        case "SERVERS_CAPACITY":
                            serverAssignReq(msg);
                            break; 
                        case "REPLY":
                            replyMsg(msg);
                            break;
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e.toString()+ "Algo falhou");
            }

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
            Message heart_msg = new Message("HEARTBEAT", lbId, true);
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
