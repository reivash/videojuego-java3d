/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.*;
import weka.core.*;

/**
 *
 * @author David
 */
public class Weka {

    Classifier conocimiento = null;
    Instances casosEntrenamiento = null;
    int maximoNumeroCasosEntrenamiento = 200;
    Instance caso;

    public Weka(String FicheroEntrenamiento) {
        try {
            casosEntrenamiento = new Instances(new BufferedReader(new FileReader(FicheroEntrenamiento)));
            casosEntrenamiento.setClassIndex(casosEntrenamiento.numAttributes() - 1);
            conocimiento = new M5P();   //àra regresión, o J48 para clasificación
            conocimiento.buildClassifier(casosEntrenamiento);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double resultadoEsperado() {
        return resultadoEsperado(caso);
    }

    public double resultadoEsperado(Instance casoADecidir) {
        double resultado = 0;
        try {
            resultado = conocimiento.classifyInstance(casoADecidir);
        } catch (Exception e) {
            // meter al log el error, supongo
        }
        return resultado;
    }

    public void generarCasoADecidir(double... atributos) {
        caso = casoADecidir(atributos);
    }

    public Instance casoADecidir(double... atributos) {
        Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
        casoAdecidir.setDataset(casosEntrenamiento);
        for (int i = 0; i < atributos.length; i++) {
            casoAdecidir.setValue(i, atributos[i]);
        }
        return casoAdecidir;
    }

    public void fijarAprendizaje(double fuerza, double resultadoRealObservado) {
        fijarAprendizaje(casoADecidir(fuerza), resultadoRealObservado);
    }

    public void fijarAprendizaje(Instance casoAdecidir, double resultadoRealObservado) {
        casoAdecidir.setClassValue(resultadoRealObservado);
        casosEntrenamiento.add(casoAdecidir);
        for (int i = 0; i < casosEntrenamiento.numInstances() - this.maximoNumeroCasosEntrenamiento; i++) {
            casosEntrenamiento.delete(0);  //Si hay muchos ejemplos borrar el más antiguo
        }
        try {
            conocimiento.buildClassifier(casosEntrenamiento);
            Evaluation evaluador = new Evaluation(casosEntrenamiento);
            evaluador.crossValidateModel(conocimiento, casosEntrenamiento, 10, new Random(1));
        } catch (Exception e) {
            // meter el error al log
        }
    }
}
