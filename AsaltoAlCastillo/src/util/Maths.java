package util;

public class Maths {

    public static float distancia(float[] p1, float[] p2) {
        float x = p1[0] - p2[0];
        float y = p1[1] - p2[1];
        float z = p1[2] - p2[2];
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    public static float distanciaHorizontal(float[] p1, float[] p2) {
        float x = p1[0] - p2[0];
        float z = p1[2] - p2[2];
        return (float) Math.sqrt(x * x + z * z);
    }
}