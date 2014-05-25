package entidad;

import figuras.EntidadInteligente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Actualizable;

public class DiccionarioEntidades implements Actualizable {

    private Map<Integer, EntidadFisica> listaEntidadesFisicas = new HashMap<Integer, EntidadFisica>();
    private ArrayList<EntidadInteligente> listaObjetosNoFisicos = new ArrayList<EntidadInteligente>();

    private EntidadInteligente jugador;

    private Integer index = new Integer(0);

    /**
     * ************ On demand holder initialization *************
     */
    private DiccionarioEntidades() {
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
    public List<Integer> buscarEntidades(TipoEntidad te) {
        List<Integer> objetivos = new ArrayList<Integer>();
        for (EntidadJava3D e : listaEntidadesFisicas.values()) {
            if (e.getTipos().contains(te)) {
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
}
