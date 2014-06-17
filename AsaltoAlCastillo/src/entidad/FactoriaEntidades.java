package entidad;

import comportamiento.ComportamientoApuntar;
import comportamiento.ComportamientoAtacar;
import comportamiento.ComportamientoJefazoDefensor;
import comportamiento.ComportamientoPatrullar;
import comportamiento.ComportamientoPerseguir;
import comportamiento.ComportamientoRangoAtacar;
import java.util.ArrayList;
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
            case "soldado": {
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/Iron_Golem_Bl.mdl", .5f, conjunto, juego, true);
                ei.añadirTipo("ENEMIGO");
                ei.setComportamiento(new ComportamientoRangoAtacar(ei, 256));
                ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(25, 1, 0), new Vector3f());
                break;
            }
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
                EntidadInteligente ei = new EntidadInteligente("objetosMDL/Fire_Elemental.mdl", .5f, conjunto, juego, true);
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
                        ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f((i - 2) * 30, 1, 300 + j * 10), new Vector3f());
                    }
                }
                break;
            }
            case "tesoro": {
//                Bloque b = new Bloque(new Vector3f(10, 4, 4), "res//texturas//balon.jpg", conjunto, juego);
//                b.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(0, 2, 350), new Vector3f());
//                b.añadirTipo("TESORO");
                Tesoro t = new Tesoro(new Vector3f(10, 4, 4), "res//texturas//balon.jpg", conjunto, juego);
                t.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(0, 2, 350), new Vector3f());
                t.añadirTipo("TESORO");
                break;
            }
            case "escuadron01": {
                ArrayList<EntidadInteligente> escuadron = new ArrayList<EntidadInteligente>();
                EntidadInteligente ei;
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < 5; i++) {
                        ei = new EntidadInteligente("objetosMDL/Iron_Golem_Bl.mdl", .5f, conjunto, juego, true);
                        ei.añadirTipo("ENEMIGO");
                        ei.setComportamiento(new ComportamientoRangoAtacar(ei, 256));
                        ei.crearPropiedades(masa, elasticidad, dampingLineal, new Vector3f(i * 5 - 10, 1, 100 - j * 5), new Vector3f(0, (float) Math.PI, 0));
                        ei.setDañoAtaque(10);
                        ei.setVida(50);
                        escuadron.add(ei);
                    }
                }
                EntidadInteligente jefazo = new EntidadInteligente("objetosMDL/Iron_Golem_Yel.mdl", 1f, conjunto, juego, true);
                jefazo.añadirTipo("ENEMIGO");
                jefazo.setComportamiento(new ComportamientoJefazoDefensor(jefazo, escuadron));
                jefazo.crearPropiedades(2.5f, elasticidad, dampingLineal, new Vector3f(0, 1, 105), new Vector3f(0, (float) Math.PI, 0));
                jefazo.setVida(300);
                jefazo.setIntervaloAtaque(10);
                jefazo.setDañoAtaque(20);

                break;
            }
            case "patrullaCastillo": {

                {
                    float[] p1 = new float[]{-80, 0, 110};
                    float[] p2 = new float[]{80, 0, 110};
                    EntidadInteligente ei = new EntidadInteligente("objetosMDL/Iron_Golem_Bl.mdl", .7f, conjunto, juego, true);
                    ei.añadirTipo("ENEMIGO");
                    ei.setComportamiento(new ComportamientoPatrullar(ei, p1, p2));
                    ei.crearPropiedades(3, elasticidad, dampingLineal, new Vector3f(5, 1, 110), new Vector3f(0, 0, 0));
                    ei.setDañoAtaque(15);
                }
                {
                    float[] p1 = new float[]{-80, 0, 115};
                    float[] p2 = new float[]{80, 0, 115};
                    EntidadInteligente ei = new EntidadInteligente("objetosMDL/Iron_Golem_Bl.mdl", .7f, conjunto, juego, true);
                    ei.añadirTipo("ENEMIGO");
                    ei.setComportamiento(new ComportamientoPatrullar(ei, p2, p1));
                    ei.crearPropiedades(3, elasticidad, dampingLineal, new Vector3f(-5, 1, 115), new Vector3f(0, 0, 0));
                    ei.setDañoAtaque(15);
                }
                break;
            }
        }
    }
}
