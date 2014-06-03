package mundo;

import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;

public class CreadorDeEstructuras {

    /* Se han de crear entidades fisicas y meterlas al branchgroup */
    /* Los tamaños se especifican en mitades */
    public static void crearBloque(Vector3f centro, Vector3f tamaños, Vector3f rotacion, BranchGroup conjunto) {
        /* ToDo: Implementar */
    }
    
    /* Los muros tienen un ancho prefijado */
    public static void crearMuro(
            Vector3f comienzo, 
            Vector3f fin,
            float altura,
            int numeroBloques,
            Vector3f rotacion,
            BranchGroup conjunto) 
    {
        /* ToDo: Implementar */
    }
    
    /* Creación de una torre cilíndrica */
    public static void crearTorre(
            Vector3f pos,
            float radio,
            int altura,
            int numeroBloques,
            BranchGroup conjunto) 
    {
        for(int i = 0; i < altura; i++){
            float angDesplazamiento = (float) (Math.PI/numeroBloques); 
            for(int j = 0; j < numeroBloques; j++){
                 float angulo = (float) (j==0?0:(2*Math.PI/j + (j&1)==0?0:angDesplazamiento));
                 Vector3f posCentro = new Vector3f((float) (pos.x+radio*(Math.sin(angulo))), pos.y + i, (float) (pos.x+radio*(Math.cos(angulo))));
                 crearBloque(posCentro, new Vector3f(2,1,1), new Vector3f(0,angulo,0), conjunto);
            }
        }
    }
    
}
