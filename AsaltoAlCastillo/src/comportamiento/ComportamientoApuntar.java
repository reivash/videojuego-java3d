package comportamiento;

import entidad.Bola;
import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.EntidadInteligente;
import entidad.Personaje;
import javax.vecmath.Vector3f;
import static util.Maths.*;

public class ComportamientoApuntar implements Comportamiento {
    public static final long TIEMPO_ESPERA_ATAQUES = 1000;
    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private float maximaRangoDistancia = 256f;
    private long tiempoUltimoAtaque = 0;

    /* El supuesto enemigo */
    private Personaje objetivo = null;

    public ComportamientoApuntar(EntidadInteligente objetivo) {
        this.entidadControlada = objetivo;
    }

    public void actualizar() {
//        System.out.println("distancia al objetivo: " + (objetivo != null ? distancia(objetivo.posiciones, posicionInicial) : "NULL"));
        if (objetivo == null) {
            for (Personaje e : diccionarioEntidades.getPersonajesHostiles(entidadControlada)) {
                if (distancia(e.posiciones, entidadControlada.posiciones) < maximaRangoDistancia) {
                    objetivo = e; // Fusionaremos las clases y no habra que hacer casting
                    break;
                }
            }
        } else {
            if (distancia(objetivo.posiciones, entidadControlada.posiciones) > maximaRangoDistancia) {
                /* Si el enemigo está demasiado lejos del punto guardado lo olvidamos */
                objetivo = null;
//                System.out.println("Objetivo fuera del perímetro");
            } else {
                if(entidadControlada.estaMirando(objetivo.posiciones)){
                    if(tiempoUltimoAtaque+TIEMPO_ESPERA_ATAQUES<System.currentTimeMillis()){
                        // Crear bola aquí con weka
                        System.out.println("Bolillazo");
                        tiempoUltimoAtaque = System.currentTimeMillis();
                    }
                } else {
                    entidadControlada.mirarA(objetivo.posiciones);
                }      
            }
        }
    }
}
