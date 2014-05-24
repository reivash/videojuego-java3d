package entidad;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import figuras.EntidadInteligente;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simulador.Juego;
import util.Actualizable;
import util.Mostrable;

public abstract class EntidadJava3D implements Actualizable, Mostrable {

    protected int identificadorFigura;
    public int identificadorFisico;

    protected Juego juego;

    /* Java3D */
    protected BranchGroup branchGroup;
    protected Matrix3f matrizRotacion = new Matrix3f();
    protected BranchGroup ramaVisible = new BranchGroup();
    public TransformGroup desplazamiento = new TransformGroup();
    protected float[] posiciones = new float[3];
    protected int[] posAnteriorMilimetros = new int[3];


    /* Constructor */
    public EntidadJava3D(Juego juego, BranchGroup branchGroup) {

        this.branchGroup = branchGroup;
        this.juego = juego;

        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        desplazamiento.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ramaVisible.setCapability(BranchGroup.ALLOW_DETACH);
    }


    public Vector3d conseguirDireccionFrontal() {

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
}
