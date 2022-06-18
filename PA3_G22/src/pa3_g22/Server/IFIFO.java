/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pa3_g22.Server;

/**
 *
 * @author joaoc
 */
public interface IFIFO {
    public void put( long requestId );
    public long get();
    public void removeAll();
    public void resetFIFO();
    public boolean isEmpty();
}
