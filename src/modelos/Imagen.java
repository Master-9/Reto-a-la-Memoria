/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author wlad
 */
public class Imagen {
    private Image dibujo;
    private Integer key;
    private boolean encontrado;
    
    public Imagen(String ruta,int key){
        try {
                dibujo = new Image(new FileInputStream(ruta));
        } catch (FileNotFoundException ex) {
                Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.key = key;
        encontrado = false;
    }
    
    public Image getDibujo(){
        return dibujo;
    }
    
    public Integer getKey(){
        return key;
    }
    
    public boolean getEncontrado(){
        return encontrado;
    }
    
    public void setEncontrado(boolean h){
        encontrado = h;
    }
}
