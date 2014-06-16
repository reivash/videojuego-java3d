package comportamiento;

import entidad.Bola;
import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.EntidadInteligente;
import entidad.Personaje;
import javax.vecmath.Vector3f;
import static util.Maths.*;
import util.Weka;

public class ComportamientoApuntar implements Comportamiento {

    public static final long TIEMPO_ESPERA_ATAQUES = 1000;
    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private float maximaRangoDistancia = 256f;
    private long tiempoUltimoAtaque = 0;

    /* El supuesto enemigo */
    private Personaje objetivo = null;

    private Weka weka;

    public ComportamientoApuntar(EntidadInteligente objetivo) {
        this.entidadControlada = objetivo;
        weka = new Weka("apuntar.arff");
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
            float distanciaObjetivo = distancia(objetivo.posiciones, entidadControlada.posiciones);
            if (distanciaObjetivo > maximaRangoDistancia) {
                /* Si el enemigo está demasiado lejos del punto guardado lo olvidamos */
                objetivo = null;
//                System.out.println("Objetivo fuera del perímetro");
            } else {
                if (entidadControlada.estaMirando(objetivo.posiciones)) {
                    if (tiempoUltimoAtaque + TIEMPO_ESPERA_ATAQUES < System.currentTimeMillis()) {
                        weka.generarCasoADecidir(distanciaObjetivo); //inicializamos el weka con la distancia al objetivo
                        float fuerzaEstimada = (float) weka.resultadoEsperado();
                        // fuerzaEstimada es la fuerza que calcula WEKA que habrá que utilizar. Ahora hay que crear y lanzar la bola.
                        Vector3f vectorFuerza = new Vector3f(entidadControlada.direccionFrontal());
                        vectorFuerza.set(vectorFuerza.x * fuerzaEstimada, fuerzaEstimada / 2f, vectorFuerza.z * fuerzaEstimada);
                        Bola bolazo = new Bola(0.5f, 5, vectorFuerza, "res//texturas//bola.jpg", entidadControlada.branchGroup, entidadControlada.juego);
                        bolazo.setWeka(weka, fuerzaEstimada);

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
