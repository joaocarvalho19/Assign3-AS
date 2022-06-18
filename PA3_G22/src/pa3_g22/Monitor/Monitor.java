/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Monitor;

import java.net.Socket;
import java.util.HashMap;
import pa3_g22.Communication.Message;
import pa3_g22.Communication.SClient;
import pa3_g22.Communication.SServer;
import pa3_g22.LoadBalancer.LB;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author joaoc
 */
public class Monitor extends Thread{
    // Communication client.
    private SClient socketLB;
    // Client ID
    private final long clientId;
    // Client GUI
    private final MonitorGUI monitorGUI;
    
    private final SServer server;
    
    // Server and its heartbeat Thread
    private final Map<Long, ServerHeartbeatThread> serverHeartbeats;
    // LBs and its heartbeat Thread
    private final Map<Long, LBHeartbeatThread> lbHeartbeats;
    // Number of requests being processed in each server
    private final Map<Long, Integer> serverCounter;
    
    
    public Monitor(long Id, SServer server, SClient socketClient, MonitorGUI monitorGUI) {
        this.clientId = Id;
        System.out.println("Monitor Id: "+Id);
        this.server = server;
        this.socketLB = socketClient;
        this.monitorGUI = monitorGUI;
        this.serverHeartbeats = new HashMap<>();
        this.lbHeartbeats = new HashMap<>();
        this.serverCounter = new HashMap<>();
    }
    
    public void newServer(long serverId, SClient cc) {
        this.serverCounter.put(serverId, 0);
        this.monitorGUI.appendServer(serverId);
        serverHeartbeats.put(serverId, new ServerHeartbeatThread(serverId));
        serverHeartbeats.get(serverId).start();
    }
    
    // New request received by LB
    public void newRequest(Message msg, SClient cc) {
        System.out.println("IN REQ");
        cc.writeObject(new Message("SERVERS_CAPACITY", this.serverCounter, msg.getRequestId()));
    }
    
    // A server accepted a new req
    public void inServerRequest(Message msg) {
        msg.print();
        this.serverCounter.put(msg.getServerId(), msg.getTotal_iterations());
        this.monitorGUI.setServerReqs(msg.getServerId(), msg.getTotal_iterations());
    }
    
    // A server has finished processing a req
    public void outServerRequest(Message msg) {
        System.out.println(msg.getServerId());
        this.serverCounter.put(msg.getServerId(), msg.getTotal_iterations());
        this.monitorGUI.setServerReqs(msg.getServerId(), msg.getTotal_iterations());
    }
    
    public void runLB(SClient client){
        System.out.println("LB running");
        this.socketLB = client;
    }
    
    public void serverHeartbeat(long serverId){
        serverHeartbeats.get(serverId).interrupt();
    }
    
    @Override
    public void run() {
        Object msg;
        server.open();
        Socket socket;
        try{
            while((socket = server.accept()) != null)         
               try {
                    new ClientHandler(new SClient(socket), socket).start();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
            catch (Exception e) {System.out.println(e.toString());}
    } 
    
    class ClientHandler extends Thread {
        private final SClient client;
        private final Socket s;
        
        // Constructor
        public ClientHandler(SClient client, Socket s)
        {
            this.s = s;
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
                        case "LB":
                            runLB(client);
                            break;
                        case "NEW_SERVER":
                            System.out.println("NEW Server");
                            newServer(msg.getServerId(),client);
                            break;
                        case "REQ":
                            newRequest(msg,client);
                            break;
                        case "SERVER_REQ_IN":
                            System.out.println("++++++++++++++");
                            inServerRequest(msg);
                            break;
                        case "SERVER_REQ_OUT":
                            System.out.println("---------------");
                            outServerRequest(msg);
                            break;
                        case "HEARTBEAT":
                            serverHeartbeat(msg.getServerId());
                            System.out.println("Server "+msg.getServerId()+ " is alive!");
                            break;    
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e.toString()+ "Algo falhou");
            }
        }
    }
    class ServerHeartbeatThread extends Thread{

        private final long serverId;

        public ServerHeartbeatThread(long serverId) {
            super("Monitoring heartbeat of server " + serverId + " thread");
            this.serverId = serverId;
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(3000);
                    break;
                } catch (InterruptedException ex) {}
            }
            System.out.println("Server "+serverId+" morreu :(");
            serverCounter.remove(serverId);
        }
    }
    class LBHeartbeatThread extends Thread{

        private final long serverId;

        public LBHeartbeatThread(long serverId) {
            super("Monitoring heartbeat of server " + serverId + " thread");
            this.serverId = serverId;
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(3000);
                    break;
                } catch (InterruptedException ex) {}
            }
            System.out.println("Server "+serverId+" morreu :(");
            serverCounter.remove(serverId);
        }
    }
}
