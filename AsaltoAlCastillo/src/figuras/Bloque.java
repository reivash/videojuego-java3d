package figuras;

import main.Juego;
import com.bulletphysics.collision.dispatch.*;
import com.bulletphysics.collision.shapes.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import entidad.Entidad;
import javax.media.j3d.*;
import javax.vecmath.*;
import net.sf.nwn.loader.AnimationBehavior;

public class Bloque extends Entidad {

    public Scene escenaPersonaje1;
    AnimationBehavior ab = null;
    String nombreAnimacionCorriendo, nombreAnimacionCaminando, nombreAnimacionQuieto;
    Vector3d direccion = new Vector3d(0, 0, 10);
    float alturaP, alturaDeOjos;
    Cylinder figuraLimintesFisicos;
    boolean esPersonaje;

    public Bloque(
            Vector3f tamaños,
            String textura,
            BranchGroup conjunto,
            Juego juego) {

        // Si se desea programar una clase Esfera, su constrctor tendr’a esta linea
        super(juego, conjunto);

        // Creando una apariencia
        // Creacion de formas visuales y fisicas
        Appearance ap = new Appearance();
        Texture tex = new TextureLoader(textura, null).getTexture();
        ap.setTexture(tex);
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        ap.setTextureAttributes(texAttr);

        Box figuraVisual = new Box(tamaños.x, tamaños.y, tamaños.z, Sphere.GENERATE_TEXTURE_COORDS, ap);
        BoxShape figuraFisica = new BoxShape(tamaños);
        ramaFisica = new CollisionObject();
        ramaFisica.setCollisionShape(figuraFisica);
        ramaVisible.addChild(desplazamiento);
        desplazamiento.addChild(figuraVisual);
        this.branchGroup = conjunto;
    }

}
