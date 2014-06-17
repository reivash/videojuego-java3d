package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.EntidadInteligente;
import entidad.Personaje;
import java.util.ArrayList;
import static util.Maths.*;

public class ComportamientoJefazoDefensor implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;

    private float minimaDistanciaDefensa = 50f;

    /* El jefe tiene una vista de halcón y ve los enemigos antes */
    private float distanciaVista = 100f;

    /* Solo podemos mandar sobre personajes inteligentes, hay que mantener el nivel */
    private ArrayList<EntidadInteligente> escuadron = null;

    /* Cada cierto tiempo los soldados deben volver cerca del jefe y formar */
    private int aFormar = 0;
    private int intervaloFormar = 500;

    /* Guardamos a quien estamos atacando para no enviar demasiados soldados */
    private ArrayList<Personaje> enemigosAtacados = new ArrayList<Personaje>();

    public ComportamientoJefazoDefensor(EntidadInteligente entidadControlada, ArrayList<EntidadInteligente> escuadron) {
        this.entidadControlada = entidadControlada;

        this.escuadron = escuadron;
    }

    public void actualizar() {

        /* Comprobar soldados muertos */
        comprobarSoldadosMuertos();

        /* Si se queda sin soldados pasa a defender él */
        if (escuadron.isEmpty()) {
            entidadControlada.setComportamiento(new ComportamientoGuardian(entidadControlada));
        }

        /* Buscamos enemigos */
        float d;
        for (Personaje per : diccionarioEntidades.getPersonajesHostiles(entidadControlada)) {
            /* Si ya se le mandaron tropas */

            d = distancia(per.posiciones, entidadControlada.posiciones);
//            System.out.println("Comprobando personaje con etiquetas " + per.getEtiquetas() + " distancia al jefe: " + d);
            /* Si los enemigos han cruzado el perímetro enviamos dos soldados */
            if (d < entidadControlada.getDistanciaAtaque()) {
                entidadControlada.atacar(per);
            }
            if (d < minimaDistanciaDefensa) {
//                System.out.println("Enemigo dentro del perímetro interno!");
                /* Si un enemigo está demasiado cerca todos deben atacarle */
                for (EntidadInteligente entInt : escuadron) {
                    entInt.setComportamiento(new ComportamientoAtacar(entInt, per));
                }
                break;
            }
            if (enemigosAtacados.contains(per)) {
                continue;
            }
            if (d < distanciaVista) {
                entidadControlada.mirarA(per.posiciones);
                int numSoldados = 2;
                for (EntidadInteligente entInt : escuadron) {
                    if (entInt.getComportamiento().getClass().equals(ComportamientoObedecer.class)) {
                        entInt.setComportamiento(new ComportamientoAtacar(entInt, per));
                        numSoldados--;
                        if (numSoldados <= 0) {
                            break;
                        }
                    }
                }
                enemigosAtacados.add(per);
                break;
            }
        }

        /* Miramos si nuestros soldados deben formar */
        if (aFormar <= 0) {
            aFormar = intervaloFormar;
            for (EntidadInteligente entInt : escuadron) {
                entInt.setComportamiento(new ComportamientoVolverConElJefe(entInt, entidadControlada));
            }
        }
        aFormar--;

        /* Enemigos que ya han muerto y deben ser eliminados de la lista */
        comprobarEnemigosMuertos();
    }

    private void comprobarSoldadosMuertos() {
        ArrayList<EntidadInteligente> listaMuertos = new ArrayList<EntidadInteligente>();

        /* Buscar muertos */
        for (EntidadInteligente entInt : escuadron) {
            if (entInt.estaMuerto()) {
                listaMuertos.add(entInt);
                /* Por si acaso no se le quitó el comportamiento bien al morir y sigue moviéndose*/
                entInt.setComportamiento(null);
            }
        }
        /* Eliminar muertos */
        for (EntidadInteligente entInt : listaMuertos) {
            escuadron.remove(entInt);
        }
    }

    private void comprobarEnemigosMuertos() {
        ArrayList<Personaje> listaMuertos = new ArrayList<Personaje>();

        /* Buscar muertos */
        for (Personaje entInt : enemigosAtacados) {
            if (entInt.estaMuerto()) {
                listaMuertos.add(entInt);
            }
        }
        /* Eliminar muertos */
        for (Personaje entInt : listaMuertos) {
            enemigosAtacados.remove(entInt);
        }
    }
}
