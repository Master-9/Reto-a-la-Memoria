/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos;

import control.Casos;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import juegomemo.JuegoMemo;
import modelos.Vista;

/**
 *
 * @author wlad
 */
public class Trabajador implements Runnable{
    private Image imagenPredet; 
    Deque pil;
    Vista unaVista;
    Casos dos;
    
    public Trabajador( Image imagenPredet, Vista unaVista, Deque pil, Casos dos){
        this.pil = pil;
        this.imagenPredet = imagenPredet;
        this.unaVista = unaVista;
        this.dos = dos;
        
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        unaVista.setImagePre(imagenPredet);
        Integer aux = (Integer)pil.removeFirst();
        for(Vista z:dos.getVista(aux)){     //hay un pop
            z.setImagePre(imagenPredet);
        }
        dos.setExclusivo(true);
    }
}
