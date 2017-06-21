/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import javafx.collections.ObservableList;
import javafx.concurrent.Service;

/**
 *
 * @author wlad
 */
public class Cronometro {
    private long tInicio;
    private long tFinal;
    private Thread real;
    private CronometroBackground cb;
    private Thread background;
    Service<ObservableList<Long>> servicio;
    
    public Cronometro(){
        cb = null;
        tFinal = -1;
    }
    
    public Cronometro(CronometroBackground cb){
        this.cb = cb;
        tFinal = -1;
    }
    
    public Cronometro(Service<ObservableList<Long>> servicio){
        real = null;
        this.servicio = servicio;
        tFinal = -1;
    }
    
    public void start(){
        tInicio = System.currentTimeMillis();
    }
    
    public void showActualTimeBackgroundService(){
        //cb.setInicio(tInicio);
        servicio.start();
        
    }
    
    public Long getTinicio(){
        return tInicio;
    }
    
    public void reset(){
        tFinal = -1;
        if (servicio != null)
            servicio.reset();
        if (cb != null)
            System.out.println("estado del hilo background:" + cb.getState());
    }
    
    public void showActualTimeBackground(){
        cb.setInicio(tInicio);
        background = new Thread(cb);
        background.setDaemon(true);
        background.start();
        
    }
    
    private void stop(){
        tFinal= System.currentTimeMillis();
        if (real != null)
            real.interrupt();
        else{
            if(cb == null){
                servicio.cancel();
            }
            else{
                cb.cancel();
            }
        }
    }
    
    public long getTiempoTranscurridoSeg(){
        if(tFinal == -1){
            stop();
        }
        return (tFinal - tInicio) / (long)1000.0;
    }
    
    public void showActualTime(){
        real = new Thread(new RealTime(tInicio));
        real.start();
    }
    
}
