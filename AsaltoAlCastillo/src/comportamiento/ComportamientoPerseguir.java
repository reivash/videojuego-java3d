package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.EntidadFisica;
import entidad.EntidadInteligente;
import entidad.EntidadJava3D;
import entidad.EtiquetaEntidad;
import static util.Maths.distancia;

public class ComportamientoPerseguir implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    private final float[] posicionInicial;
    private float minimaDistanciaPerseguir = 5f;
    private float maximaDistanciaPerseguir = 10f;
    float[] posi; //posicion inicial de la entidad

    /* No tienes porque estar exactamente en la posición inicial */
    private float epsilon = 1f;

    private EntidadJava3D objetivo = null;

    public ComportamientoPerseguir(EntidadInteligente objetivo) {
        this.entidadControlada = objetivo;
        posicionInicial = objetivo.posiciones;
        posi=entidadControlada.posiInicial;
        this.objetivo = diccionarioEntidades.buscarEntidades(EtiquetaEntidad.JUGADOR).get(0);
        System.out.println("Objetivo: " + objetivo);
    }

    public void actualizar() {
        System.out.println("Distancia al perro:" +distancia(objetivo.posiciones, posicionInicial) + " pos"+  posicionInicial[0]);
        if (distancia(objetivo.posiciones, posicionInicial) < maximaDistanciaPerseguir) {
                entidadControlada.ir(objetivo.posiciones);
        }else{
            entidadControlada.ir(posi);
        }
    }
}
