package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.EntidadInteligente;
import static util.Maths.*;

public class ComportamientoGuardian implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private final float[] posicionInicial;
    private float minimaDistanciaPerseguir = 5f;
    private float maximaDistanciaPerseguir = 15f;

    /* No tienes porque estar exactamente en la posición inicial */
    private float epsilon = 1f;

    /* El supuesto enemigo */
    private EntidadInteligente objetivo = null;

    public ComportamientoGuardian(EntidadInteligente objetivo) {
        this.entidadControlada = objetivo;
        posicionInicial = objetivo.posiciones;
    }

    public void actualizar() {
        if (objetivo == null) {
            /* Si estamos aún de vuelta */
            if (distanciaHorizontal(entidadControlada.posiciones, posicionInicial) > epsilon) {
                entidadControlada.ir(posicionInicial);
            } else {
                /* Estar alerta por si se acercan fuerzas hostiles */
                for (Entidad e : diccionarioEntidades.getEntidadesHostiles(entidadControlada)) {
                    if (distanciaHorizontal(e.posiciones, posicionInicial) < minimaDistanciaPerseguir) {
                        objetivo = (EntidadInteligente) e; // Fusionaremos las clases y no habra que hacer casting
                        break;
                    }
                }
            }
        } else {
            if (distancia(objetivo.posiciones, posicionInicial) > maximaDistanciaPerseguir) {
                /* Si el enemigo está demasiado lejos del punto guardado: volver */
                objetivo = null;
            } else {
                /* Si está al alcance: atacar */
                entidadControlada.atacar(objetivo);
            }
        }
    }
}