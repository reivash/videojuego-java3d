package entrada;

import dataengine.DataGroup;
import dataengine.DataNode;
import static dataengine.DataTestMain.print;
import dataengine.Yylex;
import dataengine.parser;
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
import eventos.Evento;
import figuras.EsferaMDL;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ConcurrentModificationException;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Point3d;

public class Teclado
        extends javax.media.j3d.Behavior  {

    /* Mapa de acciones asociadas a las teclas */
    private Map<String, Evento> map = new HashMap<String, Evento>();
    private Set<String> teclasPulsadas = new HashSet<>();

    private WakeupOnAWTEvent presionada = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    private WakeupOnAWTEvent liberada = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
    private WakeupCondition keepUpCondition = null;
    private WakeupCriterion[] continueArray = new WakeupCriterion[2];

    private BranchGroup branchGroup = new BranchGroup();
    private EsferaMDL jugador;

    /* Para cuando processStimulus es llamado (asíncronamente) y estamos en actualizar revisando las teclas */
    private boolean iterando = false;

    public Teclado(BranchGroup conjunto) {
        continueArray[0] = liberada;
        continueArray[1] = presionada;
        keepUpCondition = new WakeupOr(continueArray);

        branchGroup.addChild(this);
        setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0));
        conjunto.addChild(branchGroup);

        /* Leermos las acciones */
//        cargarConfiguracion("teclado.txt");
    }

    public Teclado(BranchGroup conjunto, String tecladotxt) {
        this(conjunto);
        /* Empezar */
        try {
            /* Leer configuración teclado */
            InputStream in = new FileInputStream("teclado.txt");

//            System.out.print("Write some test data defs: \n");
            BufferedReader aux = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = aux.readLine();
            while (line.length() > 0) {
                sb.append(line);
                sb.append(" ");
                line = aux.readLine();
            }
            in = new ByteArrayInputStream(sb.toString().getBytes());

            parser p = new parser(new Yylex(in));
            DataGroup data = (DataGroup) p.parse().value;

            interpretarDatos(data);

//            System.out.println("The parsing worked: " + (data != null));
            print(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interpretarDatos(DataNode datos) {
        for (DataNode dn : datos.asGroup()) {

            DataGroup datosTecla = dn.asGroup();

            String tecla = datosTecla.getIdentifier();

            TipoEntidad tipoObjetivo = TipoEntidad.valueOf(datosTecla.getNodeByIndex(0).asValue().getValue().getDatum().toString().toUpperCase());
            String comando = datosTecla.getNodeByIndex(1).asValue().getValue().getDatum().toString();

            /* Parámetros */
            ArrayList<String> parametros = new ArrayList<String>();
            for (int i = 2; i < datosTecla.getAllNodes().size(); i++) {
                parametros.add(datosTecla.getNodeByIndex(i).asValue().getValue().getDatum().toString());
            }

            Evento e = new Evento();
            e.setTipoObjetivo(tipoObjetivo);
            e.setCommando(comando);
            e.setParams(parametros);

            map.put(tecla, e);
        }
    }

    public void initialize() {
        wakeupOn(keepUpCondition);
    }

    public void processStimulus(Enumeration criteria) {

        if (!iterando) {
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

    public void actualizar() {
        iterando = true;
        try {
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
        } catch (ConcurrentModificationException e) {
            /* Incluso con la variable iterando estas excepcione siguen apareciendo, aunque con menor frecuencia */
        }
        iterando = false;
    }

    public void setJugador(EsferaMDL jugador) {
        this.jugador = jugador;
    }

}
