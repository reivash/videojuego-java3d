package comportamiento;

import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.EntidadInteligente;
import entidad.Personaje;
import static util.Maths.*;

public class ComportamientoObedecer implements Comportamiento {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private EntidadInteligente entidadControlada = null;
    
    /* Nuestro querido comandante */
    private EntidadInteligente jefe = null;

    public ComportamientoObedecer(EntidadInteligente entidadControlada, EntidadInteligente jefe) {
        this.entidadControlada = entidadControlada;
        this.jefe = jefe;
    }

    public void actualizar() {
        /* Si nuestro jefe muere nos un patatús y al primero que se acerque lo perseguimos hasta el fin de los tiempos */
        if (jefe.estaMuerto()) {
            entidadControlada.setComportamiento(new ComportamientoRangoAtacar(entidadControlada, 256));
        }
    }
}
