package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.EntidadInteligente;
import entidad.Personaje;
import static util.Maths.*;

public class ComportamientoVolverConElJefe implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    
    private float epsilon = 20f;
    
    /* Nuestro querido comandante */
    private EntidadInteligente jefe = null;

    public ComportamientoVolverConElJefe(EntidadInteligente entidadControlada, EntidadInteligente jefe) {
        this.entidadControlada = entidadControlada;
        this.jefe = jefe;
    }

    public void actualizar() {
        /* Si ya estamos cerca cambiar a comportamiento obedecer */
        if(distancia(entidadControlada.posiciones, jefe.posiciones) < epsilon) {
            entidadControlada.setComportamiento(new ComportamientoObedecer(entidadControlada, jefe));
        } else {
            entidadControlada.ir(jefe.posiciones);
        }
    }
}
