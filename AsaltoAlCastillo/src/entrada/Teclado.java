package entrada;

import entidad.Jugador;
import entidad.TipoEntidad;
import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import util.Actualizable;
import eventos.Evento;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;

public class Teclado
        extends javax.media.j3d.Behavior implements Actualizable {

    /* Mapa de acciones asociadas a las teclas */
    private Map<String, Evento> map = new HashMap<String, Evento>();
    private Set<String> teclasPulsadas = new HashSet<>();

    private WakeupOnAWTEvent presionada = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    private WakeupOnAWTEvent liberada = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
    private WakeupCondition keepUpCondition = null;
    private WakeupCriterion[] continueArray = new WakeupCriterion[2];

    private BranchGroup branchGroup = new BranchGroup();
    private Jugador jugador;

    public Teclado(BranchGroup conjunto) {
        continueArray[0] = liberada;
        continueArray[1] = presionada;
        keepUpCondition = new WakeupOr(continueArray);

        branchGroup.addChild(this);
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        conjunto.addChild(branchGroup);

        /* Leermos las acciones */
        cargarConfiguracion("teclado.txt");
    }

    public void initialize() {
        wakeupOn(keepUpCondition);
    }

    public void processStimulus(Enumeration criteria) {

        while (criteria.hasMoreElements()) {
            WakeupCriterion ster = (WakeupCriterion) criteria.nextElement();
            if (ster instanceof WakeupOnAWTEvent) {
                AWTEvent[] events = ((WakeupOnAWTEvent) ster).getAWTEvent();
                for (int n = 0; n < events.length; n++) {
                    if (events[n] instanceof KeyEvent) {
                        KeyEvent ek = (KeyEvent) events[n];
                        String key = String.valueOf(ek.getKeyChar());
                        if (ek.getID() == KeyEvent.KEY_PRESSED && !teclasPulsadas.contains(key)) {
                            /* ToDo: Guardar la tecla pulsada */
                            teclasPulsadas.add(key);

                        } else if (ek.getID() == KeyEvent.KEY_RELEASED) {
                            /* Eliminar la tecla que se ha pulsado */
                            teclasPulsadas.remove(key);
                        }
                    }
                }
            }
        }
        wakeupOn(keepUpCondition);
    }

    private void cargarConfiguracion(String archivo) {
        try {
            Scanner sc = new Scanner(new File(archivo));
            while (sc.hasNextLine()) {
                Scanner scannerDeLinea = new Scanner(sc.nextLine());
                scannerDeLinea.useDelimiter("[ ]+");

                /* Leer tecla */
                String tecla = scannerDeLinea.next();

                /* Leer accion a realizar */
                Evento e = new Evento();
                try {
                    e.setTipoObjetivo(TipoEntidad.valueOf(scannerDeLinea.next().toUpperCase()));

                    e.setCommando(scannerDeLinea.next());

                    if (scannerDeLinea.hasNext()) {
                        ArrayList<String> params = new ArrayList<>();
                        while (scannerDeLinea.hasNext()) {
                            params.add(scannerDeLinea.next());
                        }
                        e.setParams(params);
                    }
                } catch (Exception ex) {
                    continue;
                }

                map.put(tecla, e);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Teclado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar() {
        for (String tecla : teclasPulsadas) {
            // ¿Es necesario String.valueOf? No eliminar sin comprobar que funciona sin él
            Evento e = map.get(tecla);

            /* El jugador es un caso especial. El teclado habla directamente con él */
            if (e != null) {
                if (e.getTipoObjetivo().equals(TipoEntidad.JUGADOR) && jugador != null) {
                    jugador.realizarAccion(e);
                }
            }
        }
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

}
