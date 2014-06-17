package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.EntidadInteligente;
import entidad.Entidad;
import entidad.Personaje;
import static util.Maths.*;

public class ComportamientoPatrullar implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private float minimaDistanciaAtaque = 80f;
    private float maximaDistanciaAtaque = 300;
    private float[] p1;
    private float[] p2;
    private float[] siguientePunto;

    /* Para potenciales enemigos */
    private Personaje objetivo = null;

    public ComportamientoPatrullar(EntidadInteligente entidadControlada, float[] p1, float[] p2) {
        this.entidadControlada = entidadControlada;

        this.p1 = p1;
        this.p2 = p2;

        siguientePunto = p1;
    }

    public void actualizar() {
        if (objetivo == null) {
            /* Si no hay enemigos vamos al siguiente punto */
            if (entidadControlada.ir(siguientePunto)) {
                if (siguientePunto.equals(p1)) {
                    siguientePunto = p2;
                } else {
                    siguientePunto = p1;
                }
            }
            /* Miramos si hay malos cerca */
            for (Personaje per : diccionarioEntidades.getPersonajesHostiles(entidadControlada)) {
                if (distancia(per.posiciones, entidadControlada.posiciones) < minimaDistanciaAtaque) {
                    objetivo = per;
                    break;
                }
            }
        } else {
            entidadControlada.atacar(objetivo);
            /* Si el enemigo está lejísimos del siguiente punto la patrulla */
            if(distancia(siguientePunto, objetivo.posiciones) > maximaDistanciaAtaque) {
                objetivo = null;
            }
        }
    }
}
