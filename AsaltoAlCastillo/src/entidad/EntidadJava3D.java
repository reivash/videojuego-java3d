package entidad;

import eventos.Evento;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import main.Juego;
import util.Actualizable;
import util.Log;
import util.Mostrable;

public abstract class EntidadJava3D extends Log implements Actualizable, Mostrable {

    protected int identificadorFigura;
    public int identificadorFisico;

    protected Juego juego;

    /* Java3D */
    protected BranchGroup branchGroup;
    protected Matrix3f matrizRotacion = new Matrix3f();
    protected BranchGroup ramaVisible = new BranchGroup();
    public TransformGroup desplazamiento = new TransformGroup();
    public float[] posiciones = new float[3];
    protected int[] posAnteriorMilimetros = new int[3];

    /* Identificador entidad */
    private static Integer id_seq = 0;
    private Integer id = null;

    /* El tipo nos ayuda a agrupar las entidades a nuestro antojo */
    private List<TipoEntidad> tipos = new ArrayList<TipoEntidad>();

    /* Constructor */
    public EntidadJava3D(Juego juego, BranchGroup branchGroup) {
        this.branchGroup = branchGroup;
        this.juego = juego;

        id = id_seq++;

        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        desplazamiento.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ramaVisible.setCapability(BranchGroup.ALLOW_DETACH);
    }

    public Vector3d direccionFrontal() {

        /* Posición actual del personaje */
        Transform3D transformActual = new Transform3D();
        desplazamiento.getTransform(transformActual);

        Vector3f posPersonaje = new Vector3f(0, 0, 0);
        transformActual.get(posPersonaje);

        /* Punto delante del personaje */
        Transform3D t3DSonar = new Transform3D();
        t3DSonar.set(new Vector3f(0.0f, 0, 10f));

        Transform3D t3DDelante = new Transform3D(transformActual);
        t3DDelante.mul(t3DSonar);
        Vector3d puntoDeEnfrente = new Vector3d(0, 0, 0);
        t3DDelante.get(puntoDeEnfrente);

        /* Vector dirección frontal */
        return new Vector3d(puntoDeEnfrente.x - posPersonaje.x, puntoDeEnfrente.y - posPersonaje.y, puntoDeEnfrente.z - posPersonaje.z);
    }

    public Integer getId() {
        return id;
    }
    /* ¿Debería ir aquí o en alguna subclase? */

    public void recibirEvento(Evento e) {
        //ToDo:
    }

    public List<TipoEntidad> getTipos() {
        return tipos;
    }

    public void añadirTipo(TipoEntidad te) {
        tipos.add(te);
    }

    public void eliminarTipo(TipoEntidad te) {
        tipos.remove(te);
    }
}
