package entidad;

import comportamiento.Comportamiento;
import comportamiento.ComportamientoGuardian;
import eventos.Evento;
import figuras.Personaje;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import main.Juego;
import util.Maths;

/* Funcionalidad: vida, sistema de ataque y comportamiento */
public class EntidadInteligente extends Personaje {

    private Comportamiento comportamiento = null;



    public EntidadInteligente(String ficheroMDL, float radio, BranchGroup conjunto, Juego juego, boolean esPersonaje) {
        super(ficheroMDL, radio, conjunto, juego, esPersonaje);
        /* Comportamiento por defecto */
        comportamiento = new ComportamientoGuardian(this);
    }

    public void actualizar() {
        super.actualizar();

        comportamiento.actualizar();
    }

    public void setComportamiento(Comportamiento comportamiento) {
        this.comportamiento = comportamiento;
    }

    public Comportamiento getComportamiento() {
        return comportamiento;
    }

}
