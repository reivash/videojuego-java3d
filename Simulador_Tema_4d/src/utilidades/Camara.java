package utilidades;

import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import simulador.Figura;

/**
 * Clase interfaz para el reposicionamiento de la cámara en el mundo.
 */
public class Camara {

    private SimpleUniverse universo = null;

    public Camara(SimpleUniverse universo) {
        this.universo = universo;
    }

    public void colocarCamara(Point3d posicionCamara, Point3d objetivoCamara) {
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

    public void camaraAlPersonaje(Figura personaje) {

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
        colocarCamara(posicionCamara, new Point3d(c[0] + dir.x, c[1], c[2] + dir.z));
    }
}
