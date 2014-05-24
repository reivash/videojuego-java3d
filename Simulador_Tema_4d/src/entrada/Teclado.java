//package entrada;
//
//
//import java.awt.AWTEvent;
//import java.awt.event.KeyEvent;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.media.j3d.WakeupCondition;
//import javax.media.j3d.WakeupCriterion;
//import javax.media.j3d.WakeupOnAWTEvent;
//import javax.media.j3d.WakeupOr;
//import util.Actualizable;
//
//
/*
        EN CONSTRUCCIÓN :D
*/



//public class Teclado
//        extends javax.media.j3d.Behavior implements Actualizable {
//
//    /* Mapa de acciones asociadas a las teclas */
//    private Map<String, Accion> map = new HashMap<String, Accion>();
//    private Set<String> teclasPulsadas = new HashSet<>();
//
//    private WakeupOnAWTEvent presionada = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
//    private WakeupOnAWTEvent liberada = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
//    private WakeupCondition keepUpCondition = null;
//    private WakeupCriterion[] continueArray = new WakeupCriterion[2];
//
//    public Teclado() {
//        continueArray[0] = liberada;
//        continueArray[1] = presionada;
//        keepUpCondition = new WakeupOr(continueArray);
//
//        /* Leermos las acciones */
//        cargarConfiguracion("keyboard_config.txt");
//    }
//
//    public void initialize() {
//        wakeupOn(keepUpCondition);
//    }
//
//    public void processStimulus(Enumeration criteria) {
//
//        while (criteria.hasMoreElements()) {
//            WakeupCriterion ster = (WakeupCriterion) criteria.nextElement();
//            if (ster instanceof WakeupOnAWTEvent) {
//                AWTEvent[] events = ((WakeupOnAWTEvent) ster).getAWTEvent();
//                for (int n = 0; n < events.length; n++) {
//                    if (events[n] instanceof KeyEvent) {
//                        KeyEvent ek = (KeyEvent) events[n];
//                        String key = String.valueOf(ek.getKeyChar());
//                        if (ek.getID() == KeyEvent.KEY_PRESSED && !teclasPulsadas.contains(key)) {
//                            /* ToDo: Guardar la tecla pulsada */
//                            teclasPulsadas.add(key);
//                            
//                        } else if (ek.getID() == KeyEvent.KEY_RELEASED) {
//                            /* Eliminar la tecla que se ha pulsado */
//                            teclasPulsadas.remove(key);
//                        }
//                    }
//                }
//            }
//        }
//        wakeupOn(keepUpCondition);
//    }
//
//    private void cargarConfiguracion(String archivo) {
//        try {
//            Scanner sc = new Scanner(new File(archivo));
//            while (sc.hasNextLine()) {
//                Scanner scannerDeLinea = new Scanner(sc.nextLine());
//                scannerDeLinea.useDelimiter("[ ]+");
//
//                /* Leer tecla */
//                String tecla = scannerDeLinea.next();
//                
//                /* Leer accion a realizar */
//                Event e = new Event();
//                try {
//                    e.setTargetType(scannerDeLinea.next());
//
//                    e.setCommand(scannerDeLinea.next());
//
//                    if (scannerDeLinea.hasNext()) {
//                        ArrayList<String> params = new ArrayList<>();
//                        while (scannerDeLinea.hasNext()) {
//                            params.add(scannerDeLinea.next());
//                        }
//                        e.setParams(params);
//                    }
//                } catch (Exception ex) {
//                    continue;
//                }
//
//                map.put(tecla, e);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Teclado.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void actualizar() {
//        for (String tecla : teclasPulsadas) {
//            // ¿Es necesario String.valueOf? No eliminar sin comprobar que funciona sin él
//            Accion e = map.get(String.valueOf(tecla));
//            
//            /* ToDo: realizar acción en el personaje */
//            
//        }
//    }    
//}
