/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author wlad
 */
public class Jugador {
    private String nombre;
    private short edad;
    private char sexo;
    
    public Jugador(String nombre){
        this.nombre = nombre;
    }
    
    public void setJugador(String nombre){
        this.nombre = nombre;
    }
    
    public String getJugador(){
        return nombre;
    }
    
    public void setEdad(short edad){
        this.edad = edad;
    }
    
    public short getEdad(){
        return edad;
    }
    
    public void setSexo(char sexo){
        this.sexo = sexo;
    }
    
    public char getSexo(){
        return sexo;
    }
    
    
}
