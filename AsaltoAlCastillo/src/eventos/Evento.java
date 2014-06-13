package eventos;

import dataengine.DataGroup;
import entidad.EtiquetaEntidad;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3f;

public class Evento implements Comparable {

    /* Objetivos */
    private EtiquetaEntidad tipoObjetivo = null;
    private List<Integer> objetivos = new ArrayList<Integer>();
    private long tiempo = 0;
    private Integer emisor = -1;

    /* Acciones del evento */
    private String comando = null;
    private ArrayList<String> params = new ArrayList<String>();

    /* Datos */
    private double valor = 0.0;
    private Vector3f vector = new Vector3f();

    /* Constructor */
    // Hey, where is the constructor?

    /* Methods */
    public Evento añadirObjetivo(Integer id) {
        objetivos.add(id);
        return this;
    }

    public Evento añadirParametro(String p) {
        params.add(p);
        return this;
    }

    public Evento setObjetivo(Integer id) {
        emisor = id;
        return this;
    }

    public Integer getSource() {
        return emisor;
    }

    public List<Integer> getTargets() {
        return objetivos;
    }

    public Evento setTargets(List<Integer> targets) {
        this.objetivos = targets;
        return this;
    }

    public long getTiempo() {
        return tiempo;
    }

    public Evento setTiempo(long time) {
        this.tiempo = time;
        return this;
    }

    public String getCommando() {
        return comando;
    }

    public Evento setCommando(String commando) {
        this.comando = commando;
        return this;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public Evento setParams(ArrayList<String> params) {
        this.params = params;
        return this;
    }

    public double getValor() {
        return valor;
    }

    public Evento setValor(double valor) {
        this.valor = valor;
        return this;
    }

    public Vector3f getVector() {
        return vector;
    }

    public Evento setVector(Vector3f vector) {
        this.vector = vector;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        long oTime = ((Evento) o).getTiempo();
        if (tiempo < oTime) {
            return -1;
        } else if (tiempo == oTime) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "Evento\n\t[Target type: " + tipoObjetivo + ",\n\tObjetivos: " + objetivos.toString() + ",\n\tTiempo: " + tiempo + ",\n\tComando: " + comando + ",\n\tParámetros: " + params.toString() + "]";
    }

    public Evento setTipoObjetivo(EtiquetaEntidad tipo) {
        this.tipoObjetivo = tipo;
        return this;
    }

    public EtiquetaEntidad getTipoObjetivo() {
        return tipoObjetivo;
    }
}
