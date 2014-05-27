package figuras;

import entidad.EntidadJava3D;
import java.util.ArrayList;
import javax.media.j3d.BranchGroup;
import javax.vecmath.*;
import main.Juego;

public class EntidadInteligente extends entidad.EntidadFisica {

    //atributos opcionales para dotar a la figura de cierta inteligencia
    public Vector3f localizacionObjetivo;
    public int estadoFigura;                    //Dependiendo del estado de la figura, su entorno, y del juego, la figura tiene un comportamiento dado.
    public int[] estadoEntornoFigura;      //El entorno alrededoar de la figura podria descrbirse con m‡s de un estado. Lloviendo y tengo poca energia
    public EntidadJava3D objetivo;                      //El objetivo puede ser: localizar otra figura,
    //Si adem‡s, hubiera que realizar uan accion particular (ej. Dispararle, darle alimento) se necesitaria otro atributo (ej. TareaObjetivo)
    float aceleracionMuscular;

    public EntidadInteligente(Juego juego, BranchGroup conjunto) {
        super(juego, conjunto);
    }

    public void actualizar() {
        super.actualizar();
        //Opcional: ACTUALIZACION DEL ESTADO DE LA FIGURA Y DEL ESTADO DEL ENTORNO
        //Para actualizar el estado de la figura:  detectar cercanias,exploraciones picking, localizacion (cuadrantes, mundos)
        //Para actualizar el estado del entorno:  lo puede hacer la misma figura, una figura coordinara, o el mismo juego

        //Opcional: ACTUALIZACION DE PLANIFICACION A LARGO PLAZAO
        //Dependiendo del objetivo a conseguir ejecutar un plan a largo plazo
        //REGLAS DE MOVIMIENTO A CORTO PLAZO DE LA FIGURA DEPENDIENDO DE SU ESTADO, DEL ENTORNO Y DEL ESTADO DEL JUEGO
        //ejemplo: C—digo de actualizar() del programa  Navegador_Tema_3
        if (localizacionObjetivo != null) {
            Vector3f direccion = new Vector3f(localizacionObjetivo.x - posiciones[0], 0f, localizacionObjetivo.z - posiciones[2]);
            direccion.normalize();                                                                           //El vector se normaliza con 1 para que indique solo la direccion.
            Vector3f fuerzaDePersecucion;
            fuerzaDePersecucion = new Vector3f(direccion.x * masa * aceleracionMuscular / 2f, 0, direccion.z * masa * aceleracionMuscular / 2f);  //Crea vector fuerza
            cuerpoRigido.applyCentralForce(fuerzaDePersecucion);
        }
        
        if(objetivo != null) {
             this.localizacionObjetivo = new Vector3f(this.objetivo.posiciones[0], this.objetivo.posiciones[1], this.objetivo.posiciones[2]);
        }
    }

    public void asignarObjetivo(EntidadJava3D objetivo, float aceleracionMuscular) {
        this.objetivo = objetivo;
        this.localizacionObjetivo = new Vector3f(this.objetivo.posiciones[0], this.objetivo.posiciones[1], this.objetivo.posiciones[2]);
        this.aceleracionMuscular = aceleracionMuscular;
    }

    public void asignarObjetivo(Vector3f localizacionObjetivo, float aceleracionMuscular) {
        this.localizacionObjetivo = localizacionObjetivo;
        this.aceleracionMuscular = aceleracionMuscular;
    }

}
