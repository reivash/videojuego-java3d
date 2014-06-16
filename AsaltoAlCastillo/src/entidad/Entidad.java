package entidad;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import eventos.Evento;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import main.Juego;
import util.Log;

public abstract class Entidad extends Log {

    /* Identificadores */
    protected int identificadorFigura;
    public int identificadorFisico;

    private static Integer id_seq = 0;
    protected Integer id = null;

    public Juego juego;

    /* Java3D */
    public BranchGroup branchGroup;
    protected Matrix3f matrizRotacion = new Matrix3f();
    protected BranchGroup ramaVisible = new BranchGroup();
    public TransformGroup desplazamiento = new TransformGroup();
    public float[] posiciones = new float[3];
    protected int[] posAnteriorMilimetros = new int[3];

    /* El tipo nos ayuda a agrupar las entidades a nuestro antojo */
    protected List<String> etiquetas = new ArrayList<String>();

    /* Física */
    protected DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();
    protected DiscreteDynamicsWorld mundoFisico;
    public RigidBody cuerpoRigido;
    protected CollisionObject ramaFisica;
    public float masa, elasticidad;
    protected float[] velocidades = new float[3];
    public float[] posiInicial;

    /* La velocidad lineal es relativa a la posicion del jugador, siendo hacia delante el eje x positivo */
    public Vector3f velocidad_lineal = new Vector3f(0f, 0f, 0f);
    public Vector3f velocidad_angular = new Vector3f(0f, 0f, 0f);

    public boolean esMDL;

    /* Visión */
    private float epsilon = .1f;

    /* Muere al llamarse el método remover */
    protected boolean muerto = false;

    /* Constructor */
    public Entidad(Juego juego, BranchGroup branchGroup) {

        /* Java3D */
        this.branchGroup = branchGroup;
        this.juego = juego;

        id = id_seq++;

        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        desplazamiento.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        desplazamiento.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ramaVisible.setCapability(BranchGroup.ALLOW_DETACH);

        /* Parte física */
        this.mundoFisico = juego.getMundoFisico();
        posiInicial = new float[3];
        
        /* Nos añadimos al diccionario */
        diccionarioEntidades.añadirEntidad(this);
    }

    public void crearPropiedades(float masa, float elasticidad, float dampingLineal, Vector3f centro, Vector3f rotacion) {
        //Creación de un cuerpoRigido (o RigidBody) con sus propiedades fisicas 
        this.masa = masa;
        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(centro);
        posiInicial[0] = centro.x;
        posiInicial[1] = centro.y;
        posiInicial[2] = centro.z;
        /* Rotacion */
        float qy = (float) Math.sin(rotacion.y / 2);
        float qw = (float) Math.cos(rotacion.y / 2);
        Quat4f rot = new Quat4f();
        rot.y = qy;
        rot.w = qw;
        groundTransform.setRotation(rot);

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
        diccionarioEntidades.añadirEntidad(this);

        //Presentaci—n inicial de la  figura visual asociada al cuerpo rigido
        Transform3D inip = new Transform3D();
        inip.set(centro);
        /* ToDo: Necesita testearse */

        inip.set(new Quat4f(rotacion.x, rotacion.y, rotacion.z, 1));
        desplazamiento.setTransform(inip);

        //Actualizacion de posicion. La rotacion se empezará a actualizar en el primer movimiento (ver final del metodo mostrar(rigidBody))
        this.posiciones[0] = centro.x;
        this.posiciones[1] = centro.y;
        this.posiciones[2] = centro.z;
    }

    public void remover() {
        if (!muerto) {
            if (etiquetas.contains("JUGADOR")) {
                Evento e = new Evento();
                e.setComando("GAME_OVER");
                juego.procesarEvento(e);
            } else {
                try {
                     /* Así no puede ser buscado más veces */
                    diccionarioEntidades.eliminarEntidad(this);
                    
                    /* Limpiar sus contenidos */
                    //ramaVisible.removeAllChildren();
                    ramaVisible.detach();
                    System.out.println("Eliminando: " + identificadorFigura);
                    mundoFisico.getCollisionObjectArray().remove(identificadorFisico);
                    mundoFisico.removeRigidBody(cuerpoRigido);
                } catch (Exception e) {
                    System.out.println("Ya eliminado");
                }
            }

            /* A veces se llama a remover varias veces, esto lo evita */
            muerto = true;
        }
    }

    public void marcarParaEliminar() {
        diccionarioEntidades.marcarParaEliminar(this);
    }

    public Vector3f direccionFrontal() {

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
        Vector3f dir = new Vector3f(new Vector3d(
                puntoDeEnfrente.x - posPersonaje.x,
                puntoDeEnfrente.y - posPersonaje.y,
                puntoDeEnfrente.z - posPersonaje.z));
        if (dir.x != 0 || dir.y != 0 || dir.z != 0) {
            dir.normalize();
        }
        return dir;
    }

    public boolean mirarA(float[] p) {
//        if(distancia(posiciones, p) < 1) return true;

        if (!estaMirando(p)) {
            Vector3f direccionAlPunto = new Vector3f(
                    p[0] - posiciones[0],
                    p[1] - posiciones[1],
                    p[2] - posiciones[2]);

            direccionAlPunto.normalize();

            float[] a = new float[]{
                direccionAlPunto.x,
                0,
                direccionAlPunto.z
            };

            Vector3f direccionVista = direccionFrontal();
            direccionVista.normalize();
            float[] b = new float[]{
                direccionVista.x,
                0,
                direccionVista.z
            };

            /* From: http://math.stackexchange.com/questions/74307/two-2d-vector-angle-clockwise-predicate */
            float c = direccionAlPunto.x * direccionVista.z
                    - direccionAlPunto.z * direccionVista.x;

            if (c > 0) {
                velocidad_angular.y += 5;
            } else {
                velocidad_angular.y -= 5;
            }
            return false;
        }
        /* Devolveremos true cuando ya estemos mirando al objetivo */
        return true;
    }

    public boolean estaMirando(float[] p) {

//        System.out.println("p: (" + p[0] + ", " + p[2] + ")");
//        System.out.println("posicionActual: (" + posiciones[0] + ", " + posiciones[2] + ")");
        Vector3f direccionAlPunto = new Vector3f(
                p[0] - posiciones[0],
                p[1] - posiciones[1],
                p[2] - posiciones[2]);

        if (direccionAlPunto.x == 0
                && direccionAlPunto.y == 0
                && direccionAlPunto.z == 0) {
            return true;
        }

        direccionAlPunto.normalize();

        float[] a = new float[]{
            direccionAlPunto.x,
            0,
            direccionAlPunto.z
        };

        Vector3f direccionVista = direccionFrontal();
        direccionVista.normalize();

        float[] b = new float[]{
            direccionVista.x,
            0,
            direccionVista.z
        };

        Vector3f distancia = new Vector3f(a[0] - b[0], 0, a[2] - b[2]);

//        System.out.println("a: (" + a[0] + ", " + a[2] + ")");
//        System.out.println("b: (" + b[0] + ", " + b[2] + ")");
//        System.out.println("Distancia angular (recta): " + distancia.length());
        boolean res = distancia.length() < epsilon;

        if (res) {
            /* Eliminamos velocidad de rotación en Y si estamos mirándole */
            Vector3f angularVelocity = new Vector3f();
            cuerpoRigido.getAngularVelocity(angularVelocity);
            angularVelocity.y = 0;
            cuerpoRigido.setAngularVelocity(angularVelocity);
//            System.out.println("Está mirando");
        }
        return res;
    }

    public Integer getId() {
        return id;
    }
    /* ¿Debería ir aquí o en alguna subclase? */

    public void recibirEvento(Evento e) {
        //ToDo:
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void añadirTipo(String ee) {
        etiquetas.add(ee);
    }

    public void eliminarEtiqueta(String ee) {
        etiquetas.remove(ee);
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
//            rot.get(this.matrizRotacion);

            /* Posicion */
            this.posiciones[0] = trans.origin.x;
            this.posiciones[1] = trans.origin.y;
            this.posiciones[2] = trans.origin.z;
        }
    }

    public void actualizar() {
        Vector3f direccionFrente = direccionFrontal();

        /* Fuerza hacia delante */
        cuerpoRigido.applyCentralForce(new Vector3f(direccionFrente.x * velocidad_lineal.x * 0.1f,
                direccionFrente.y,
                direccionFrente.z * velocidad_lineal.x * 0.1f));

        /* Fuerza hacia arriba */
        cuerpoRigido.applyCentralForce(new Vector3f(0, velocidad_lineal.y, 0));

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
