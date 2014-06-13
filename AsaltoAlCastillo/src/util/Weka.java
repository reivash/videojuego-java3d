/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.classifiers.Classifier;
import weka.classifiers.trees.M5P;
import weka.core.Instances;

/**
 *
 * @author David
 */
public class Weka {

    Classifier conocimiento = null;
    Instances casosEntrenamiento = null;
    int maximoNumeroCasosEntrenamiento = 200;

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
}
