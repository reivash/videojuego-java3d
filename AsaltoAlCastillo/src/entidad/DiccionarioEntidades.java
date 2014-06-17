package entidad;

import disparadores.Disparador;
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
    private List<Entidad> colaEliminacion = new ArrayList<Entidad>();
    private List<Entidad> colaCreacion = new ArrayList<Entidad>();
    private Integer index = new Integer(0);

    private List<Disparador> disparadores = new ArrayList<Disparador>();

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

    public void añadirDisparador(Disparador d) {
        disparadores.add(d);
    }

    public void encolar(Entidad entidad) {
        if(!colaCreacion.contains(entidad)){
            colaCreacion.add(entidad);
        }
    }

    public void creaEncolados() {
        /*for (Propiedades p : colaCreacion) {
            p.registrar();
        }*/
        if(colaCreacion.size()!=0){
            System.out.println(">> Tam. cola creación: " + colaCreacion.size());
        }
        for (Entidad e : colaCreacion){
            añadirEntidad(e);
        }
        colaCreacion.clear();
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

        /* Actualizar disparadores */
        for (Disparador d : disparadores) {
            d.actualizar();
        }
    }

    public void mostrar() {
        /* Mostrar las entidades */
        for (Entidad e : listaEntidades.values()) {
            if(colaEliminacion.size()==0 || !colaEliminacion.contains(e)){
                e.mostrar();
            } else {
                System.out.println(">>>>>>> No mostrando: " + e);
            }
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

    public void eliminarEncolados() {
        if(colaEliminacion.size()!=0){
            System.out.println(">> Tam. cola eliminación: " + colaEliminacion.size());
        }
        for (Entidad e : colaEliminacion) {
            e.remover();
        }
        colaEliminacion.clear();
    }

    public void marcarParaEliminar(Entidad e) {
        if(!colaEliminacion.contains(e)){
           colaEliminacion.add(e); 
        }
    }

    public Iterable<Personaje> getPersonajesHostiles(Entidad objetivo) {
        List<String> etiquetas = objetivo.getEtiquetas();
        ArrayList<Personaje> entidadesHostiles = new ArrayList<Personaje>();

//        System.out.println("Contenido diccionario: " + listaEntidades.values());
//        System.out.println("Buscando personajes hostiles para entidad con etiquetas: " + etiquetas.toString());
        /* Si alguna de nuestras etiquetas tiene como hostil 
         alguna de sus etiquetas 
         entonces es una entidad hostil */
        boolean hostil = false;
        for (Entidad ef : listaEntidades.values()) {
            if (ef.getClass().equals(EntidadInteligente.class) || ef.getClass().equals(Personaje.class)) {
//                System.out.println("Entidad: " + ef.getEtiquetas().toString());
                for (String ee : etiquetas) {
                    for (String ee2 : ef.getEtiquetas()) {
//                        System.out.println("nuestras etiquetas: "+ etiquetas.toString() + " sus etiquetas: " + ef.getEtiquetas().toString());
                        if (mapHostilidades.get(ee).contains(ee2)) {
                            hostil = true;
                            entidadesHostiles.add((Personaje) ef);
                            break;
                        }
                    }
                    if (hostil) {
                        break;
                    }
                }
                hostil = false;
            }
        }
        return entidadesHostiles;
    }
}
