package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.EntidadInteligente;
import entidad.Entidad;
import entidad.Personaje;
import util.Maths;
import static util.Maths.*;

public class ComportamientoRangoAtacar implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private Personaje objetivo = null;
    private float rango = 256;

    public ComportamientoRangoAtacar(EntidadInteligente entidadControlada, float rango) {
        this.entidadControlada = entidadControlada;

        /* Ponemos al jugador como enemigo fijo */
//        this.objetivo = personaje;

        this.rango = rango;
//        System.out.println("Objetivo:  " + objetivo);
    }

    public void actualizar() {
        if (objetivo == null) {
            for (Personaje j : diccionarioEntidades.getPersonajesHostiles(entidadControlada)) {
                if (Maths.distancia(entidadControlada.posiciones, j.posiciones) < rango) {
                    objetivo = j;
                    break;
                }
            }
        } else {
            if (objetivo.estaMuerto()) {
                objetivo = null;
            } else {
                entidadControlada.atacar(objetivo);
            }
        }
    }
}
