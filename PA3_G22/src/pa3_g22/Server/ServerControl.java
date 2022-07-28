/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pa3_g22.Server;
import pa3_g22.Communication.SClient;
import pa3_g22.Communication.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author joaoc
 */
public class ServerControl {
    //private final SClient cClient;
    private Map<Long, Message> requests;
    private Map<Long, String> currentState;
    

    public ServerControl() {
        //this.cClient = cClient;
        this.requests = new HashMap<>();
        this.currentState = new HashMap<>();
        System.out.println("INIT");
    }

    public void addRequest(Message request){
        requests.put(request.getRequestId(), request);
        currentState.put(request.getRequestId(), "In Queue");
    }
    
    public void removeRequest(long requestId){
        requests.remove(requestId);
        currentState.remove(requestId);
    }
    
    public void setCurrentState(long requestId, String cs){
        this.currentState.replace(requestId, cs);
    }
    
    public List<Message> getRequests(){
        return new ArrayList<>(requests.values());
    }
    
    public Map<Long, String> getCurrentState(){
        return currentState;
    }
}