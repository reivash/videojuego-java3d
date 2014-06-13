package mundo;

import entidad.Bloque;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;
import main.Juego;

public class CreadorDeEstructuras {

    /* Se han de crear entidades fisicas y meterlas al branchgroup */
    /* Los tamaños se especifican en mitades */
    private static float masa = 10;
    private static float elasticidad = 0.00005f;
    private static float dampingLineal = 0.9995f;
    private static float dampingAngular = 0.999f;

    private static float anchuraMuro = 10f;

    public static void crearBloque(
            Vector3f centro,
            Vector3f tamaños,
            Vector3f rotacion,
            BranchGroup conjunto,
            Juego juego) {

        Bloque b = new Bloque(tamaños, "res//texturas//castillo.jpg", conjunto, juego);
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

        /* Asumimos el suelo está a altura 0 */
        comienzo.y = 0;
        fin.y = 0;

        /* Iremos colocando bloques paso a paso */
        Vector3f paso = new Vector3f(fin);
        paso.sub(comienzo);
        float distancia = paso.length();
        paso.scale((float) 1 / (float) numeroBloques);

        /* Datos de cada bloque */
        Vector3f centro = new Vector3f(comienzo);
//        /* Para que no atraviesen el suelo */
        centro.y += altura / 2;

        /* Rotacion */
        float dx = comienzo.x - fin.x;
        float dz = comienzo.z - fin.z;
        float h = (float) Math.sqrt(dx * dx + dz * dz);
        float offset = 0;
        
        /* Tras meses de arduo esfuerzo y muchas horas de insufrible tortura la rotación funciona */
        if (dx < 0) {
            if (dz > 0) {
                offset = (float) (-Math.PI/2 - Math.asin(dz / h)) * -2f;
            } else {
                offset = (float) (Math.PI/2 - Math.asin(dz / h)) * -2f;
            }
        }
        Vector3f rotacion = new Vector3f(0, (float) offset
                + (float) -Math.asin(dz / h), 0);

//        System.out.println("\nComienzo: " + comienzo + " Fin: " + fin);
//        System.out.println("offset: " + offset);
//        System.out.println("dx: " + dx + " dz: " + dz);
//        System.out.println("dx/h: " + dx / h + " dz/h: " + dz / h);
//        System.out.println("dx/dx: " + (dx != 0 ? Math.abs(dx) / dx : 1));
//        System.out.println("Angulo: " + Math.sin(dz / h));

        Vector3f tamaño = new Vector3f(distancia / (numeroBloques + .02f), altura, anchuraMuro);
        tamaño.scale(.5f);
//        System.out.println("Primero: " + centro);
        for (int i = 0; i <= numeroBloques; i++) {
            /* Crear bloque base */
            
//            System.out.println("Creando bloque [centro: " + centro.toString() + " tamaño: " + tamaño.toString() + "]");
            crearBloque(centro, tamaño, rotacion, conjunto, juego);
            centro.add(paso);
//            rotacion.y += .3f;
//            System.out.println(rotacion.y);
            /* ToDo: Crear almena (necesaria rotacion) */
        }
//        System.out.println("Ultimo: " + centro);
    }

    /* Creación de una torre cilíndrica */
    public static void crearTorre(
            Vector3f pos,
            float radio,
            int altura,
            int numeroBloques,
            BranchGroup conjunto,
            Juego juego) {
        for (int i = 0; i < altura; i++) {
            float angDesplazamiento = (float) (Math.PI / numeroBloques);
            for (float j = 2 * angDesplazamiento; j < 2 * Math.PI; j += 2 * angDesplazamiento) {
                float desplNivel = (i & 1) == 0 ? 0 : angDesplazamiento;
                Vector3f posCentro = new Vector3f((float) (pos.x + radio * (Math.sin(j + desplNivel))), pos.y + i * 4f, (float) (pos.z + radio * (Math.cos(j + desplNivel))));
                crearBloque(posCentro, new Vector3f(3.5f, 2, 3.5f), new Vector3f(0, j + desplNivel, 0), conjunto, juego);
            }
        }
        crearBloque(new Vector3f(pos.x, pos.y + altura * 4, pos.z), new Vector3f(radio * 1.6f, 2.5f, radio * 1.6f), new Vector3f(), conjunto, juego);
    }

}
