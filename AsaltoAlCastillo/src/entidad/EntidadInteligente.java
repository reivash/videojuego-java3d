package entidad;

import com.bulletphysics.linearmath.Transform;
import comportamiento.Comportamiento;
import comportamiento.ComportamientoGuardian;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Quat4f;
import main.Juego;

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
        if (comportamiento != null) {
            comportamiento.actualizar();
        }

        if (!muerto && vida <= 0) {
            muerto = true;

            /* Tumbar */
            Transform trans = new Transform();
            Quat4f rotacion = new Quat4f();
            cuerpoRigido.getCenterOfMassTransform(trans);
            trans.getRotation(rotacion);
            rotacion.x = 0;
            rotacion.z = 1;
            trans.setRotation(rotacion);
            cuerpoRigido.setCenterOfMassTransform(trans);

            comportamiento = null;
        }
    }

    public void setComportamiento(Comportamiento comportamiento) {
        this.comportamiento = comportamiento;
    }

    public Comportamiento getComportamiento() {
        return comportamiento;
    }

    void setDañoAtaque(int i) {
        this.dañoAtaque = i;
    }

    public float getDistanciaAtaque() {
        return distanciaAtaque;
    }

}
