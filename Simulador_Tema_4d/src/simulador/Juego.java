package simulador;

import java.awt.*;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.ArrayList;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import figuras.Esfera;
import figuras.EsferaMDL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Juego extends JFrame {

    SimpleUniverse universo;
    BoundingSphere limites = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    public String rutaCarpetaProyecto = System.getProperty("user.dir") + "/";
    ArrayList<simulador.Figura> listaObjetosFisicos = new ArrayList<Figura>();
    ArrayList<simulador.Figura> listaObjetosNoFisicos = new ArrayList<Figura>();
    DiscreteDynamicsWorld mundoFisico;
    BranchGroup conjunto = new BranchGroup();
    public boolean actualizandoFisicas, mostrandoFisicas;
    public float tiempoJuego;
    // Pesonajes importantes del juego
    Figura personaje;  //golem;
    Figura perseguidor;

    /* Tiempo de espera del loop */
    private float dt = 3f / 100f;
    private int tiempoDeEspera = (int) (dt * 1000);

    private boolean peticionDeCerrar = false;

    public Juego() {

        inicializarJBullet();
        inicializarJava3D();
    }

    public void inicializarJBullet() {
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
        Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
        Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
        AxisSweep3 broadphase = new AxisSweep3(worldAabbMin, worldAabbMax);
        SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
        mundoFisico = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        mundoFisico.setGravity(new Vector3f(0, -10, 0));
    }

    public void inicializarJava3D() {
        Container GranPanel = getContentPane();
        Canvas3D zonaDibujo = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        zonaDibujo.setPreferredSize(new Dimension(800, 600));
        GranPanel.add(zonaDibujo, BorderLayout.CENTER);
        universo = new SimpleUniverse(zonaDibujo);

        BranchGroup escena = crearEscena();

        escena.compile();
        universo.getViewingPlatform().setNominalViewingTransform();
        universo.addBranchGraph(escena);
        universo.getViewer().getView().setBackClipDistance(50);
    }

    public BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        conjunto = new BranchGroup();
        objRoot.addChild(conjunto);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        ComportamientoMostrar mostrar = new ComportamientoMostrar(this);
        DirectionalLight LuzDireccional = new DirectionalLight(new Color3f(10f, 10f, 10f),
                new Vector3f(1f, 0f, -1f));
        BoundingSphere limitesLuz = new BoundingSphere(new Point3d(-15, 10, 15), 100.0); //Localizacion de fuente/paso de luz
        objRoot.addChild(LuzDireccional);
        mostrar.setSchedulingBounds(limites);
        LuzDireccional.setInfluencingBounds(limitesLuz);
        Background bg = new Background();
        bg.setApplicationBounds(limites);
        bg.setColor(new Color3f(135f / 256, 206f / 256f, 250f / 256f));
        objRoot.addChild(bg);
        objRoot.addChild(mostrar);

        //Es sencillo crearlos estaticos como se muestra a continuacion. Si desea que caigan, y se sometan a fuerzas, mejor crear una figura.
        float radio = 2f;
        float posY = -4f;
        // tutorial de creacion de una objeto   (la piedra con textura de ladrillo)
        // parte gráfica del objeto:
        Appearance apariencia = new Appearance();
        apariencia.setTexture(new TextureLoader(System.getProperty("user.dir") + "//texturas//ladrillo.jpg", this).getTexture());
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);
        Sphere figuraVisual = new Sphere(radio, Sphere.GENERATE_TEXTURE_COORDS, 60, apariencia);
        Transform3D desplazamiento2 = new Transform3D();
        desplazamiento2.set(new Vector3f(0f, posY, 0));
        TransformGroup TGesferaFija = new TransformGroup(desplazamiento2);
        TGesferaFija.addChild(figuraVisual);
        objRoot.addChild(TGesferaFija);

        // parte física del objeto:
        float masa = 0f;                                                       //con masa =0 el objeto es est‡tico
        SphereShape figuraFisica = new SphereShape(radio);
        CollisionObject ramaFisica = new CollisionObject();
        ramaFisica.setCollisionShape(figuraFisica);
        Transform groundTransform = new Transform();
        groundTransform.setIdentity();
        groundTransform.origin.set(new Vector3f(0, posY, 0));
        Vector3f inerciaLocal = new Vector3f(0, 0, 0);
        DefaultMotionState EstadoDeMovimiento = new DefaultMotionState(groundTransform);
        RigidBodyConstructionInfo InformacionCuerpoR = new RigidBodyConstructionInfo(masa, EstadoDeMovimiento, figuraFisica, inerciaLocal);
        RigidBody cuerpoRigido = new RigidBody(InformacionCuerpoR);
        cuerpoRigido.setActivationState(RigidBody.DISABLE_DEACTIVATION);
        mundoFisico.addRigidBody(cuerpoRigido); // add the body to the dynamics world

        //Para crear objeto que se sometan a fisica, su masa debe seo >0 e invocar continuamente
        // mundoFisico.stepSimulation(dt) y actualizar su objeto java3d a partir de su rigidBody.
        //Mejor, usar la clase Figura simulada con el codigo del run(), mostrar() y actualizar()
        return objRoot;
    }

    public void cargarContenido() {
        //Creando el personaje del juego, controlado por teclado. Tambien se pudo haber creado en CrearEscena()
        float masa = 1f;
        float radio = 1f;
        float posX = 5f;
        float posY = 5f, posZ = 0f;
        float elasticidad = 0.5f;
        float dampingLineal = 0.5f;
        float dampingAngular = 0.9f;
        personaje = new EsferaMDL("objetosMDL/Iron_Golem.mdl", radio, conjunto, listaObjetosFisicos, this, true);
        personaje.crearPropiedades(masa, elasticidad, 0.5f, posX, posY, posZ, mundoFisico);
        personaje.cuerpoRigido.setDamping(dampingLineal, dampingAngular);

        //Creando un Agente (es decir, un personaje aut—nomo) con el objetivo de perseguir al personaje controlado por teclado
        perseguidor = new Esfera(radio, "texturas//bosques2.jpg", conjunto, listaObjetosFisicos, this);
        if (!actualizandoFisicas) {
            perseguidor.crearPropiedades(masa, elasticidad, dampingLineal, 20, 4, -15, mundoFisico);
        }
        perseguidor.asignarObjetivo(personaje, 15f);   //Este objetivo de perseguir DEBE actualizado para que persiga la nueva posicion del personaje

        //Creacion de un Terreno Simple (no es una figura, no es movil, tiene masa 0)
        float friccion = 0.5f;
        utilidades.TerrenoSimple terreno = new utilidades.TerrenoSimple(30, 30, -5, -3f, -12, "unaTextura_Desabilitada", conjunto, mundoFisico, friccion);
    }

    public void actualizar(float dt) {

        perseguidor.asignarObjetivo(personaje, 150f);

        //ACTUALIZAR DATOS DE FUERZAS DEL PERSONAJE CONTROLADO POR EL JUGADOR
        if (personaje != null) {
            float fuerzaHaciaAdelante = 0, fuerzaLateral = 0;
            if (personaje.adelante) {
                fuerzaHaciaAdelante = personaje.masa * 100f * 2.5f;
            }
            if (personaje.atras) {
                fuerzaHaciaAdelante = -personaje.masa * 100f * 2.5f;
            }
            if (personaje.derecha) {
                fuerzaLateral = -personaje.masa * 40f;
            }
            if (personaje.izquierda) {
                fuerzaLateral = personaje.masa * 40f;
            }

            Vector3d direccionFrente = personaje.conseguirDireccionFrontal();
            personaje.cuerpoRigido.applyCentralForce(new Vector3f((float) direccionFrente.x * fuerzaHaciaAdelante * 0.1f, 0, (float) direccionFrente.z * fuerzaHaciaAdelante * 0.1f));
            personaje.cuerpoRigido.applyTorque(new Vector3f(0, fuerzaLateral, 0));
        }

        //ACTUALIZAR DATOS DE FUERZAS DE LAS FIGURAS AUTONOMAS  (ej. para que cada figura pueda persiguir su objetivo)
        for (int i = 0; i < this.listaObjetosFisicos.size(); i++) {
            listaObjetosFisicos.get(i).actualizar();
        }

        //ACTUALIZAR DATOS DE LOCALIZACION DE FIGURAS FISICAS
        this.actualizandoFisicas = true;
        try {
            mundoFisico.stepSimulation(dt);    //mundoFisico.stepSimulation ( dt  ,50000, dt*0.2f);
        } catch (Exception e) {
            System.out.println("JBullet forzado. No debe crearPropiedades de solidoRigidos durante la actualizacion stepSimulation");
        }
        this.actualizandoFisicas = false;
        tiempoJuego = tiempoJuego + dt;
    }

    public void mostrar() throws Exception {
        //MOSTRAR FIGURAS FISICAS (muestra el componente visual de la figura, con base en los datos de localizacion del componente fisico)
        this.mostrandoFisicas = true;
        try {
            if ((mundoFisico.getCollisionObjectArray().size() != 0)
                    && (listaObjetosFisicos.size() != 0)) {
                for (int idFigura = 0; idFigura <= this.listaObjetosFisicos.size() - 1; idFigura++) {     // Actualizar posiciones fisicas y graficas de los objetos.
                    try {
                        int idFisico = listaObjetosFisicos.get(idFigura).identificadorFisico;
                        CollisionObject objeto = mundoFisico.getCollisionObjectArray().get(idFisico); //
                        RigidBody cuerpoRigido = RigidBody.upcast(objeto);
                        listaObjetosFisicos.get(idFigura).mostrar(cuerpoRigido);
                    } catch (Exception e) {
                        // No hacer nada
                    }
                }
            }
        } catch (Exception e) {
            // No hacer nada
        }
        this.mostrandoFisicas = false;
    }

    public void empezar() {

        /* Bucle principal del juego */
        while (!peticionDeCerrar) {
            actualizar(dt);
            try {
                mostrar();
            } catch (Exception ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }

            camaraAlJugador();

            esperar();
        }

    }

    public void esperar() {
        try {
            Thread.sleep(tiempoDeEspera);
        } catch (InterruptedException e) {
            // No hacer nada
        }
    }

    public void camaraAlJugador() {

        /* Calcular posición y dirección */
        Transform3D t3dPersonaje = new Transform3D();
        personaje.desplazamientoFigura.getTransform(t3dPersonaje);
        Vector3f posPersonaje = new Vector3f(0, 0, 0);
        t3dPersonaje.get(posPersonaje);

        double[] c = new double[]{posPersonaje.x, posPersonaje.y, posPersonaje.z};
        Vector3d dir = personaje.conseguirDireccionFrontal();
        dir.scale(1.5);
        Point3d posicionCamara = new Point3d(c[0] - dir.x, c[1] + 4, c[2] - dir.z);

        /* Colocar en la espalda encima del jugador */
        colocarCamara(universo, posicionCamara, new Point3d(c[0]+ dir.x, c[1], c[2] + dir.z));

    }

    public void colocarCamara(SimpleUniverse universo, Point3d posicionCamara, Point3d objetivoCamara) {
        posicionCamara = new Point3d(posicionCamara.x + 0.001, posicionCamara.y + 0.001d, posicionCamara.z + 0.001);
        Transform3D datosConfiguracionCamara = new Transform3D();
        datosConfiguracionCamara.lookAt(posicionCamara, objetivoCamara, new Vector3d(0.001, 1.001, 0.001));
        try {
            datosConfiguracionCamara.invert();
            TransformGroup TGcamara = universo.getViewingPlatform().getViewPlatformTransform();
            TGcamara.setTransform(datosConfiguracionCamara);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        Juego juego = new Juego();

        juego.cargarContenido();

        /* Frame */
        juego.setTitle("Juego");
        juego.setSize(1000, 800);
        juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juego.setVisible(true);

        /* Bucle del juego */
        juego.empezar();
    }
}
