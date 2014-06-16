/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidad;

import javax.vecmath.Vector3f;

/**
 *
 * @author David
 */
public class Propiedades {
    Entidad entidad;
    float masa;
    float elasticidad;
    float dampingLineal;
    Vector3f centro;
    Vector3f fuerza;
    
    public Propiedades(Entidad entidad, float masa, float elasticidad, float dampingLineal, Vector3f centro, Vector3f fuerza){
        this.entidad = entidad;
        this.masa = masa;
        this.elasticidad = elasticidad;
        this.dampingLineal = dampingLineal;
        this.centro = centro;
        this.fuerza = fuerza;
    }
    
    public void registrar(){
        entidad.crearPropiedades(masa, elasticidad, dampingLineal, centro, new Vector3f());
        entidad.velocidad_lineal = fuerza;
    }
}
