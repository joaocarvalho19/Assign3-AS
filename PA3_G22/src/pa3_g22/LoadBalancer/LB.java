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
    
    private String Monitorhost = "localhost";
    
    private int Monitorport = 2000;
    
    private int LBport = 0;
    
    // SERVERS Map     
    private final HashMap<Long, SClient> serversMap;
    
    // CLIENTS Map     
    private final HashMap<Long, SClient> clientsMap;
    
    private SClient socketMonitor;
    
    // Requests waiting to be assigned to a server.
    private final Map<Long, Message> waitingRequests;
    
    
    public LB(long Id, SServer server, SClient socketMonitor, LBGUI lbGUI , int LBport, String Monitorhost, int Monitorport) {
        this.lbId = Id;
        System.out.println("LB Id: "+Id);
        this.server = server;
        this.socketMonitor = socketMonitor;
        new ClientHandler(socketMonitor, true).start();
        this.lbGUI = lbGUI;
        this.serversMap = new HashMap<>();
        this.clientsMap = new HashMap<>();
        this.waitingRequests = new HashMap<>();
        this.Monitorhost = Monitorhost;
        this.Monitorport = Monitorport;
        
        // Thread to send heartBeats to Monitor
        new HeartBeatThread().start();
    }
    
    @Override
    public void run() {
        server.open();
        Socket socket;
        try{
            while((socket = server.accept()) != null)         
               try {
                    new ClientHandler(new SClient(socket, Monitorhost, Monitorport), false).start();
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
        clientsMap.get(msg.getClientId()).writeObject(msg);
    }
    
    public void updateRole(Message msg){
        System.out.println("NEW PORT!");
        this.server = new SServer(1000);
        /*try {
            this.socketMonitor = new SClient(new Socket(Monitorhost, Monitorport), Monitorhost, Monitorport);
            new ClientHandler(socketMonitor, true).start();
        } catch (IOException ex) {
            System.err.println(ex);
        }*/
        this.run();
    }
    
    // Server is down lets distribute the requests again
    public void serverCrash(Message msg){
        serversMap.remove(msg.getServerId());
        msg.getServerRequests().forEach(m -> {
            lbGUI.removeRequest(m.getRequestId());
            newRequest(m);
        });
    }
    
    public void serverAssignReq(Message msg){
        long right_server = getRightServer(msg.getCapacity_map());
        if (right_server == -1){
            Message m = waitingRequests.get(msg.getRequestId());
            Message reply = new Message("REPLY",m.getClientId(), m.getRequestId(), "03", m.getNum_iterations(), m.getDeadline());
            clientsMap.get(m.getClientId()).writeObject(reply);
        }
        else{
            Message m = waitingRequests.get(msg.getRequestId());
            serversMap.get(right_server).writeObject(m);
            lbGUI.setRequest(msg.getRequestId(), right_server);
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
        public ClientHandler(SClient client, boolean isMon)
        {
            this.client = client;
            client.createSocket();
            if(isMon)
                client.writeObject(new Message("NEW_LB", lbId));
        }
        
        @Override
        public void run()
        {
            
            try{
                Message msg;
                while ((msg = client.readObject()) != null) {
                    // writing the received message from
                    // client
                    
                    switch(msg.getType()){
                        case "NEW_SERVER":
                            addServer(msg.getClientId(), client);
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
                        case "SERVER_CRASHED":
                            serverCrash(msg);
                            break;
                        case "LB_NEW_PRIMARY":
                            updateRole(msg);
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
            Message heart_msg = new Message("HEARTBEAT_LB", lbId, true);
            while(true) {
                try {
                    Thread.sleep(1000);
                    socketMonitor.writeObject(heart_msg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
