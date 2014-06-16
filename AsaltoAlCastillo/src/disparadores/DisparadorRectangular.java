package disparadores;

import entidad.DiccionarioEntidades;
import entidad.Entidad;
import java.util.List;

/* 
    Cuando la entidad con las etiquetas indicadas entre en este rectángulo contenido en el plano del suelo se lanzará el evento 
*/

public class DisparadorRectangular extends DisparadorAbstracto {

    private DiccionarioEntidades dE = DiccionarioEntidades.getInstance();
    
    private float x1,z1,x2,z2;
    
    public DisparadorRectangular(List<String> objetivos, float x1, float z1, float x2, float z2) {
        super(objetivos);
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    @Override
    public void actualizar() {
        float x,z;
        for(Entidad e : dE.getEntidades()){
            x = e.posiciones[0];
            z = e.posiciones[2];
        }
    }
    
}
