package mundo;

import figuras.Bloque;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;
import main.Juego;

public class CreadorDeEstructuras {

    /* Se han de crear entidades fisicas y meterlas al branchgroup */
    /* Los tamaños se especifican en mitades */
    private static float masa = 1f;
    private static float elasticidad = 0.5f;
    private static float dampingLineal = 0.5f;
    private static float dampingAngular = 0.9f;
    
    private static float anchuraMuro = 10f;
    
    public static void crearBloque(
            Vector3f centro,
            Vector3f tamaños,
            Vector3f rotacion,
            BranchGroup conjunto,
            Juego juego) {
        
        Bloque b = new Bloque(tamaños, "res//texturas//muro.jpg", conjunto, juego);
        b.crearPropiedades(masa, elasticidad, dampingLineal, centro, rotacion);
    }

    /* Los muros tienen un ancho prefijado */
    public static void crearMuro(
            Vector3f comienzo,
            Vector3f fin,
            float altura,
            int numeroBloques,
            BranchGroup conjunto,
            Juego juego) {
        
        /* Iremos colocando bloques paso a paso */
        Vector3f paso = new Vector3f(fin);
        paso.sub(comienzo);
        float distancia = paso.length();
        paso.scale((float) 1 / (float) numeroBloques);
        
        /* Datos de cada bloque */
        Vector3f centro = new Vector3f(comienzo);
//        /* Para que no atraviesen el suelo */
//        centro.y += altura/2;
        
        Vector3f tamaño = new Vector3f(distancia / (numeroBloques+1), altura, anchuraMuro);
        tamaño.scale(.5f);
        for (int i = 0; i < numeroBloques; i++) {
            /* Crear bloque base */
            centro.add(paso);
            System.out.println("Creando bloque [centro: " + centro.toString() + " tamaño: " + tamaño.toString() + "]");
            crearBloque(centro, tamaño, new Vector3f(), conjunto, juego); 
                    
            /* ToDo: Crear almena (necesaria rotacion) */
        }
    }

    /* Creación de una torre cilíndrica */
    public static void crearTorre(
            Vector3f pos,
            float altura,
            int numeroBloques,
            Vector3f rotacion,
            BranchGroup conjunto,
            Juego juego) {
        /* ToDo: Implementar */
    }
    
}
