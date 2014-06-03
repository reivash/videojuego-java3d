package mundo;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3d;

public class CreadorDeEstructuras {

    /* Se han de crear entidades fisicas y meterlas al branchgroup */
    /* Los tamaños se especifican en mitades */
    public static void crearBloque(Vector3d centro, Vector3d tamaños, Vector3d rotacion, BranchGroup conjunto) {
        /* ToDo: Implementar */
    }
    
    /* Los muros tienen un ancho prefijado */
    public static void crearMuro(
            Vector3d comienzo, 
            Vector3d fin,
            float altura,
            int numeroBloques,
            Vector3d rotacion,
            BranchGroup conjunto) 
    {
        /* ToDo: Implementar */
    }
    
    /* Creación de una torre cilíndrica */
    public static void crearTorre(
            Vector3d pos,
            float altura,
            int numeroBloques,
            Vector3d rotacion,
            BranchGroup conjunto) 
    {
        /* ToDo: Implementar */
    }
    
}
