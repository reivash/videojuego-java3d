package entidad;

import comportamiento.ComportamientoApuntar;
import comportamiento.ComportamientoAtacar;
import comportamiento.ComportamientoPerseguir;
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
            case "perroListo": {
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/Intellect_Devour.mdl", .5f, conjunto, juego, true);
                 ei.añadirTipo("ENEMIGO");
                ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(0, 1, 50), new Vector3f());
                break;
            }
            case "perroPerseguidor": {
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/Intellect_Devour.mdl", .5f, conjunto, juego, true);
                   ei.añadirTipo("ENEMIGO");
                Personaje jugador = (Personaje) diccionarioEntidades.buscarEntidades("JUGADOR").get(0);
                ei.setComportamiento(new ComportamientoPerseguir(ei, jugador));
                ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(0, 1, 50), new Vector3f());
                break;
            }
            case "tiraBolas": {
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/pixie.mdl", .5f, conjunto, juego, true);
                ei.setComportamiento(new ComportamientoApuntar(ei));
                ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(0, 0, 30), new Vector3f());
                ei.añadirTipo("ENEMIGO");
                break;
            }
            case "jauria": {
                Personaje jugador = (Personaje) diccionarioEntidades.buscarEntidades("JUGADOR").get(0);
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        EntidadInteligente ei = new EntidadInteligente("objetosMDL/Intellect_Devour.mdl", .5f, conjunto, juego, true);
                        ei.añadirTipo("ENEMIGO");
                        ei.setComportamiento(new ComportamientoAtacar(ei, jugador));
                        ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f((i - 2) * 30, 1, 500 + j * 10), new Vector3f());
                    }
                }
            }
        }
    }
}
