/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import java.time.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author wlad
 */
public class CronometroBackground extends Task<ObservableList<Long>> {
    
    private long inicio;
    
    public void setInicio(long inicio){
        this.inicio = inicio;
    }

    @Override
    protected ObservableList<Long> call() throws Exception {
        final ObservableList<Long> res = FXCollections.observableArrayList();
        //long s = 0;
       
        while(!this.isCancelled()){
            //s = (System.currentTimeMillis() - inicio) / (long)1000.0;
            //String tiempo =  Duration.ofSeconds(s).toString().substring(2) ;
            //this.updateValue(FXCollections.unmodifiableObservableList(res));
            this.updateTitle( Duration.ofSeconds( (System.currentTimeMillis() - inicio) / (long)1000.0 )
                    .toString().substring(2) );
        }
        return res;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
