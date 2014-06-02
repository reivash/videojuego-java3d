package main;

import figuras.EntidadPerseguidora;
import java.awt.*;
import javax.swing.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import entidad.DiccionarioEntidades;
import entrada.Teclado;
import figuras.Esfera;
import figuras.EsferaMDL;
import util.Camara;

public class Juego extends JFrame {

    /* Java3D y física */
    private SimpleUniverse universo;
    private BranchGroup conjunto = new BranchGroup();
    private DiscreteDynamicsWorld mundoFisico;
    private float tiempoJuego;
    private BoundingSphere limitesBackground = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

    private Camara camara = null;

    /* Entidades */
    private DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();
    private EsferaMDL jugador;

    /* Entrada por teclado */
    private Teclado teclado;
    
    /* Tiempo de espera del loop */
    private float dt = 3f / 100f;
    private int tiempoDeEspera = (int) (dt * 1000);

    private boolean peticionDeCierre = false;

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
        mundoFisico.setGravity(new Vector3f(0, -100, 0));
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
        universo.getViewer().getView().setBackClipDistance(10000);
        camara = new Camara(universo);
        
        teclado = new Teclado(conjunto, "teclado.txt");
        
    }

    public BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        conjunto = new BranchGroup();
        objRoot.addChild(conjunto);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        DirectionalLight LuzDireccional = new DirectionalLight(new Color3f(10f, 10f, 10f),
                new Vector3f(1f, 0f, -1f));
        BoundingSphere limitesLuz = new BoundingSphere(new Point3d(-15, 10, 15), 100.0); //Localizacion de fuente/paso de luz
        objRoot.addChild(LuzDireccional);
        LuzDireccional.setInfluencingBounds(limitesLuz);
        Background bg = new Background();
        bg.setApplicationBounds(limitesBackground);
        bg.setColor(new Color3f(135f / 256, 206f / 256f, 250f / 256f));
        objRoot.addChild(bg);

        //Es sencillo crearlos estaticos como se muestra a continuacion. Si desea que caigan, y se sometan a fuerzas, mejor crear una figura.
        float radio = 2f;
        float posY = -4f;

        // Creación de un objeto (la piedra con textura de ladrillo)
        // Componente gráfico
        Appearance apariencia = new Appearance();
        apariencia.setTexture(new TextureLoader(System.getProperty("user.dir") + "//res//texturas//ladrillo.jpg", this).getTexture());
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);
        Sphere figuraVisual = new Sphere(radio, Sphere.GENERATE_TEXTURE_COORDS, 60, apariencia);
        Transform3D desplazamiento2 = new Transform3D();
        desplazamiento2.set(new Vector3f(0f, posY, 0));
        TransformGroup TGesferaFija = new TransformGroup(desplazamiento2);
        TGesferaFija.addChild(figuraVisual);
        objRoot.addChild(TGesferaFija);

        // Componente físico
        float masa = 0f;                                                       //con masa =0 el objeto es estático
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

    /* Getters y setters */
    public DiscreteDynamicsWorld getMundoFisico() {
        return mundoFisico;
    }

    public Teclado getTeclado(){
        return teclado;
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
        jugador = new EsferaMDL("objetosMDL/Iron_Golem.mdl", radio, conjunto, this, true);
        jugador.crearPropiedades(masa, elasticidad, 0.1f, posX, posY, posZ);
        jugador.cuerpoRigido.setDamping(dampingLineal, dampingAngular); //ToDo: eliminar acceso directo
        teclado.setJugador(jugador);
        
        //Creando un Agente (es decir, un personaje aut—nomo) con el objetivo de perseguir al personaje controlado por teclado
        float fuerza_muscular = 20f;
        EntidadPerseguidora perseguidor;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                perseguidor = new EntidadPerseguidora(radio, "res//texturas//balon.jpg", conjunto, this);
            } else {
                perseguidor = new EntidadPerseguidora(radio, "res//texturas//hielo.jpg", conjunto, this);
            }

            perseguidor.crearPropiedades(masa, elasticidad, dampingLineal, 20, 4, -15);
            perseguidor.asignarObjetivo(jugador, fuerza_muscular);   //Este objetivo de perseguir DEBE actualizado para que persiga la nueva posicion del personaje
            diccionarioEntidades.añadirEntidadFisica(perseguidor);
        }

        // Creación de un Terreno Simple (no es una figura, no es movil, tiene masa 0)
        float friccion = 4f;
        terreno.TerrenoSimple terreno = new terreno.TerrenoSimple(100, 100, -50, -3f, -50, "res//texturas//cespedfutbol.jpg", conjunto, mundoFisico, friccion);
    }

    public void actualizar(float dt) {

        diccionarioEntidades.actualizar();

        /* Actualizar la física del mundo */
        try {
            mundoFisico.stepSimulation(dt);
        } catch (Exception e) {
            System.out.println("JBullet forzado. No debe crearPropiedades de solidoRigidos durante la actualizacion stepSimulation");
        }

        tiempoJuego = tiempoJuego + dt;
    }

    /* Actualiza la posición visual en base a la física */
    public void mostrar() {
        diccionarioEntidades.mostrar();
    }

    public void esperar() {
        try {
            Thread.sleep(tiempoDeEspera);
        } catch (InterruptedException e) {
            // Nada
        }
    }

    public void empezar() {

        /* Bucle principal del juego */
        while (!peticionDeCierre) {

            teclado.actualizar();
            
            actualizar(dt);
            mostrar();

            camara.camaraAlPersonaje(jugador);

            esperar();
        }
    }

    public static void main(String[] args) {

        /* Inicialización */
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
