/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.scene.image.Image;
import modelos.Imagen;
import modelos.Juego;
import modelos.Vista;

/**
 *
 * @author wlad
 */
public class Casos {
    private byte nImagenes;
    private byte nVistas;
    private String ruta;
    private List<String> archivos;
    //private List<Imagen> imagenes;
    Map<Integer, List<Vista> > zz;    //Map<int[],ImageView> zz;
    List<Vista> listaVistas;    //List<ImageView> listaVistas;
    Deque pila; //Stack esta descontinuado
    boolean exclusivo;
    public static int tamMaxPila= 0;
    private List<Juego> juegos;
    Path dir = null;
    
    public Casos(String ruta){
        this.ruta= ruta;
        archivos = new ArrayList<>();
        zz= new HashMap();
        nImagenes = 9;
        nVistas = 2;
        pila = new ArrayDeque();
        exclusivo = true;
        tamMaxPila = nImagenes * nVistas;
        juegos = new ArrayList<>();
    }
    
    public int addGame(Juego unJuego){  //retorna la pos en donde se inserto el juego
        juegos.add(unJuego);
        return juegos.size() -1;
    }
    
    public Juego getActualGame(){
        if(juegos.isEmpty()){
            return null;
        }
        return juegos.get(juegos.size() -1);
    }
    
    public Juego getGame(int k){
        return juegos.get(k);
    }
    
    public List<Vista> getVista(Integer k){
        return zz.get(k);
    }
    
    public Deque getPila(){
      return pila;  
    }
    
    public Path getDir(){
        if(dir != null)
            return dir;
        else
            return null;
    }
    
    public void setRuta(Path dir, Image imagenPredet) throws IOException{
        this.dir = dir;
        archivos.clear();
        zz.clear();
        getFilesNamesImages();
        createTable(imagenPredet);
        persistirRutaDirectorio();
    }
    
    private void persistirRutaDirectorio(){
        
    }
    
    public void getFilesNamesImages() throws IOException{
        
        Path laRuta = dir == null ? Paths.get(ruta) : dir;
        //Files.newDirectoryStream(Paths.get(ruta), path -> {
        Files.newDirectoryStream(laRuta, path -> {
            return path.toString().endsWith(".png") || path.toString().endsWith(".jpg");
        })
                .forEach(archivo -> archivos.add(archivo.toString()));
    }
    
    public void createTable(Image imagenPredet){
        Random r = new Random();
        IntStream n = r.ints(nImagenes, 0, archivos.size() -1).distinct();  //asi me trae nImagenes, pero si alguna de ellas sale
                                                //repetida me la elimina con el distinct, osea puede retornar menos de nImagenes.
        
        n = r.ints(0, archivos.size() -1).distinct().limit(nImagenes);    //n = r.ints(nImagenes, 0, archivos.size() -1);
        //Lo anterior me trae todo, luego me elimina los repetidos y finalmente me trae nImagenes.
        n.mapToObj(x-> new Imagen(archivos.get(x),x) )
               .forEach(v -> {List<Vista> as = new ArrayList<Vista>();
                            IntStream.range(0, nVistas)
                                .forEach(i -> as.add(i, new Vista(v)));
                                    zz.put(v.getKey(), as);
                            } );
        
        loadViewsFunc(imagenPredet);
        
    }
    
    private void loadViewsFunc(Image imagenPredet){
        
        List<Vista> ff= new ArrayList<>();
        zz.values().forEach(l -> ff.addAll(l));
        ff.stream().forEach(v -> {v.setFitHeight(200);
                                  v.setFitWidth(200);
                                  v.setPreserveRatio(false);
                                  v.setImagePre(imagenPredet);
                                });
        listaVistas = Collections.synchronizedList(ff);
    }
    
    public List<Vista> showVistas(){
        Collections.shuffle(listaVistas);
        return Collections.synchronizedList(listaVistas);
    }
    
    public void reset(Image imagenPredet){
        listaVistas.stream().forEach(v -> {v.setImagePre(imagenPredet);
                                           v.propia.setEncontrado(false);
                                            });
        exclusivo = true;      
    }
    
    public List<Vista> get(){
        return Collections.synchronizedList(listaVistas);
    }
    
    public void printArchivos(){
        archivos.stream().forEach(System.out::println);
    }
    
    public boolean getExclusivo(){
        return exclusivo;
    }
    
    public void setExclusivo(boolean g){
        exclusivo= g;
    }
    
}
