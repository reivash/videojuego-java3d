package main;

import entidad.Personaje;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.dynamics.*;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import entidad.DiccionarioEntidades;
import entidad.FactoriaEntidades;
import entrada.Teclado;
import eventos.Evento;
import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import mundo.CreadorDeEstructuras;
import mundo.TerrenoSimple;
import util.Camara;
import util.Sonido;

public class Juego extends JFrame {

    /* Java3D y física */
    private SimpleUniverse universo;
    private BranchGroup conjunto = new BranchGroup();
    private DiscreteDynamicsWorld mundoFisico;
    private float tiempoJuego;
    private BoundingSphere limitesBackground = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);

    private Camara camara = null;

    /* Entidades */
    private DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();
    private Personaje jugador;

    /* Entrada por teclado */
    private Teclado teclado;

    /* Tiempo de espera del loop */
    private float dt = 3f / 100f;
    private int tiempoDeEspera = (int) (dt * 1000);

    private boolean peticionDeCierre = false;

    private boolean partidaAcabada = false;

    /* Texto3D */
    private BranchGroup tituloBG = null;
    private Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 2), new FontExtrusion());
    private Text3D textGeom;

    /* Constructor */
    public Juego() {
        inicializarJBullet();
        inicializarJava3D();
        new Thread() {
            public void run() {
                inicializarTexto3D();
            }
        }.start();
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
        JuegoCanvas zonaDibujo = new JuegoCanvas(SimpleUniverse.getPreferredConfiguration(), this);
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

    private void inicializarTexto3D() {
        tituloBG = new BranchGroup();

        textGeom = new Text3D(font3d, "", new Point3f(0, 4, -5f));
        textGeom.setAlignment(Text3D.ALIGN_CENTER);
        Shape3D textShape = new Shape3D(textGeom);

        Appearance apariencia = new Appearance();
        Texture tex = new TextureLoader("res/texturas/balon.jpg", null).getTexture();
        apariencia.setTexture(tex);
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);

        textShape.setAppearance(apariencia);

        TransformGroup tituloTG = new TransformGroup();
        Transform3D tituloT3D = new Transform3D();
        tituloT3D.rotY(Math.PI);
        tituloTG.setTransform(tituloT3D);

        tituloTG.addChild(textShape);
        tituloBG.addChild(tituloTG);
    }

    public BranchGroup crearEscena() {
        BranchGroup objRoot = new BranchGroup();
        conjunto = new BranchGroup();
        objRoot.addChild(conjunto);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        conjunto.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        conjunto.setCapability(BranchGroup.ALLOW_DETACH);

        /* Iluminación */
        DirectionalLight LuzDireccional = new DirectionalLight(new Color3f(10f, 10f, 10f),
                new Vector3f(1f, 0f, -1f));
        BoundingSphere limitesLuz = new BoundingSphere(new Point3d(-15, 10, 15), 10000.0); //Localizacion de fuente/paso de luz
        objRoot.addChild(LuzDireccional);
        LuzDireccional.setInfluencingBounds(limitesLuz);
        Background bg = new Background();
        bg.setApplicationBounds(limitesBackground);
        bg.setColor(new Color3f(135f / 256, 206f / 256f, 250f / 256f));
        objRoot.addChild(bg);

//        //Es sencillo crearlos estaticos como se muestra a continuacion. Si desea que caigan, y se sometan a fuerzas, mejor crear una figura.
//        float radio = 2f;
//        float posY = -4f;
//
//        // Creación de un objeto (la piedra con textura de ladrillo)
//        // Componente gráfico
//        Appearance apariencia = new Appearance();
//        apariencia.setTexture(new TextureLoader(System.getProperty("user.dir") + "//res//texturas//ladrillo.jpg", this).getTexture());
//        TextureAttributes texAttr = new TextureAttributes();
//        texAttr.setTextureMode(TextureAttributes.MODULATE);
//        apariencia.setTextureAttributes(texAttr);
//        Sphere figuraVisual = new Sphere(radio, Sphere.GENERATE_TEXTURE_COORDS, 60, apariencia);
//        Transform3D desplazamiento2 = new Transform3D();
//        desplazamiento2.set(new Vector3f(0f, posY, 0));
//        TransformGroup TGesferaFija = new TransformGroup(desplazamiento2);
//        TGesferaFija.addChild(figuraVisual);
//        objRoot.addChild(TGesferaFija);
//
//        // Componente físico
//        float masa = 0f;                                                       //con masa =0 el objeto es estático
//        SphereShape figuraFisica = new SphereShape(radio);
//        CollisionObject ramaFisica = new CollisionObject();
//        ramaFisica.setCollisionShape(figuraFisica);
//        Transform groundTransform = new Transform();
//        groundTransform.setIdentity();
//        groundTransform.origin.set(new Vector3f(0, posY, 0));
//        Vector3f inerciaLocal = new Vector3f(0, 0, 0);
//        DefaultMotionState EstadoDeMovimiento = new DefaultMotionState(groundTransform);
//        RigidBodyConstructionInfo InformacionCuerpoR = new RigidBodyConstructionInfo(masa, EstadoDeMovimiento, figuraFisica, inerciaLocal);
//        RigidBody cuerpoRigido = new RigidBody(InformacionCuerpoR);
//        cuerpoRigido.setActivationState(RigidBody.DISABLE_DEACTIVATION);
//        mundoFisico.addRigidBody(cuerpoRigido); // add the body to the dynamics world
//
//        //Para crear objeto que se sometan a fisica, su masa debe seo >0 e invocar continuamente
//        // mundoFisico.stepSimulation(dt) y actualizar su objeto java3d a partir de su rigidBody.
//        //Mejor, usar la clase Figura simulada con el codigo del run(), mostrar() y actualizar()
        return objRoot;
    }

    public void cargarContenido() {

        //Creando el personaje del juego, controlado por teclado. Tambien se pudo haber creado en CrearEscena()
        float masa = 1f;
        float radio = 1f;
        float posX = 5f;
        float posY = 5f, posZ = -50f;
        float elasticidad = 0.5f;
        float dampingLineal = 0.5f;
        float dampingAngular = 0.9999998f;
        jugador = new Personaje("objetosMDL/Iron_Golem.mdl", radio, conjunto, this, true);
        jugador.añadirTipo("JUGADOR");
        jugador.crearPropiedades(masa, elasticidad, 0.1f, new Vector3f(posX, posY, posZ), new Vector3f());
        jugador.cuerpoRigido.setDamping(dampingLineal, dampingAngular); //ToDo: eliminar acceso directo
        teclado.setJugador(jugador);
        jugador.cuerpoRigido.setFriction(0.6f);
        jugador.setVida(5000);
//        System.out.println("Jugador: " + jugador);
        
        /* Sonido */
        Sonido.init(conjunto, universo);
        
//        //Creando un Agente (es decir, un personaje aut—nomo) con el objetivo de perseguir al personaje controlado por teclado
//        float fuerza_muscular = 20f;
//        EntidadPerseguidora perseguidor;
//
//        /* Crea todas las que quieras cambiando el número */
//        for (int i = 0; i < 1; i++) {
//            if (i % 2 == 0) {
//                perseguidor = new EntidadPerseguidora(radio, "res//texturas//balon.jpg", conjunto, this);
//            } else {
//                perseguidor = new EntidadPerseguidora(radio, "res//texturas//hielo.jpg", conjunto, this);
//            }
//
//            perseguidor.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(20, 4, -15), new Vector3f());
//            perseguidor.asignarObjetivo(jugador, fuerza_muscular);   //Este objetivo de perseguir DEBE actualizado para que persiga la nueva posicion del personaje
//            diccionarioEntidades.añadirEntidadFisica(perseguidor);
//        }

        /* NPCs */
//        FactoriaEntidades.crearEntidad("perroListo", conjunto, this);
//        FactoriaEntidades.crearEntidad("perroPerseguidor", conjunto, this);
        FactoriaEntidades.crearEntidad("jauria", conjunto, this);
//        FactoriaEntidades.crearEntidad("tiraBolas", conjunto, this);

        /* CASTILLO */
        /* Torres del castillo traseras */
        float radioTorre = 12f;
        int nivelesTorre = 5;
        int numPiezasNivel = 8;
//        CreadorDeEstructuras.crearTorre(new Vector3f(0, 0f, 400), radioTorre, nivelesTorre, numPiezasNivel, conjunto, this);
//        CreadorDeEstructuras.crearTorre(new Vector3f(-200, 0f, 400), radioTorre, nivelesTorre, numPiezasNivel, conjunto, this);
//        CreadorDeEstructuras.crearTorre(new Vector3f(+200, 0f, 400), radioTorre, nivelesTorre, numPiezasNivel, conjunto, this);
//
//        /* Torres del castillo delanteras */
        CreadorDeEstructuras.crearTorre(new Vector3f(-100, 0f, 150), radioTorre, nivelesTorre, numPiezasNivel, conjunto, this);
        CreadorDeEstructuras.crearTorre(new Vector3f(+100, 0f, 150), radioTorre, nivelesTorre, numPiezasNivel, conjunto, this);
//
//        /* Muros traseros */
        float alturaMuros = 15f;
        int numPiezas = 10;
        float offset = 15f; // Ayuda a posicionar los muros de forma que no choquen con las torres y las tiren
//        CreadorDeEstructuras.crearMuro(new Vector3f(-200 + radioTorre / 2 + offset*1.05f, 0, 400), new Vector3f(-radioTorre / 2 - offset*1.1f, 0, 400), alturaMuros, numPiezas, conjunto, this);
//        CreadorDeEstructuras.crearMuro(new Vector3f(200 - radioTorre / 2 - offset*1.05f, 0, 400), new Vector3f(radioTorre / 2 + offset*1.1f, 0, 400), alturaMuros, numPiezas, conjunto, this);
//
//        /* Muros delanteros */
        CreadorDeEstructuras.crearMuro(new Vector3f(-100 + radioTorre / 2 + offset, 0f, 150), new Vector3f(-15, 0, 150), alturaMuros, numPiezas / 2, conjunto, this);
        CreadorDeEstructuras.crearMuro(new Vector3f(+100 - radioTorre / 2 - offset, 0f, 150), new Vector3f(15, 0, 150), alturaMuros, numPiezas / 2, conjunto, this);
//
//        /* Muros laterales */
//        CreadorDeEstructuras.crearMuro(new Vector3f(-200, 0, 400 - radioTorre / 2 - offset * 1.4f), new Vector3f(-100, 0f, 150 + radioTorre / 2 + offset * 1.4f), alturaMuros, numPiezas, conjunto, this);
//        CreadorDeEstructuras.crearMuro(new Vector3f(200, 0, 400 - radioTorre / 2 - offset * 1.4f), new Vector3f(100 , 0f, 150 + radioTorre / 2 + offset * 1.4f), alturaMuros, numPiezas, conjunto, this);

        /* Prueba de bola */
//        entidad.Bola b = new entidad.Bola(1, 64, new Vector3f(-750000,500,0), "res//texturas//bola.jpg", conjunto, this);
//        b.crearPropiedades(100, 0, dampingLineal, new Vector3f(0,0,1), new Vector3f());


        /* Test de rotación de muros */
//        for (int i = 0; i < 8; i++) {
//            CreadorDeEstructuras.crearMuro(new Vector3f(-30f + i*10, 0f, 20f), new Vector3f(-300f + i*100f, 0f, 400f), 10, 5, conjunto, this);
//        }
//        for (int i = 0; i < 8; i++) {
//            CreadorDeEstructuras.crearMuro(new Vector3f(-30f + i*10, 0f, -20f), new Vector3f(-300f + i*100f, 0f, -400f), 10, 5, conjunto, this);
//        }
        /* Bloque paralelo al eje X */
//        CreadorDeEstructuras.crearBloque(new Vector3f(), new Vector3f(20, 1, 5), new Vector3f(), conjunto, this);
        // Creación de un Terreno Simple (no es una figura, no es movil, tiene masa 0)
        float friccion = 4f;
        mundo.TerrenoSimple terreno = new TerrenoSimple(2000, 2000, -1000, -3f, -1000, "res//texturas//cespedfutbol.jpg", conjunto, mundoFisico, friccion);

        /* Disparador */
//        Evento evento = new Evento();
//        evento.setComando("VICTORIA");
//        ArrayList<String> objetivos = new ArrayList<String>();
//        objetivos.add("JUGADOR");
//        Disparador d = new DisparadorRectangular(objetivos, this, evento, 50, 50, -25, -25);
    }

    /* Getters y setters */
    public DiscreteDynamicsWorld getMundoFisico() {
        return mundoFisico;
    }

    public Teclado getTeclado() {
        return teclado;
    }

    public Personaje getJugador() {
        return jugador;
    }

    public void añadirLineaAlChat(String linea) {
        ((JuegoCanvas) universo.getCanvas()).addLineToChat(linea);
    }

    public void procesarEvento(Evento e) {
        switch (e.getComando()) {

            case "VICTORIA":
                if (!partidaAcabada) {
                    añadirTitulo("VICTORIA");
                    partidaAcabada = true;
                }
                break;
            case "GAME_OVER":
                if (!partidaAcabada) {
                    añadirTitulo("GAME OVER");
                    partidaAcabada = true;
                }
                break;
        }
    }

    public void añadirTitulo(String titulo) {
        textGeom.setString(titulo);
        jugador.desplazamiento.addChild(tituloBG);
    }

    public void actualizar(float dt) {
        diccionarioEntidades.eliminarEncolados();
        diccionarioEntidades.creaEncolados();
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

//            System.out.println("Hostiles al jugador: " + diccionarioEntidades.getEntidadesHostiles(jugador));
//            System.out.println("Hostiles al perro: " + diccionarioEntidades.getEntidadesHostiles(
//                    diccionarioEntidades.getEntidad(2)));
//            System.out.println("Jugador: " + jugador);
            esperar();
        }
    }

    public static void main(String[] args) {

        /* Inicialización */
        Juego juego = new Juego();
        juego.cargarContenido();

        /* Frame */
        juego.setTitle("Juego");
        juego.setSize(1024, 768);
        juego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juego.setVisible(true);

        /* Bucle del juego */
        juego.empezar();
    }

}
