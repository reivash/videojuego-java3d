package entidad;

import comportamiento.Comportamiento;
import comportamiento.ComportamientoGuardian;
import figuras.EsferaMDL;
import javax.media.j3d.BranchGroup;
import main.Juego;

/* Funcionalidad: vida, sistema de ataque y comportamiento */

public class EntidadInteligente extends EsferaMDL{

    private Comportamiento comportamiento = null;
    
    public EntidadInteligente(String ficheroMDL, float radio, BranchGroup conjunto, Juego juego, boolean esPersonaje) {
        super(ficheroMDL, radio, conjunto, juego, esPersonaje);
        comportamiento = new ComportamientoGuardian(this);
    }
    
    public void actualizar(){
        super.actualizar();
        
        comportamiento.actualizar();
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
        
        /* Si podemos atacar: lo hacemos*/
        
        /* Actualizar espera de ataque */
    }

    public float getDistanciaAtaque() {
        /* ToDo: Implementar */
        
        
        return 0;
    }
    
}