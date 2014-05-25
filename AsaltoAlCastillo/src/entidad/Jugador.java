package entidad;

import eventos.Evento;
import figuras.EsferaMDL;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;
import simulador.Juego;

public class Jugador extends EsferaMDL {

    private float velocidad_giro = 50f;
    private float velocidad_movimiento = 100;

    public Jugador(String ficheroMDL, float radio, BranchGroup conjunto, Juego juego, boolean esPersonaje) {
        super(ficheroMDL, radio, conjunto, juego, esPersonaje);
        logEnabled = false;
    }

    public void realizarAccion(Evento e) {
        log("Dirección frontal: " + direccionFrontal());
        try {
            String command = e.getCommando();
            ArrayList<String> params = e.getParams();
            switch (command) {
                case "mover": {
                    String option = params.get(0);
                    switch (option) {
                        case "adelante":
                            log("Ir adelante");
                            /* Parece ser que no hay forma de saber si una animacion está en marcha */
                            /* Habrá que controlarlo a mano */
//                            ab.playAnimation(nombreAnimacionCaminando, true);
                            velocidad_lineal.x += velocidad_movimiento;
                            break;
                        case "atras":
                            log("Ir atras");
                            velocidad_lineal.x -= velocidad_movimiento;
                            break;
                    }
                    break;
                }
                case "girar": {
                    String option = params.get(0);
                    switch (option) {
                        case "izquierda":
                            log("Girar izquierda");
                            velocidad_angular.y += velocidad_giro;
                            break;
                        case "derecha":
                            log("Girar derecha");
                            velocidad_angular.y -= velocidad_giro;
                    }
                    break;
                }
                /* Porque puedo */
                case "volar": {
                    log("Volando");
                    velocidad_lineal.y += velocidad_movimiento;
                    break;
                }
            }
        } catch (Exception ex) {
            log("Error al procesar evento");
        }
    }

}
