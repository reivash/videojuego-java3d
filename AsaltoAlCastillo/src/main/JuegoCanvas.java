package main;

import entidad.DiccionarioEntidades;
import entidad.Entidad;
import entidad.Personaje;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.Canvas3D;

public class JuegoCanvas extends Canvas3D {
    public static final int MAX_CHAT_LENGTH = 8;
    private List<String> lista;
    private Juego juego;
    public JuegoCanvas(GraphicsConfiguration gc, Juego juego) {
        super(gc);
        lista = new ArrayList<String>();
        this.juego = juego;
    }

    public void postRender() {
        Personaje jugador = juego.getJugador();
        Graphics2D gd = this.getGraphics2D();
        gd.setFont(new Font("Tahoma", Font.PLAIN, 40));
        gd.setColor(Color.black);
        gd.drawString("[ Vida: " + jugador.vida + " ]" ,8, 44);
        
        if(jugador.tieneTesoro){
            gd.setFont(new Font("Tahoma", Font.PLAIN, 24));
            gd.setColor(Color.black);
            gd.drawString("[ Lleva el tesoro al campamento ]" ,this.getWidth()-368, 28);
        }
        
        gd.setFont(new Font("Tahoma", Font.PLAIN, 24));
        gd.setColor(Color.white);
        int lineHeight = this.getHeight()/24;
        int i = 0;
        while(lista.size()-i>0 && i<8){
            gd.drawString(lista.get(lista.size()-i-1), 8, this.getHeight() - 8 - lineHeight* i);
            i++;
        }
        this.getGraphics2D().flush(false);
    }
    
    public void addLineToChat(String line){
        lista.add(line);
        if(lista.size()>MAX_CHAT_LENGTH){
            lista.remove(0);
        }
    }
}
