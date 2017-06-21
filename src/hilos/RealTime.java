/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.time.Duration;

/**
 *
 * @author wlad
 */
class RealTime implements Runnable{
    private final long inicio;
    
    
    public RealTime(long inicio ){
        this.inicio = inicio;
    }
    
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
                long s = (System.currentTimeMillis() - inicio) / (long)1000.0;
                String tiempo =  Duration.ofSeconds(s).toString().substring(2) ;
                System.out.println(tiempo);
                
        }
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
