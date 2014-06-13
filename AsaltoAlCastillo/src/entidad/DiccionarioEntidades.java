package entidad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiccionarioEntidades {

    private Map<Integer, EntidadFisica> listaEntidadesFisicas = new HashMap<Integer, EntidadFisica>();
    /* No se usa */
//    private ArrayList<EntidadPerseguidora> listaObjetosNoFisicos = new ArrayList<EntidadPerseguidora>();

    private Map<EtiquetaEntidad, List<EtiquetaEntidad>> mapHostilidades = new HashMap<EtiquetaEntidad, List<EtiquetaEntidad>>();

    private Integer index = new Integer(0);

    /**
     * ************ On demand holder initialization *************
     */
    private DiccionarioEntidades() {
        /* Creamos hostilidades por defecto */
        ArrayList<EtiquetaEntidad> enemigosJugador = new ArrayList<EtiquetaEntidad>();
        enemigosJugador.add(EtiquetaEntidad.ENEMIGO);
        mapHostilidades.put(EtiquetaEntidad.JUGADOR, enemigosJugador);

        ArrayList<EtiquetaEntidad> enemigosCastillo = new ArrayList<EtiquetaEntidad>();
        enemigosCastillo.add(EtiquetaEntidad.JUGADOR);
        mapHostilidades.put(EtiquetaEntidad.ENEMIGO, enemigosCastillo);

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
        for (EntidadFisica ef : listaEntidadesFisicas.values()) {
            ef.actualizar();
        }
    }

    public void mostrar() {
        /* Mostrar las entidades */
        for (EntidadFisica e : listaEntidadesFisicas.values()) {
            e.mostrar();
        }
    }

    public void añadirEntidadFisica(EntidadFisica ef) {
        listaEntidadesFisicas.put(ef.getId(), ef);
    }

    public void eliminarEntidadFisica(EntidadFisica ef) {
        listaEntidadesFisicas.remove(ef.getId());
    }

    /* Buscar entidad por su tipo */
    public List<EntidadJava3D> buscarEntidades(EtiquetaEntidad te) {
        List<EntidadJava3D> objetivos = new ArrayList<EntidadJava3D>();
        for (EntidadJava3D e : listaEntidadesFisicas.values()) {
            if (e.getEtiquetas().contains(te)) {
                objetivos.add(e);
            }
        }
        return objetivos;
    }

    public List<Integer> buscarIdEntidades(EtiquetaEntidad te) {
        List<Integer> objetivos = new ArrayList<Integer>();
        for (EntidadJava3D e : listaEntidadesFisicas.values()) {
            if (e.getEtiquetas().contains(te)) {
                objetivos.add(e.getId());
            }
        }
        return objetivos;
    }

    public EntidadJava3D getEntidad(Integer id) {
        for (EntidadJava3D e : listaEntidadesFisicas.values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public Collection<EntidadFisica> getEntidadesFisicas() {
        return listaEntidadesFisicas.values();
    }

    public Iterable<EntidadFisica> getEntidadesHostiles(EntidadJava3D objetivo) {
        List<EtiquetaEntidad> etiquetas = objetivo.getEtiquetas();
        ArrayList<EntidadFisica> entidadesHostiles = new ArrayList<EntidadFisica>();

        /* Si alguna de nuestras etiquetas tiene como hostil 
         alguna de sus etiquetas 
         entonces es una entidad hostil */
        boolean hostil = false;
        for (EntidadFisica ef : listaEntidadesFisicas.values()) {
            for (EtiquetaEntidad ee : etiquetas) {
                for (EtiquetaEntidad ee2 : ef.getEtiquetas()) {
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
