package entidad;

import eventos.Evento;
import figuras.EsferaMDL;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import net.sf.nwn.loader.ModelAnimation;
import main.Juego;

public class Jugador extends EsferaMDL {

    private float velocidad_giro = 50f;
    private float velocidad_movimiento = 100;

    private String animacionActual = "";

    private boolean accionRealizada = false;

    public Jugador(String ficheroMDL, float radio, BranchGroup conjunto, Juego juego, boolean esPersonaje) {
        super(ficheroMDL, radio, conjunto, juego, esPersonaje);
        logEnabled = true;
    }

    public void realizarAccion(Evento e) {

        accionRealizada = true;

        /* Velocidad por defecto de la animación */
        ab.setAnimationTimeScale(.5f);
        
        try {
            String command = e.getCommando();
            ArrayList<String> params = e.getParams();
            switch (command) {
                case "mover": {
                    String option = params.get(0);
                    switch (option) {
                        case "adelante":
                            log("Ir adelante");
                            if (!animacionActual.equals(nombreAnimacionCaminando)) {
                                ab.playAnimation(nombreAnimacionCaminando, true);
                                ab.setAnimationTimeScale(.5f);
                                animacionActual = nombreAnimacionCaminando;
                            }
                            velocidad_lineal.x += velocidad_movimiento;
                            break;
                        case "atras":
                            if (!animacionActual.equals(nombreAnimacionCaminando)) {
                                ab.playAnimation(nombreAnimacionCaminando, true);
                                ab.setAnimationTimeScale(.5f);
                                animacionActual = nombreAnimacionCaminando;
                            }
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
                case "atacar": {
                    log("Atacando");
                    if (!animacionActual.equals(nombreAnimacionLuchando)) {
                        ab.playAnimation(nombreAnimacionLuchando, true);
                        ab.setAnimationTimeScale(.5f);
                        animacionActual = nombreAnimacionLuchando;
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

    @Override
    public void actualizar() {
        super.actualizar();

        /* Si no estamos haciendo nada ponemos la animacion por defecto */
        if (!accionRealizada && !animacionActual.equals(nombreAnimacionQuieto)) {
            log("Animacion quieto");
            ab.playAnimation(nombreAnimacionQuieto, true);
            animacionActual = nombreAnimacionQuieto;
        }
        accionRealizada = false;
    }
}
