package disparadores;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import entidad.DiccionarioEntidades;
import entidad.Entidad;
import eventos.Evento;
import java.util.List;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import main.Juego;

/* 
 Cuando la entidad con las etiquetas indicadas entre en este rectángulo contenido en el plano del suelo se lanzará el evento 
 */
public class DisparadorRectangular extends DisparadorAbstracto {

    private DiccionarioEntidades dE = DiccionarioEntidades.getInstance();

    private float x1, z1, x2, z2;

    private Juego juego = null;
    private Evento e = null;

    public DisparadorRectangular(List<String> objetivos, Juego juego, Evento e, float x1, float z1, float x2, float z2, BranchGroup bg) {
        super(objetivos);
        this.juego = juego;
        this.e = e;

        /* x1 siempre será el mínimo de los dos x. z1 siempre será el mínimo de los dos z */
        if (x1 < x2) {
            this.x1 = x1;
            this.x2 = x2;
        } else {
            this.x1 = x2;
            this.x2 = x1;
        }
        if (z1 < z2) {
            this.z1 = z1;
            this.z2 = z2;
        } else {
            this.z1 = z2;
            this.z2 = z1;
        }
        if (bg != null) {
            BranchGroup boundsBG = new BranchGroup();
            Appearance ap = new Appearance();
            Texture tex = new TextureLoader("res//texturas//cespedfutbol.jpg", null).getTexture();
            ap.setTexture(tex);
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE);
            ap.setTextureAttributes(texAttr);

            Box figuraVisual = new Box((this.x2 - this.x1)/2, .05f, (this.z2 - this.z1)/2, 
                    Box.GENERATE_TEXTURE_COORDS, ap);
            Transform3D t3D = new Transform3D();
            t3D.setTranslation(new Vector3f((x1 + x2) / 2, -2, (z1 + z2) / 2));
            TransformGroup desplazamiento = new TransformGroup();
            desplazamiento.setTransform(t3D);
            desplazamiento.addChild(figuraVisual);
            boundsBG.addChild(desplazamiento);
            bg.addChild(boundsBG);
        }
        dE.añadirDisparador(this);
    }

    @Override
    public void actualizar() {
        boolean b = true;

        float x, z;
        for (Entidad ent : dE.getEntidades()) {
            for (String s : objetivos) {
                if (!ent.getEtiquetas().contains(s)) {
                    b = false;
                    break;
                }
            }
            if (!b) {
                b = true;
                continue;
            }
            x
                    = ent.posiciones[0];
            z = ent.posiciones[2];
            if (x1 <= x && x <= x2
                    && z1 <= z && z <= z2) {
                juego.procesarEvento(this.e);
                break;
            }
        }
    }
}
