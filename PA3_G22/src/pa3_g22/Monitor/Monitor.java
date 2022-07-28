/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Monitor;

import java.net.Socket;
import java.util.HashMap;
import pa3_g22.Communication.Message;
import pa3_g22.Server.ServerControl;
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
    
    private String host = null;
    
    private int port = 0;
    
    private final SServer server;
    
    // Server and its heartbeat Thread
    private final Map<Long, ServerHeartbeatThread> serverHeartbeats;
    // LBs and its heartbeat Thread
    private final Map<Long, LBHeartbeatThread> lbHeartbeats;
    // Server information
    private final Map<Long, ServerControl> serversInfo;
    // Number of requests being processed or waiting in each server
    private final Map<Long, Integer> serverCounter;
    // Request not assign to a server yet
    private final Map<Long, Message> waitRequests;
    
    private final Map<Long, SClient> lbsMap;
    private final Map<Long, String> lbsRole;
    
    public Monitor(long Id, SServer server, SClient socketClient, MonitorGUI monitorGUI) {
        this.clientId = Id;
        System.out.println("Monitor Id: "+Id);
        this.server = server;
        this.socketLB = socketClient;
        this.monitorGUI = monitorGUI;
        this.serverHeartbeats = new HashMap<>();
        this.lbHeartbeats = new HashMap<>();
        this.serverCounter = new HashMap<>();
        this.serversInfo = new HashMap<>();
        this.waitRequests = new HashMap<>();
        this.lbsMap = new HashMap<>();
        this.lbsRole = new HashMap<>();
    }
    
    public void newServer(long serverId, SClient cc) {
        serversInfo.put(serverId, new ServerControl());
        this.serverCounter.put(serverId, 0);
        this.monitorGUI.appendServer(serverId);
        serverHeartbeats.put(serverId, new ServerHeartbeatThread(serverId));
        serverHeartbeats.get(serverId).start();
    }
    
    // New request received by LB
    public void newRequest(Message msg, SClient cc) {
        waitRequests.put(msg.getRequestId(), msg);
        cc.writeObject(new Message("SERVERS_CAPACITY", this.serverCounter, msg.getRequestId()));
        monitorGUI.appendReq(msg.getRequestId(), msg.getClientId(), msg.getServerId(), msg.getNum_iterations());
    }
    
    public void newIteration(Message msg) 
    {
        serversInfo.get(msg.getServerId()).setCurrentState(msg.getRequestId(), msg.getIteration());
        monitorGUI.setReqState(msg.getRequestId(), msg.getServerId(), String.valueOf(msg.getIteration()));
    }
    
    // A server accepted a new req
    public void inServerRequest(Message msg) {
        
        Message request = waitRequests.remove(msg.getRequestId());
        this.serversInfo.get(msg.getServerId()).addRequest(request);
        this.serverCounter.put(msg.getServerId(), serverCounter.get(msg.getServerId()) + msg.getNum_iterations());
        this.monitorGUI.setServerReqs(msg.getServerId(), msg.getNum_iterations(), true);
    }
    
    // A server has finished processing a req
    public void outServerRequest(Message msg) {
        this.serversInfo.get(msg.getServerId()).removeRequest(msg.getRequestId());
        this.serverCounter.put(msg.getServerId(), serverCounter.get(msg.getServerId()) - msg.getNum_iterations());
        this.monitorGUI.setServerReqs(msg.getServerId(), msg.getNum_iterations(), false);
    }
    
    public void runLB(SClient client, long lbId){
        if (this.lbsMap.isEmpty()){
            this.socketLB = client;
            monitorGUI.appendLB(lbId, "PRIMARY");
            this.lbsRole.put(lbId, "PRIMARY");
        }
        else{
            monitorGUI.appendLB(lbId, "SECUNDARY");
            this.lbsRole.put(lbId, "SECUNDARY");
        }
        this.lbsMap.put(lbId, client);
        lbHeartbeats.put(lbId, new LBHeartbeatThread(lbId));
        lbHeartbeats.get(lbId).start();
 
    }
    
    public void serverCrash(long serverId){
        Message msg = new Message("SERVER_CRASHED", serverId, serversInfo.get(serverId).getRequests());
        monitorGUI.setServerState(serverId, "DOWN");
        serverCounter.remove(serverId);
        serverHeartbeats.remove(serverId);
        this.socketLB.writeObject(msg);
    }
    
    public void lbCrash(long lbId){
        if(this.lbsRole.get(lbId).equals("PRIMARY") && lbsRole.size()>1){
            long new_prim_lb = 0;
            for (Map.Entry<Long, String>set : lbsRole.entrySet()) {
                if(set.getKey() != lbId){
                    new_prim_lb = set.getKey();
                }
            }
            
            monitorGUI.setLBRole(new_prim_lb, "PRIMARY");
            monitorGUI.setLBRole(lbId, "SECUNDARY");
            monitorGUI.setLBState(lbId, "DOWN");
            lbsRole.remove(lbId);
            lbsMap.remove(lbId);
            this.socketLB = lbsMap.get(new_prim_lb);
            this.socketLB.writeObject(new Message("LB_NEW_PRIMARY"));
        }
    }
    
    public void serverHeartbeat(long serverId){
        serverHeartbeats.get(serverId).interrupt();
    }
    
    public void lbHeartbeat(long lbId){
        lbHeartbeats.get(lbId).interrupt();
    }
    
    @Override
    public void run() {
        server.open();
        Socket socket;
        try{
            while((socket = server.accept()) != null)         
               try {
                    new ClientHandler(new SClient(socket, host, port), socket).start();
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
                    // writing the received message from
                    // client
                    
                    // if msg -> New REQ | New SERVER | HEATBEAT | 
                    switch(msg.getType()){
                        
                        case "NEW_LB":
                            runLB(client, msg.getClientId());
                            break;
                        case "NEW_SERVER":
                            newServer(msg.getServerId(),client);
                            break;
                        case "REQ":
                            newRequest(msg,client);
                            break;
                        case "ITERATION":
                            newIteration(msg);
                            break;
                        case "SERVER_REQ_IN":
                            inServerRequest(msg);
                            break;
                        case "SERVER_REQ_OUT":
                            outServerRequest(msg);
                            break;
                        case "HEARTBEAT_SERVER":
                            serverHeartbeat(msg.getServerId());
                            break;    
                        case "HEARTBEAT_LB":
                            lbHeartbeat(msg.getServerId());
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
            //super("Monitoring heartbeat of server " + serverId + " thread");
            this.serverId = serverId;
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(4000);
                    break;
                } catch (InterruptedException ex) {}
            }
            System.out.println("Server "+serverId+" morreu :(");
            serverCrash(serverId);
        }
    }
    class LBHeartbeatThread extends Thread{

        private final long lbId;

        public LBHeartbeatThread(long lbId) {
            this.lbId = lbId;
        }
        
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(4000);
                    break;
                } catch (InterruptedException ex) {}
            }
            // Server Crashes
            System.out.println("LB "+lbId+" morreu :(");
            lbCrash(lbId);
        }
    }
}
