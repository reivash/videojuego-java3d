package entidad;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;
import main.Juego;

public class FactoriaEntidades {

    private static DiccionarioEntidades diccionarioEntidades = DiccionarioEntidades.getInstance();

    private static float masa = 1f;
    private static float radio = 1f;
    private static float posX = 5f;
    private static float posY = 5f, posZ = 0f;
    private static float elasticidad = 0.5f;
    private static float dampingLineal = 0.5f;
    private static float dampingAngular = 0.9f;

    public static void crearEntidad(String nombre, BranchGroup conjunto, Juego juego) {

        switch (nombre) {

            case "perroListo":
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/Intellect_Devour.mdl", .2f, conjunto, juego, true);
                ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(20, 4, -15), new Vector3f());
                diccionarioEntidades.añadirEntidadFisica(ei);
                
                break;
        }
    }
}
