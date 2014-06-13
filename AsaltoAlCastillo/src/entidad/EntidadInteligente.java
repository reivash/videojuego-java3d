package entidad;

import comportamiento.Comportamiento;
import comportamiento.ComportamientoGuardian;
import eventos.Evento;
import figuras.EsferaMDL;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;
import main.Juego;

/* Funcionalidad: vida, sistema de ataque y comportamiento */
public class EntidadInteligente extends EsferaMDL {

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

    public void actualizar() {
        super.actualizar();

        comportamiento.actualizar();

        /* Sistema de ataque */
        if (siguienteAtaque > 0) {
            siguienteAtaque--;
        }
    }

    public void setComportamiento(Comportamiento comportamiento) {
        this.comportamiento = comportamiento;
    }

    public Comportamiento getComportamiento() {
        return comportamiento;
    }

    /* Cuando este método esté hecho se discutirá si debe ir aquí o en EntidadFisica */
    public void ir(float[] p) {

        /* Provisional (es demasiado fuerte) */
        Vector3f dir = new Vector3f(p[0] - posiciones[0], p[1] - posiciones[1], p[2] - posiciones[2]);
        if (dir.x>0 || dir.y>0 || dir.z>0) {
            dir.normalize();
            dir.scale(velocidad_movimiento * 3);
            cuerpoRigido.applyCentralForce(dir);
        }
        /*nomalizar luego escalarlo*/
    }

    public void atacar(EntidadFisica objetivo) {
        /* ToDo: Implementar */

        /* Si podemos atacar y el enemigo está a nuestro alcance: lo hacemos */
        Evento e = new Evento();
        e.setCommando("dañar");
        e.setValor(dañoAtaque);

//       objetivo.procesarEvento(Evento e);
    }

    public float getDistanciaAtaque() {
        /* ToDo: Implementar */

        return 0;
    }

}
