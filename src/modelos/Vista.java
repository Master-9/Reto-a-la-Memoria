/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author wlad
 */
public class Vista extends ImageView{
    private boolean reverso;
    public Imagen propia;
    public static short numClicks;
    
    public Vista(Imagen imagen){
        propia = imagen;
        reverso = true;
    }
    
    public boolean getReverso(){
        return reverso;
    }
   
    
    public Integer getKey(){
        return propia.getKey();
    }
    
    public final void setImage(){
        this.setImage(propia.getDibujo());
        reverso = false;
    }
    
    public final void setImagePre(Image value){
        this.setImage(value);
        reverso = true;
    }
}
