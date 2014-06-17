package main;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupOnElapsedFrames;

public class Visualizador extends Behavior {
   WakeupOnElapsedFrames framewake = new WakeupOnElapsedFrames(0, true);
   Juego juego;

public Visualizador(Juego juego_ ) {
     juego = juego_;
}

public void initialize() { 
    wakeupOn( framewake );
}

public void processStimulus(Enumeration criteria) {
    try{ juego.mostrar(); } catch(Exception e){}
    wakeupOn( framewake ); }
}