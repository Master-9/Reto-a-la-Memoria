/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegomemo;

import control.Casos;
import hilos.Cronometro;
import hilos.CronometroBackground;
import hilos.Trabajador;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;
import static javafx.stage.WindowEvent.WINDOW_HIDING;
import modelos.Juego;
import modelos.Jugador;
import modelos.Vista;

/**
 *
 * @author wlad
 */
public class JuegoMemo extends Application {
    
    private Image imagenPredet;
    private Casos dos = new Casos("C:\\Users\\wlad\\Pictures\\");
    private Cronometro hora;
    private Label reloj;
    private CronometroBackground backclock;
    TilePane tablero;
    Scene unGrid;
    Scene scene;
    Stage primaryStage;
    ChoiceBox<String> jugadores;
    boolean conJugadoresReg;
    boolean seleccionadoDirectorio;
    
    public void manejador(WindowEvent e){
        EventType<WindowEvent> type = e.getEventType();
        if(type == WINDOW_HIDING){
            if(primaryStage.getScene().getRoot().getId().equals("rootJuego"))
                reiniciar();
            System.out.println("TERMINADDO");
        }
    } 
    
    public void elijaJugadorYdirectorio(){
        ButtonType aceptar = new ButtonType("Aceptar",ButtonData.OK_DONE);
        Dialog<ButtonType> dial = new Dialog<>();
        dial.getDialogPane().getButtonTypes().add(aceptar);
        dial.setTitle("Mensaje:");
        dial.setContentText("Debe elegir un jugador y la carpeta de donde se cargarán las fotos, para poder iniciar una partida!");
        dial.showAndWait();
    }
    
    public void mensajeLleneDatosCompletos(){
        ButtonType aceptar = new ButtonType("Aceptar",ButtonData.OK_DONE);
        Dialog<ButtonType> dial = new Dialog<>();
        dial.getDialogPane().getButtonTypes().add(aceptar);
        dial.setTitle("Mensaje:");
        dial.setContentText("Debe llenar todos los campos antes de hacer click en Registrar!");
        dial.showAndWait();
    }
    
    public void registroExitoso(){
        ButtonType aceptar = new ButtonType("Aceptar",ButtonData.OK_DONE);
        Dialog<ButtonType> dial = new Dialog<>();
        dial.getDialogPane().getButtonTypes().add(aceptar);
        dial.setTitle("Mensaje:");
        dial.setContentText("Su registro fue exitoso!, ya puede iniciar una partida");
        Optional<ButtonType> res = dial.showAndWait();
        if(res.isPresent() && res.get()
            .getButtonData() == ButtonData.OK_DONE){
            System.out.println(res+"fin");
            primaryStage.setScene(scene);          
        }
    }
    
    public void  loadPlayers(){
        if(Files.exists( dos.getDir().resolve("usuarios.csv") )){
            try {       //Paths.get("./src/usuarios.csv")
                Files.lines(dos.getDir().resolve("usuarios.csv"), UTF_8).filter(p -> !p.startsWith("nombreUser"))    //filtrar lineas q empiecen por nombreUser
                    .forEach(p -> jugadores.getItems().add( p.split(",", 2)[0] ) );
            } catch (IOException ex) {
                Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void registrarse(ActionEvent ev){
        GridPane forma = new GridPane();
        forma.setAlignment(Pos.CENTER);
        forma.setHgap(10);
        forma.setVgap(10);
        forma.setPadding(new Insets(25,25,25,25));
        
        Text titulo = new Text("Registro de nuevo usuario");
        forma.add(titulo, 0, 0, 2, 1 );
        
        Label nombreUser = new Label("Nombre:");
        forma.add(nombreUser, 0, 1);
        TextField nombre = new TextField();
        nombre.getSelectedText();
        forma.add(nombre,1,1);
        
        Label edadUser = new Label("Edad:");
        forma.add(edadUser, 0, 2);
        TextField edad = new TextField();           
        
        edad.setMaxWidth(55);
        edad.setPromptText("70");
        forma.add(edad,1,2);
        
        ComboBox elegirSexo = new ComboBox(FXCollections.observableArrayList("M","F"));
        elegirSexo.getValue();
        Label sexo = new Label("Sexo:");
        forma.add(sexo, 0, 3);
        forma.add(elegirSexo,1,3);
        
        Button registrar = new Button("Registrar");
        HBox hbRegistrar = new HBox(10);
        hbRegistrar.setAlignment(Pos.BOTTOM_RIGHT);
        hbRegistrar.getChildren().add(registrar);
        forma.add(hbRegistrar, 1, 4);
        forma.setId("rootRegistro");
        
        
        Scene registro = new Scene(forma, 300, 275);
        primaryStage.setScene(registro);
        registrar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(edad.getText() == null || nombre.getText() == null || elegirSexo.getValue() == null ){
                   mensajeLleneDatosCompletos();
                }
                else{
                    try {
                    Files.write(dos.getDir().resolve("usuarios.csv"), ("nombreUser, edad, sexo" + System.lineSeparator())
                            .getBytes(UTF_8), StandardOpenOption.CREATE_NEW);
                    } catch (IOException ex) {}
  
                    StringBuilder lineas= new StringBuilder(nombre.getText()+", ")
                        .append(edad.getText()+", ").append(elegirSexo.getValue() + System.lineSeparator());
                
                    try {
                        Files.write(dos.getDir().resolve("usuarios.csv"),lineas.toString()
                            .getBytes(UTF_8),StandardOpenOption.CREATE, StandardOpenOption.APPEND);          
                    } catch (IOException ex) {
                        Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    jugadores.getItems().add(nombre.getText());
                    registroExitoso();
                }
                
            }
        });
        
        
        
    }
    
    public void reiniciar(){
        try {
                    Files.write(dos.getDir().resolve("archivo.csv"), ("nombre, fecha, nAciertos, nClicks, gano, tiempo" + System.lineSeparator())
                            .getBytes(UTF_8), StandardOpenOption.CREATE_NEW);
                } catch (IOException ex) {}
                
                Juego aux = dos.getActualGame();
                if(aux != null){
                StringBuilder lineas= new StringBuilder()
                        .append(aux.getJugador()+", ").append(aux.getFecha()+", ").append(aux.getNaciertos()+", ")
                        .append(Vista.numClicks+", ").append(aux.getGano()+", ")
                        .append(hora.getTiempoTranscurridoSeg() + System.lineSeparator());
                
                aux.setTimeElapsedSeg(hora.getTiempoTranscurridoSeg());
                try {
                    Files.write(dos.getDir().resolve("archivo.csv"),lineas.toString()
                            .getBytes(UTF_8),StandardOpenOption.CREATE, StandardOpenOption.APPEND);          
                } catch (IOException ex) {
                    Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                hora.reset();
                dos.reset(imagenPredet);
                tablero.getChildren().clear();
    }
    
    private void manejoGano(){
        ButtonType loginB = new ButtonType("Nueva Partida",ButtonData.YES);
        ButtonType salir = new ButtonType("Salir",ButtonData.FINISH);
        Dialog<ButtonType> dial = new Dialog<>();
        dial.getDialogPane().getButtonTypes().addAll(loginB,salir);
        dial.setTitle("Felicitaciones Ganaste !!!");
        dial.setContentText("Deseas Salir o ir a una nueva Partida?");
        Optional<ButtonType> res = dial.showAndWait();
        if(res.isPresent() && res.get()
            .getButtonData() == ButtonData.FINISH){
            System.out.println(res+"fin");
            primaryStage.close();          
        }
        if(res.isPresent() && res.get()
            .getButtonData() == ButtonData.YES){
            System.out.println(res+"otra vez");
            reiniciar();
            logicaJuego();
        }                         
    }
    
    public void bindToWorker(final Task<ObservableList<Long>> worker){  
        reloj = new Label("hola");
        reloj.setStyle("-fx-font-size: 16pt");
        reloj.textProperty().bind(worker.titleProperty());
        
    }
    
    public void bindToWorkerService(ReadOnlyStringProperty propiedad){
        reloj.setStyle("-fx-font-size: 16pt");
        reloj.textProperty().bind(propiedad);
    }
    
    public void logicaJuego(){
         List<Vista> t= dos.get();  //new
         tablero.getChildren().addAll(dos.showVistas());  //agrega las vistas al tablero cada vez que se inicia un juego 
                
                primaryStage.setScene(unGrid);
                LocalDateTime fechaHora = LocalDateTime.now();                
                dos.addGame(new Juego(new Jugador(jugadores.getValue()), fechaHora.toLocalDate(), Casos.tamMaxPila));
                
                hora.start();                     
                bindToWorkerService(servicio.titleProperty());
                servicio.start();      //hora.showActualTimeBackgroundService();
                //hora.showActualTimeBackground();    //ONE TIME
                Vista.numClicks = 0;
                Deque pil = dos.getPila();
                pil.clear();
        
                Juego x =dos.getActualGame();
                t.parallelStream().forEach(s->{  s.setOnMouseClicked(new EventHandler<MouseEvent>(){
                //t.stream().forEach(s->{  s.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle( MouseEvent e){                        
                        if( (! s.propia.getEncontrado()) && s.getReverso() &&  dos.getExclusivo()){
                            Vista.numClicks++;  //num de cartas levantadas durante el juego
                            Integer primero= pil.isEmpty() ? -1 : (Integer)pil.peekFirst();
                            s.setImage();   //debe cambiar a reverso = false 
                            if(primero >=0){
                                if(dos.getVista(primero).get(0).propia.getEncontrado()){    //puedo chequear caulquiera, porq propia es la misma para key= primero
                                    pil.addFirst(s.getKey());
                                }
                                else{   //comparo con el mio
                                    if(primero == s.getKey()){                                  
                                        pil.addFirst(s.getKey());  //Push
                                        s.propia.setEncontrado(true);
                                        if(x.resultado(pil.size())){
                                            manejoGano();
                                            /*
                                            Dialog<ButtonType> jh = manejoGano();
                                            result = jh.showAndWait();
                                            if(result.isPresent() && result.get()
                                                    .getButtonData() == ButtonData.FINISH){
                                                System.out.println(result+"fin");
                                                primaryStage.close();
                                            }
                                            if(result.isPresent() && result.get()
                                                    .getButtonData() == ButtonData.YES){
                                                System.out.println(result+"otra vez");
                                                reiniciar();
                                                logicaJuego();
                                            }
                                            */                                              
                                        }
                                        
                                        
                                        
                                    }
                                    else{
                                        dos.setExclusivo(false);
                                        Thread desa = new Thread(new Trabajador(imagenPredet,s,pil,dos));
                                        desa.start();                               
                                    }
                                }                     
                            }
                            else{
                                pil.addFirst(s.getKey());
                            }        
                        }   //fin del s.getReverso            
                    }
                    
                }); } );
            
    }
    
    
    
    
    Service<ObservableList<Long>> servicio = new Service<ObservableList<Long>>(){
        @Override
        protected Task<ObservableList<Long>> createTask() {
            backclock = new CronometroBackground();
            backclock.setInicio(hora.getTinicio());
            return backclock;
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
            
    }; 
    
    @Override
    public void init(){}
    
    @Override
    public void stop(){ //el programa termino, no hay ninguna ventana abierta
        //dos.getActualGame().setTimeElapsedSeg(hora.getTiempoTranscurridoSeg());
        //System.out.println(hora.getTiempoTranscurridoSeg());
        //System.out.println("Programa terminado");
        //System.out.println("el jugador gano es: "+ dos.getActualGame().getGano());
        //System.out.println("Y logro: "+dos.getActualGame().getNaciertos()+" aciertos");
        //System.out.println("El numero de clicks realizados fue de:"+ Vista.numClicks);
    }
    
    @Override
    public void start(Stage primaryStage) {        
        this.primaryStage = primaryStage;       
        URL url1 = getClass().getClassLoader().getResource("azul.jpg");
        imagenPredet = new Image(url1.toExternalForm());
        //imagenPredet = new Image(new FileInputStream("C:\\Users\\wlad\\Documents\\Computer Science\\servicio\\azul.jpg"));
        
        //backclock = new CronometroBackground(); //ONE TIME
        //bindToWorker(backclock);    //ONE TIME
        reloj = new Label("hola");
        seleccionadoDirectorio = false;
        
        
        Button btn = new Button();
        btn.setText("Registrarse");
        btn.setMinSize(100, 30);
        btn.setLayoutX(-120);
        
        NumberBinding layoutXBinding = btn.layoutXProperty().add(btn.widthProperty().add(5));
        Button btnJugar = new Button();
        btnJugar.setText("Jugar");
        btnJugar.setMinSize(100, 30);
        btnJugar.layoutXProperty().bind(layoutXBinding);
        jugadores = new ChoiceBox<>();
        jugadores.setMinSize(100, 25);
             
        jugadores.layoutXProperty().bind(btnJugar.layoutXProperty());
        jugadores.layoutYProperty().bind(btnJugar.layoutYProperty().add(btnJugar.heightProperty().add(10)));
        
        
        Label mensaje = new Label("Seleccione un jugador:");
        
        //mensaje.setStyle("-fx-font-size: 12pt");
        mensaje.setFont(Font.font("Helvetica",FontWeight.BOLD,14));
        mensaje.setTextFill(Color.DARKBLUE);
        mensaje.layoutXProperty().bind(jugadores.layoutXProperty().subtract(jugadores.widthProperty().add(60)));
        mensaje.layoutYProperty().bind(jugadores.layoutYProperty());
        /*
        otra forma: Observable<String> listaJugadores = FXCollections.<String>observableArrayList("Roman","Marisa");
        ChoiceBox<String> jugadores = new ChoiceBox<>(listaJugadores);
        */
        
        //Text texto = new Text("Probando la aplicacion");
        //texto.setFont(new Font(24));
        //texto.setX(0);
        //texto.setY(0);
        //Group g = new Group(texto);
    
        Button directorio = new Button("Directorio Fotos");
        
        
        StackPane root = new StackPane();
        //UTIL: root.setBackground(Background.EMPTY);   //pone el fondo del StackPane transparente y finalmente solo se ven los botones
        //root.getChildren().add(btn);
        //root.getChildren().add(0,g);    //pone a g primero que a btn en root
        //root.getChildren().get(0).setVisible(true);     //indico si quiero que g sea visible o no
        
        Group in = new Group(btn,btnJugar,jugadores,mensaje);
        root.getChildren().add(in);
        
        
        root.setStyle("-fx-background-image: url(grey.png); -fx-background-size: 1000 550");
 
        URL url = getClass().getClassLoader().getResource("mano-cursor.png");
        Cursor mio = Cursor.cursor(url.toExternalForm());
        
        BorderPane panelInicio = new BorderPane();
        panelInicio.setCenter(root);
        panelInicio.setTop(directorio);
        panelInicio.getTop().setTranslateX(680);
        panelInicio.getTop().setStyle("-fx-color: blue; -fx-font-size: 10pt");
        panelInicio.setId("rootInicial");  //root.setId("rootInicial");
        scene = new Scene(panelInicio, 800, 600);
        scene.setCursor(mio);
        
        
        
  
        /*
        try {
            dos.getFilesNamesImages();
        } catch (IOException ex) {
            Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        dos.createTable(imagenPredet);  //aqui se ponen la imagen predeterminada al inicio de un juego
       */
        double hgap = 2.0;
        double vgap = 2.0;
        tablero = new TilePane(Orientation.HORIZONTAL,hgap,vgap);
        tablero.setPrefColumns(6);
        tablero.setPrefRows(3);
        tablero.setPrefWidth(25);
        tablero.setPrefHeight(25);            
       
        BorderPane panel = new BorderPane();
        panel.setCenter(tablero);
        panel.setTop(reloj);    //agrego el Label reloj al panel
        panel.setId("rootJuego");
        unGrid = new Scene(panel,1210,645);
        
        
        /*
            FORMULARIO:
        */
        /*
        GridPane forma = new GridPane();
        forma.setAlignment(Pos.CENTER);
        forma.setHgap(10);
        forma.setVgap(10);
        forma.setPadding(new Insets(25,25,25,25));
        
        Text titulo = new Text("Registro de nuevo usuario");
        forma.add(titulo, 0, 0, 2, 1 );
        Label nombreUser = new Label("Nombre:");
        forma.add(nombreUser, 0, 1);
        TextField nombre = new TextField();
        forma.add(nombre,1,1);
        Label edadUser = new Label("Edad:");
        forma.add(edadUser, 0, 2);
        TextField edad = new TextField();
        forma.add(edad,1,2);
        
        ComboBox elegirSexo = new ComboBox(FXCollections.observableArrayList("M","F"));
        Label sexo = new Label("Sexo:");
        sexo.layoutXProperty().bind(elegirSexo.layoutXProperty().subtract(elegirSexo.widthProperty().add(85)));
        sexo.layoutYProperty().bind(elegirSexo.layoutYProperty());
        forma.add(sexo, 0, 3);
        forma.add(elegirSexo,1,3);
        
        
        Scene registro = new Scene(forma, 300, 275);
        primaryStage.setScene(registro);
        */
        
        /*
            EVENTOS
        */  
        
        DirectoryChooser direc = new DirectoryChooser();
       
        directorio.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                direc.setTitle("Elija de que carpeta cargar las fotos");
                direc.setInitialDirectory(new File("c:\\"));
                File dir = direc.showDialog(null);
                try {
                    dos.setRuta(dir.toPath(), imagenPredet);
                } catch (IOException ex) {
                    Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                seleccionadoDirectorio = true;
                loadPlayers(); 
                reiniciar();
            }   
        });
        
        btn.setOnAction(ev -> registrarse(ev));
        
        primaryStage.setOnHiding(e -> manejador(e));    //primaryStage.setOnHiding(e -> manejador(e));
        
        //AL CERRAR EL TABLERO:
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){     //primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event) {
                if(primaryStage.getScene().getRoot().getId().equals("rootJuego")){
                    event.consume();    //evita que la solicitud de cierre hecha desde la X superior derecha termine el programa.                
                    primaryStage.setScene(scene);
             
                /*
                try {
                    Files.write(Paths.get("./archivo.csv"), ("nombre, fecha, nAciertos, nClicks, gano, tiempo" + System.lineSeparator())
                            .getBytes(UTF_8), StandardOpenOption.CREATE_NEW);
                } catch (IOException ex) {}
                
                Juego aux = dos.getActualGame();
                StringBuilder lineas= new StringBuilder()
                        .append(aux.getJugador()+", ").append(aux.getFecha()+", ").append(aux.getNaciertos()+", ")
                        .append(Vista.numClicks+", ").append(aux.getGano()+", ")
                        .append(hora.getTiempoTranscurridoSeg() + System.lineSeparator());
                
                aux.setTimeElapsedSeg(hora.getTiempoTranscurridoSeg());
                try {
                    Files.write(Paths.get("./archivo.csv"),lineas.toString()
                            .getBytes(UTF_8),StandardOpenOption.CREATE, StandardOpenOption.APPEND);          
                } catch (IOException ex) {
                    Logger.getLogger(JuegoMemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                hora.reset();
                dos.reset(imagenPredet);
                tablero.getChildren().clear();
                */
                    reiniciar();
                }
                
                if(primaryStage.getScene().getRoot().getId().equals("rootRegistro")){
                    event.consume();
                }
                
            }
        } );
        
        
        
         hora = new Cronometro(servicio);
         //hora = new Cronometro(backclock);   //ONE TIME
         //List<Vista> t= dos.get(); NOSE USA
         
         btnJugar.setOnAction(new EventHandler<ActionEvent>() {  //inicio de juego
            
            @Override
            public void handle(ActionEvent event) {
                if(jugadores.getValue() != null && seleccionadoDirectorio)
                    logicaJuego();
                else
                    elijaJugadorYdirectorio();
                    
                /*
                tablero.getChildren().addAll(dos.showVistas());  //agrega las vistas al tablero cada vez que se inicia un juego              
                primaryStage.setScene(unGrid);
                LocalDateTime fechaHora = LocalDateTime.now();                
                dos.addGame(new Juego(new Jugador("Richard"), fechaHora.toLocalDate(), Casos.tamMaxPila));
                
                hora.start();                     
                bindToWorkerService(servicio.titleProperty());
                servicio.start();      //hora.showActualTimeBackgroundService();
                //hora.showActualTimeBackground();    //ONE TIME
                Vista.numClicks = 0;
                Deque pil = dos.getPila();
                pil.clear();
        
                Juego x =dos.getActualGame();
                t.parallelStream().forEach(s->{  s.setOnMouseClicked(new EventHandler<MouseEvent>(){
                //t.stream().forEach(s->{  s.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle( MouseEvent e){                        
                        if( (! s.propia.getEncontrado()) && s.getReverso() &&  dos.getExclusivo()){
                            Vista.numClicks++;  //num de cartas levantadas durante el juego
                            Integer primero= pil.isEmpty() ? -1 : (Integer)pil.peekFirst();
                            s.setImage();   //debe cambiar a reverso = false 
                            if(primero >=0){
                                if(dos.getVista(primero).get(0).propia.getEncontrado()){    //puedo chequear caulquiera, porq propia es la misma para key= primero
                                    pil.addFirst(s.getKey());
                                }
                                else{   //comparo con el mio
                                    if(primero == s.getKey()){                                  
                                        pil.addFirst(s.getKey());  //Push
                                        s.propia.setEncontrado(true);
                                        if(x.resultado(pil.size())){
                                            System.out.println(x.getNaciertos());
                                            Dialog<ButtonType> jh = manejoGano();
                                            result = jh.showAndWait();
                                            if(result.isPresent() && result.get()
                                                    .getButtonData() == ButtonData.FINISH){
                                                System.out.println(result+"fin");
                                                primaryStage.close();
                                            }
                                            if(result.isPresent() && result.get()
                                                    .getButtonData() == ButtonData.YES){
                                                reiniciar();
                                            }
                                            
                                                
                                        }                                  
                                    }
                                    else{
                                        dos.setExclusivo(false);
                                        Thread desa = new Thread(new Trabajador(imagenPredet,s,pil,dos));
                                        desa.start();                               
                                    }
                                }                     
                            }
                            else{
                                pil.addFirst(s.getKey());
                            }        
                        }   //fin del s.getReverso            
                    }
                    
                }); } );
               
                */
                
                
           }//action event
        });
        
        
        primaryStage.setTitle("RETO A LA MEMORIA!");
        //scene.setFill(Color.TRANSPARENT);   //pone el fondo de la escena transparente
        primaryStage.setScene(scene);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);     //quita los controles de la parte superior de la ventana
        
        long inicio = System.nanoTime();
        primaryStage.show();
        long tiempoDeDuracion = System.nanoTime() - inicio;
        System.out.println(tiempoDeDuracion);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
