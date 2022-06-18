/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * This Monitor contains several errors. Debug it.
 */
package pa3_g22.Server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author OMP
 */
public class MFIFO implements IFIFO{
    
    private int idxPut = 0;
    private int idxGet = 0;
    private int count = 0;
    
    private final long fifo[];
    private final int size;
    private final ReentrantLock rl;
    private final Condition cNotFull;
    private final Condition cNotEmpty;
    
    public MFIFO(int size) {
        this.size = size;
        fifo = new long[ size ];
        rl = new ReentrantLock();
        cNotEmpty = rl.newCondition();
        cNotFull = rl.newCondition();
    }
    @Override
    public void put( long value ) {
        try {
            rl.lock();
            while ( count == size )
                cNotFull.await();
            fifo[ idxPut ] = value;
            idxPut++;
            count++;
            cNotEmpty.signal();
        } catch ( InterruptedException ex ) {}
        finally {
            rl.unlock();
        }
    }
    @Override
    public long get() {
        try{
            rl.lock();
            try {
                while ( count == 0 )
                    cNotEmpty.await();
            } catch( InterruptedException ex ) {}
            
            idxGet = idxGet % size;
            long data = fifo[idxGet++];
            count--;
            if(count == size){cNotFull.signal();}
            return data;
        }
        finally {
            rl.unlock();
        }
    }


    @Override
    public void removeAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void resetFIFO() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public int getCount(){
        return count;
    }
}
