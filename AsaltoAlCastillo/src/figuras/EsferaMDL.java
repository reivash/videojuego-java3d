package figuras;

import main.Juego;
import util.CapabilitiesMDL;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.*;
import com.sun.j3d.loaders.Scene;
import entidad.EntidadFisica;
import eventos.Evento;
import java.net.URL;
import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.*;
import net.sf.nwn.loader.AnimationBehavior;
import net.sf.nwn.loader.NWNLoader;
import static util.Maths.distancia;

public class EsferaMDL extends EntidadFisica {

    public Scene escenaPersonaje1;
    public AnimationBehavior ab = null;
    public String nombreAnimacionCorriendo, nombreAnimacionCaminando, nombreAnimacionQuieto, nombreAnimacionLuchando;
    Vector3d direccion = new Vector3d(0, 0, 10);
    public float radio, alturaP, alturaDeOjos;
    boolean esPersonaje;

    /* Control de animaciones */
    private float velocidad_giro = 50f;
    private float velocidad_movimiento = 100;


    private String animacionActual = "";
    private boolean accionRealizada = false;

    public EsferaMDL(String ficheroMDL,
            float radio,
            BranchGroup conjunto,
            Juego juego,
            boolean esPersonaje) {

        super(juego, conjunto);
        this.esPersonaje = esPersonaje;
        //Creando una apariencia
        Appearance apariencia = new Appearance();
        this.radio = radio;
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);
        esMDL = true;
        //Creacion de la forma visual MDL
        //nombre = "figura_MDL_" + identificador;
        //Sphere figuraVisual = new Sphere(radio);
        TransformGroup figuraVisual = crearObjetoMDL(ficheroMDL, radio * 2);
        SphereShape figuraFisica = new SphereShape(radio);
        ramaFisica = new CollisionObject();
        ramaFisica.setCollisionShape(figuraFisica);
        ramaVisible.addChild(desplazamiento);
        desplazamiento.addChild(figuraVisual);
    }

    public TransformGroup crearObjetoMDL(String archivo, float multiplicadorEscala) {
        BranchGroup RamaMDL = new BranchGroup();
        float rotacionX = 0;
        float rotacionY = 0;
        float rotacionZ = 0;
        float escalaTamano = 1f;
        float desplazamientoY = 0;
        try {
            NWNLoader nwn2 = new NWNLoader();
            nwn2.enableModelCache(true);
            escenaPersonaje1 = nwn2.load(new URL("file://localhost/" + System.getProperty("user.dir") + "/" + archivo));
            RamaMDL = escenaPersonaje1.getSceneGroup();
            //Recorrido por los- objetos para darle capacidades a sus Shapes3D
            CapabilitiesMDL.setCapabilities(RamaMDL, this.identificadorFigura);
            //Para cada Objeto MDL dar nombre las animaciones de la figura. Dar rotaciones a la figuraMDL (suelen venir giradas)
            ab = (AnimationBehavior) escenaPersonaje1.getNamedObjects().get("AnimationBehavior");
            if (archivo.equals("objetosMDL/Iron_Golem.mdl")) {
                nombreAnimacionCorriendo = "iron_golem:cwalk";
                nombreAnimacionCaminando = "iron_golem:cwalk";
                nombreAnimacionQuieto = "iron_golem:cpause1";
                nombreAnimacionLuchando = "iron_golem:ca1slashl";
                rotacionX = -1.5f;
                rotacionZ = 3.14f;
                escalaTamano = 0.65f;
                desplazamientoY = -1f;
                alturaP = (float) 3f * escalaTamano;
                alturaDeOjos = alturaP;
            }
            if (archivo.equals("objetosMDL/Doomknight.mdl")) {
                nombreAnimacionCaminando = "Doomknight:crun";
                nombreAnimacionCaminando = "Doomknight:cwalk";
                nombreAnimacionQuieto = "Doomknight:cpause1";
                rotacionX = -1.5f;
                rotacionZ = 3.14f;
                alturaP = 2f;
                escalaTamano = 0.8f;
                alturaDeOjos = 1.5f * escalaTamano;
                desplazamientoY = -1f;
            }
            if (archivo.equals("objetosMDL/Dire_Cat.mdl")) {
                nombreAnimacionCaminando = "dire_cat:crun";
                nombreAnimacionCaminando = "dire_cat:cwalk";
                nombreAnimacionQuieto = "dire_cat:cpause1";
                rotacionX = -1.5f;
                rotacionZ = 3.14f;
                alturaP = 2f;
                escalaTamano = 1f;
                alturaDeOjos = alturaP * escalaTamano;
            }
            if (archivo.equals("objetosMDL/pixie.mdl")) {
                nombreAnimacionCaminando = "pixie:crun";
                nombreAnimacionCaminando = "pixie:cwalk";
                nombreAnimacionQuieto = "pixie:cpause1";
                rotacionX = -1.5f;
                rotacionZ = 3.14f;
                alturaP = 1f;
                escalaTamano = 6.0f;
                desplazamientoY = -6.5f;
                alturaDeOjos = alturaP * escalaTamano;
            }
            if (archivo.equals("objetosMDL/Intellect_Devour.mdl")){
                nombreAnimacionCaminando = "intellect_devour:crun";
                nombreAnimacionCaminando = "intellect_devour:cwalk";
                nombreAnimacionQuieto = "intellect_devour:cpause1";
                nombreAnimacionLuchando = "intellect_devour:ca1slashl";
                
                rotacionX = -1.5f;
                rotacionZ = 3.14f;
                alturaP = 1f;
                escalaTamano = 6.0f;
                desplazamientoY = -6.5f;
                alturaDeOjos = alturaP * escalaTamano;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error during load Dire_Cat.mdl");
        }

        //Ajustando rotacion inicial de la figura MLD y aplicando tamano
        Transform3D rotacionCombinada = new Transform3D();
        rotacionCombinada.set(new Vector3f(0, desplazamientoY, 0));
        Transform3D correcionTemp = new Transform3D();
        correcionTemp.rotX(rotacionX);
        rotacionCombinada.mul(correcionTemp);
        correcionTemp.rotZ(rotacionZ);
        rotacionCombinada.mul(correcionTemp);
        correcionTemp.rotY(rotacionY);
        rotacionCombinada.mul(correcionTemp);
        correcionTemp.setScale(escalaTamano * multiplicadorEscala);
        rotacionCombinada.mul(correcionTemp);
        TransformGroup rotadorDeFIguraMDL = new TransformGroup(rotacionCombinada);
        rotadorDeFIguraMDL.addChild(RamaMDL);
        return rotadorDeFIguraMDL;
    }

    public void realizarAccion(Evento e) {

        accionRealizada = true;

        /* Velocidad por defecto de la animación */
        ab.setAnimationTimeScale(.5f);

        try {
            String command = e.getCommando();
            ArrayList<String> params = e.getParams();
            switch (command) {
                case "mover": {
                    String option = params.get(0);
                    switch (option) {
                        case "adelante":
                            log("Ir adelante");
                            if (!animacionActual.equals(nombreAnimacionCaminando)) {
                                ab.playAnimation(nombreAnimacionCaminando, true);
                                ab.setAnimationTimeScale(.5f);
                                animacionActual = nombreAnimacionCaminando;
                            }
                            velocidad_lineal.x += velocidad_movimiento;
                            break;
                        case "atras":
                            if (!animacionActual.equals(nombreAnimacionCaminando)) {
                                ab.playAnimation(nombreAnimacionCaminando, true);
                                ab.setAnimationTimeScale(.5f);
                                animacionActual = nombreAnimacionCaminando;
                            }
                            log("Ir atras");
                            velocidad_lineal.x -= velocidad_movimiento;
                            break;
                    }
                    break;
                }
                case "girar": {
                    String option = params.get(0);
                    switch (option) {
                        case "izquierda":
                            log("Girar izquierda");
                            velocidad_angular.y += velocidad_giro;
                            break;
                        case "derecha":
                            log("Girar derecha");
                            velocidad_angular.y -= velocidad_giro;
                    }
                    break;
                }
                case "atacar": {
                    log("Atacando");
                    if (!animacionActual.equals(nombreAnimacionLuchando)) {
                        ab.playAnimation(nombreAnimacionLuchando, true);
                        ab.setAnimationTimeScale(.5f);
                        animacionActual = nombreAnimacionLuchando;
                    }
                    for (EntidadFisica ef : diccionarioEntidades.getEntidadesFisicas()) {
                        /* ToDo: Reparar esto que no funciona. El método de colisión siempre da true */
                        if (!getId().equals(ef.getId()) && distancia(posiciones, ef.posiciones) < 10) {
                            ef.velocidad_lineal.y += 500;
//                             System.out.println("este: " + cuerpoRigido.toString() + "\nOtro: " + ef.cuerpoRigido.toString() + "\n");
                        }
                    }
                    break;
                }
                /* Porque puedo */
                case "volar": {
                    log("Volando");
                    velocidad_lineal.y += velocidad_movimiento;
                    break;
                }
            }
        } catch (Exception ex) {
            log("Error al procesar evento");
        }
    }


    @Override
    public void actualizar() {
        super.actualizar();

        /* Si no estamos haciendo nada ponemos la animacion por defecto */
        if (!accionRealizada && !animacionActual.equals(nombreAnimacionQuieto)) {
            log("Animacion quieto");
            ab.playAnimation(nombreAnimacionQuieto, true);
            animacionActual = nombreAnimacionQuieto;
        }
        accionRealizada = false;
    }
}
