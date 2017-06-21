/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.time.LocalDate;

/**
 *
 * @author wlad
 */
public class Juego {
    private Jugador player;
    private LocalDate fecha;
    private short nAciertos;
    private boolean gano;
    private long timeElapsedSeg;
    private final int tamMaxPila;
    //private Dificultad nivel;
    
    public Juego(Jugador player,LocalDate fecha, int tam){
        this.player = player;
        this.fecha = fecha;
        nAciertos = 0;
        tamMaxPila = tam;
        gano = false;
    }
        
    public boolean resultado(int pilaActual){
        gano = pilaActual == tamMaxPila;
        nAciertos++;
        return gano;
    }
    
    public boolean getGano(){
        return gano;
    }
    
    public int getNaciertos(){
        return nAciertos;
    }
    
    public LocalDate getFecha(){
        return fecha;
    }
    
    public void setTimeElapsedSeg(long ss){
        timeElapsedSeg = ss;
    }
    
    public String getJugador(){
        return player.getJugador();
    }
    
}
