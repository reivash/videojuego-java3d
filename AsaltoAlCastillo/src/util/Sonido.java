/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.bulletphysics.linearmath.Transform;
import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.io.File;
import java.net.URL;
import javax.media.j3d.BackgroundSound;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointSound;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Sanjay
 */
public class Sonido {
    
    public static PointSound screamSound = new PointSound();
    public static PointSound attackSound = new PointSound();
    public static PointSound victorySound = new PointSound();
    public static PointSound walkingSound = new PointSound();
    public static PointSound explosionSound = new PointSound();
    
    public static TransformGroup tgSound = new TransformGroup();
    public static BranchGroup bgSound = new BranchGroup();

    public static void init(BranchGroup conjunto,SimpleUniverse universo) {
        
        tgSound.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        tgSound.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        tgSound.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        bgSound.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        bgSound.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        bgSound.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        
        
        añadirAudio(tgSound, universo);
        bgSound.addChild(tgSound);
        conjunto.addChild(bgSound);
        
    }
    
    
    protected static void añadirAudio(TransformGroup tgSound,SimpleUniverse universo) {
        try {
            PhysicalEnvironment pe = universo.getViewer().getPhysicalEnvironment();
            JavaSoundMixer objetoMezcladorSonidos = new JavaSoundMixer(pe);
            objetoMezcladorSonidos.initialize();
            universo.getViewer().getView().setPhysicalEnvironment(pe);
            addObjectSound(tgSound, screamSound, "file:./sonido/scream.wav", 1);
            addObjectSound(tgSound, attackSound, "file:./sonido/attack.wav", 1);
            addObjectSound(tgSound, victorySound, "file:./sonido/victory.wav", 1);
            addObjectSound(tgSound, walkingSound, "file:./sonido/walking.wav", 1);
            addObjectSound(tgSound, explosionSound, "file:./sonido/explosion.wav", 1);

        } catch (javax.media.j3d.SoundException re) {
            System.out.println("Media Exception");
            re.printStackTrace();
        } catch (Exception e) {
            System.out.println("Other Exception");
            e.printStackTrace();
        }
    }
    
    public static void reproducirSonido(String sonido){
        stopAllSounds();
        PointSound soundToPlay= new PointSound();
        switch(sonido){
            case "attack":
                soundToPlay=attackSound;
                break;
            case "walk":
                soundToPlay=walkingSound;
                break;
            case "explosion":
                soundToPlay=explosionSound;
                break;
            case "scream":
                soundToPlay=screamSound;
                break;
            case "victory":
                soundToPlay=victorySound;
                break;
        }
        if(!soundToPlay.getEnable()){
            soundToPlay.setEnable(true);
        }
    }
    
    public static void stopAllSounds(){
        screamSound.setEnable(false);
        walkingSound.setEnable(false);
        attackSound.setEnable(false);
        victorySound.setEnable(false);
        explosionSound.setEnable(false);
    }
    
    protected static void addObjectSound(TransformGroup tg, PointSound sound,
            String soundFile, float edge) {
        System.out.println(soundFile);
        //First we get the current transform so that we can
        //position the sound in the same place
        Transform3D objXfm = new Transform3D();
        Vector3f objPosition = new Vector3f();
        tg.getTransform(objXfm);
        objXfm.get(objPosition);
        //Create the media container to load the sound
        MediaContainer soundContainer = new MediaContainer(soundFile);
        //Use the loaded data in the sound
        sound.setSoundData(soundContainer);
        sound.setInitialGain(0f);
        //Set the position to that of the given transform
        sound.setPosition(new Point3f(objPosition));
        //Allow use to switch the sound on and off
        sound.setCapability(PointSound.ALLOW_ENABLE_READ);
        sound.setCapability(PointSound.ALLOW_ENABLE_WRITE);
        sound.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0));
        //Set it off to start with
        sound.setEnable(false);
        //Set it to loop forever
        sound.setLoop(0);
        //Use the edge value to set to extent of the sound
        Point2f[] attenuation = {new Point2f(0.0f, 1.0f),
            new Point2f(edge, 0.1f)};
        sound.setDistanceGain(attenuation);
        //Add the sound to the transform group
        tg.addChild(sound);
    }
        
    protected void addBackgroundSound(BranchGroup b, String soundFile) {
        MediaContainer droneContainer = new MediaContainer(soundFile);
        BackgroundSound drone = new BackgroundSound(droneContainer, 1.0f);
        drone.setSchedulingBounds( new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0));
        drone.setEnable(false);
        drone.setLoop(1);
        b.addChild(drone);

    }
    
}
