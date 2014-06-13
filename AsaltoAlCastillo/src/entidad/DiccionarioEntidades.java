package entidad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiccionarioEntidades {

    private Map<Integer, Entidad> listaEntidades = new HashMap<Integer, Entidad>();
    /* No se usa */
//    private ArrayList<EntidadPerseguidora> listaObjetosNoFisicos = new ArrayList<EntidadPerseguidora>();

    private Map<String, List<String>> mapHostilidades = new HashMap<String, List<String>>();

    private Integer index = new Integer(0);

    /**
     * ************ On demand holder initialization *************
     */
    private DiccionarioEntidades() {
        /* Creamos hostilidades por defecto */
        ArrayList<String> enemigosJugador = new ArrayList<String>();
        enemigosJugador.add("ENEMIGO");
        mapHostilidades.put("JUGADOR", enemigosJugador);

        ArrayList<String> enemigosCastillo = new ArrayList<String>();
        enemigosCastillo.add("JUGADOR");
        mapHostilidades.put("ENEMIGO", enemigosCastillo);

    }

    private static class EntityDictionaryHolder {

        private static DiccionarioEntidades INSTANCE = new DiccionarioEntidades();
    }

    public static DiccionarioEntidades getInstance() {
        return EntityDictionaryHolder.INSTANCE;
    }

    /**
     * **********************************************************
     */
    public void actualizar() {
        /* Actualizar las entidades */
        for (Entidad ef : listaEntidades.values()) {
            ef.actualizar();
        }
    }

    public void mostrar() {
        /* Mostrar las entidades */
        for (Entidad e : listaEntidades.values()) {
            e.mostrar();
        }
    }

    public void añadirEntidad(Entidad e) {
        listaEntidades.put(e.getId(), e);
    }

    public void eliminarEntidad(Entidad e) {
        listaEntidades.remove(e.getId());
    }

    /* Buscar entidad por su tipo */
    public List<Entidad> buscarEntidades(String te) {
        List<Entidad> objetivos = new ArrayList<Entidad>();
        for (Entidad e : listaEntidades.values()) {
            if (e.getEtiquetas().contains(te)) {
                objetivos.add(e);
            }
        }
        return objetivos;
    }

    public List<Integer> buscarIdEntidades(String te) {
        List<Integer> objetivos = new ArrayList<Integer>();
        for (Entidad e : listaEntidades.values()) {
            if (e.getEtiquetas().contains(te)) {
                objetivos.add(e.getId());
            }
        }
        return objetivos;
    }

    public Entidad getEntidad(Integer id) {
        for (Entidad e : listaEntidades.values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public Collection<Entidad> getEntidades() {
        return listaEntidades.values();
    }

    public Iterable<Entidad> getEntidadesHostiles(Entidad objetivo) {
        List<String> etiquetas = objetivo.getEtiquetas();
        ArrayList<Entidad> entidadesHostiles = new ArrayList<Entidad>();

        /* Si alguna de nuestras etiquetas tiene como hostil 
         alguna de sus etiquetas 
         entonces es una entidad hostil */
        boolean hostil = false;
        for (Entidad ef : listaEntidades.values()) {
            for (String ee : etiquetas) {
                for (String ee2 : ef.getEtiquetas()) {
                    if (mapHostilidades.get(ee).contains(ee2)) {
                        hostil = true;
                        entidadesHostiles.add(ef);
                        break;
                    }
                }
                if (hostil) {
                    break;
                }
            }
            hostil = false;
        }
        return entidadesHostiles;
    }
}
