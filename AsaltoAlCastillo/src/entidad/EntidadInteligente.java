package entidad;

import comportamiento.Comportamiento;
import comportamiento.ComportamientoGuardian;
import eventos.Evento;
import figuras.EsferaMDL;
import javax.media.j3d.BranchGroup;
import main.Juego;

/* Funcionalidad: vida, sistema de ataque y comportamiento */

public class EntidadInteligente extends EsferaMDL{

    private Comportamiento comportamiento = null;
    
    /* Sistema de ataque */
    private float distanciaAtaque = 1.5f;
    private float dañoAtaque = 2;
    private float intervaloAtaque = 15;
    private float siguienteAtaque = 0;
    
    public EntidadInteligente(String ficheroMDL, float radio, BranchGroup conjunto, Juego juego, boolean esPersonaje) {
        super(ficheroMDL, radio, conjunto, juego, esPersonaje);
        /* Comportamiento por defecto */
        comportamiento = new ComportamientoGuardian(this);
    }
    
    public void actualizar(){
        super.actualizar();
        
        comportamiento.actualizar();
        
        /* Sistema de ataque */
        if(siguienteAtaque > 0) {
            siguienteAtaque--;
        }
    }
    
    public void setComportamiento(Comportamiento comportamiento){
        this.comportamiento = comportamiento;
    }
    
    public Comportamiento getComportamiento(){
        return comportamiento;
    }

    /* Cuando este método esté hecho se discutirá si debe ir aquí o en EntidadFisica */
    public void ir(float[] posicionInicial) {
        /* ToDo: Implementar */
        
        /* Si no estamos mirando en la dirección adecuada: girar paulatinamente hacia ella */
        
        /* Si estamos mirando hacia alli: moverse con nuestra velocidad hacia alli */
    }

    public void atacar(EntidadInteligente objetivo) {
        /* ToDo: Implementar */
        
        /* Si podemos atacar y el enemigo está a nuestro alcance: lo hacemos */
        
        Evento e = new Evento();
        e.setCommando("dañar");
        e.setValor(dañoAtaque);
        
//        objetivo.procesarEvento(Evento e);
    }

    public float getDistanciaAtaque() {
        /* ToDo: Implementar */
        
        
        return 0;
    }
    
}