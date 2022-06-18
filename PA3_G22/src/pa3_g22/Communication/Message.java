/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Communication;
import java.io.Serializable;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author joaoc
 */
public class Message implements Serializable{
    private long clientId = 0;  
    private long requestId = 0;
    private long serverId = 0;
    private String msgCode = null;
    private int num_iterations = 0;
    private int deadline = 0;
    private String PIValue = null;
    private SocketAddress client = null;
    private String type = null;
    private int total_iterations = 0;
    // Number of requests being processed in each server
    private Map<Long, Integer> capacity_map = new HashMap<>();;
    
    
    public Message(String type,long clientId, long requestId, String msgCode, int num_iterations, int deadline) {
        this.type = type;
        this.clientId = clientId;
        this.requestId = requestId;
        this.msgCode = msgCode;
        this.num_iterations = num_iterations;
        this.deadline = deadline;
    }

    public Map<Long, Integer> getCapacity_map() {
        return capacity_map;
    }
    
    public Message(String type,long clientId, long serverId, long requestId, String msgCode, int num_iterations, String PIValue, int deadline) {
        this.type = type;
        this.clientId = clientId;
        this.serverId = serverId;
        this.PIValue = PIValue;
        this.requestId = requestId;
        this.msgCode = msgCode;
        this.num_iterations = num_iterations;
        this.deadline = deadline;
    }
    
    public Message(String type,long clientId, long serverId, long requestId, String msgCode, int num_iterations, int deadline) {
        this.type = type;
        this.clientId = clientId;
        this.serverId = serverId;
        this.requestId = requestId;
        this.msgCode = msgCode;
        this.num_iterations = num_iterations;
        this.deadline = deadline;
    }

    public int getTotal_iterations() {
        return total_iterations;
    }

    public String getPIValue() {
        return PIValue;
    }
    
    public Message(String type,long clientId, long requestId, String msgCode, int num_iterations, int deadline, SocketAddress c) {
        this.type = type;
        this.clientId = clientId;
        this.requestId = requestId;
        this.msgCode = msgCode;
        this.num_iterations = num_iterations;
        this.deadline = deadline;
        this.client = c;
    }
    public Message(String type,long clientId) {
        this.type = type;
        this.clientId = clientId;
    }
    public Message(String type, Map<Long, Integer> capacity_map, long requestId) {
        this.type = type;
        this.capacity_map = capacity_map;
        this.requestId = requestId;
    }
    public Message(String type, long serverId, int total_iterations) {
        this.type = type;
        this.serverId = serverId;
        this.total_iterations = total_iterations;
    }
    public Message(String type, long serverId, boolean isServer) {
        this.type = type;
        this.serverId = serverId;
    }
    public Message(String type) {
        this.type = type;
    }
    
    public long getClientId() {
        return clientId;
    }

    public long getRequestId() {
        return requestId;
    }

    public long getServerId() {
        return serverId;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public int getNum_iterations() {
        return num_iterations;
    }

    public int getDeadline() {
        return deadline;
    }

    public String getValueNa() {
        return PIValue;
    }

    public SocketAddress getClient() {
        return client;
    }

    public String getType() {
        return type;
    }
    public void print(){
        System.out.println("Type:"+type+"CLientID:"+clientId+"ServerID:"+serverId+"RequestId:"+requestId);

    }
}
