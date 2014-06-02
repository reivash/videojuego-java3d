package entidad;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import main.Juego;

public class EntidadFisica extends EntidadJava3D {

    /* Física */
    protected DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();
    protected DiscreteDynamicsWorld mundoFisico;
    public RigidBody cuerpoRigido;
    protected CollisionObject ramaFisica;
    public float masa, elasticidad;
    protected float[] velocidades = new float[3];

    /* La velocidad lineal es relativa a la posicion del jugador, siendo hacia delante el eje x positivo */
    public Vector3f velocidad_lineal = new Vector3f(0f, 0f, 0f);
    public Vector3f velocidad_angular = new Vector3f(0f, 0f, 0f);

    public boolean esMDL;

    /* Constructor */
    public EntidadFisica(Juego juego, BranchGroup branchGroup) {
        super(juego, branchGroup);
        this.mundoFisico = juego.getMundoFisico();
    }

    public void crearPropiedades(float masa, float elasticidad, float dampingLineal, float posX, float posY, float posZ) {
        //Creaciîn de un cuerpoRigido (o RigidBody) con sus propiedades fisicas 
        this.masa = masa;
        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(new Vector3f(posX, posY, posZ));
        boolean isDynamic = (masa != 0f);
        Vector3f inerciaLocal = new Vector3f(0, 1, 0);
        if (isDynamic && !esMDL) { // 
            this.ramaFisica.getCollisionShape().calculateLocalInertia(masa, inerciaLocal);
        }
        DefaultMotionState EstadoDeMovimiento = new DefaultMotionState(groundTransform);
        RigidBodyConstructionInfo InformacionCuerpoR = new RigidBodyConstructionInfo(masa, EstadoDeMovimiento, this.ramaFisica.getCollisionShape(), inerciaLocal);
        InformacionCuerpoR.restitution = elasticidad;

        cuerpoRigido = new RigidBody(InformacionCuerpoR);
        cuerpoRigido.setActivationState(RigidBody.DISABLE_DEACTIVATION);
        cuerpoRigido.setDamping(dampingLineal, 0.1f);   //a–ade m‡s (1) o menos  (0) "friccion del aire" al desplazarse/caer o rotar
        cuerpoRigido.setFriction(0.3f);
        //A–adiendo el cuerpoRigido al mundoFisico
        mundoFisico.addRigidBody(cuerpoRigido); // add the body to the dynamics world
        identificadorFisico = mundoFisico.getNumCollisionObjects() - 1;

        //A–adiendo objetoVisual asociado al grafo de escea y a la lista de objetos fisicos visibles y situandolo
        branchGroup.addChild(ramaVisible);
        diccionarioEntidades.añadirEntidadFisica(this);

        //Presentaci—n inicial de la  figura visual asociada al cuerpo rigido
        Transform3D inip = new Transform3D();
        inip.set(new Vector3f(posX, posY, posZ));
        desplazamiento.setTransform(inip);

        //Actualizacion de posicion. La rotacion se empezará a actualizar en el primer movimiento (ver final del metodo mostrar(rigidBody))
        this.posiciones[0] = posX;
        this.posiciones[1] = posY;
        this.posiciones[2] = posZ;
    }

    public void remover() {
        try {
            mundoFisico.getCollisionObjectArray().remove(identificadorFisico);
            mundoFisico.removeRigidBody(cuerpoRigido);
            branchGroup.removeChild(identificadorFigura);
            diccionarioEntidades.eliminarEntidadFisica(this);
        } catch (Exception e) {
            System.out.println("Ya eliminado");
        }
    }

    public void mostrar() {

        CollisionObject objeto = mundoFisico.getCollisionObjectArray().get(identificadorFisico); //
        RigidBody cuerpoRigido = RigidBody.upcast(objeto);

        if (cuerpoRigido != null && cuerpoRigido.getMotionState() != null) {
            Transform trans = new Transform();
            cuerpoRigido.getMotionState().getWorldTransform(trans);
            Quat4f orientacion = new Quat4f();
            cuerpoRigido.getOrientation(orientacion);
            Transform3D rot = new Transform3D(orientacion, new Vector3f((float) trans.origin.x, (float) trans.origin.y, (float) trans.origin.z), 1);
            desplazamiento.setTransform(rot);

            //Actualizacion de Matriz de rotación y posiciones
            rot.get(this.matrizRotacion);

            /* Posicion */
            this.posiciones[0] = trans.origin.x;
            this.posiciones[1] = trans.origin.y;
            this.posiciones[2] = trans.origin.z;
        }
    }

    public void actualizar() {
        /* Sistema Marlónico */
        // Movimiento por fuerzas del jugador
//        float fuerzaHaciaAdelante = 0, fuerzaLateral = 0, fuerzaHaciaArriba = 0f;
//        if (adelante) {
//            fuerzaHaciaAdelante = masa * 100f * 2.5f;
//        }
//        if (atras) {
//            fuerzaHaciaAdelante = -masa * 100f * 2.5f;
//        }
//        if (derecha) {
//            fuerzaLateral = -masa * 40f;
//        }
//        if (izquierda) {
//            fuerzaLateral = masa * 40f;
//        }
//        if (arriba) {
//            fuerzaHaciaArriba = masa * 40f;
//        }

        Vector3d direccionFrente = direccionFrontal();

        /* Fuerza hacia delante */
        cuerpoRigido.applyCentralForce(new Vector3f((float) direccionFrente.x * velocidad_lineal.x * 0.1f,
                (float) direccionFrente.y,
                (float) direccionFrente.z * velocidad_lineal.x * 0.1f));
        
        /* Fuerza hacia arriba */
        cuerpoRigido.applyCentralForce(new Vector3f(0,velocidad_lineal.y,0));
        
        
        /* Rotación */
        cuerpoRigido.applyTorque(new Vector3f(0, velocidad_angular.y, 0));

        /* Damping */
        velocidad_lineal.scale(.7f);
        velocidad_angular.scale(.1f);
        if (velocidad_lineal.length() < .0001) {
            velocidad_lineal = new Vector3f(0, 0, 0);
        }
        if (velocidad_angular.length() < .0001) {
            velocidad_angular = new Vector3f(0, 0, 0);
        }

    }

}
