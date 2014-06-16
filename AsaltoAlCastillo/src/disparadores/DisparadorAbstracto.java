package disparadores;

import java.util.ArrayList;
import java.util.List;

public abstract class DisparadorAbstracto implements Disparador {
    
    protected List<String> objetivos = new ArrayList<String>();
    
    public DisparadorAbstracto(List<String> objetivos) {
        this.objetivos = objetivos;
    }
    
}