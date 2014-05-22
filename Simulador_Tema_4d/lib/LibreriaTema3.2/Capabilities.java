import com.sun.j3d.demos.utils.scenegraph.traverser.ProcessNodeInterface;
import com.sun.j3d.demos.utils.scenegraph.traverser.TreeScan;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.media.j3d.TransformGroup;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mnp
 */
public class Capabilities {
       private static ProcessNodeInterface capabilityProcessor = new ProcessNodeInterface(){
            public void processNode(Node node)  {
                if (node instanceof Group)                {
                    node.setCapability(Group.ALLOW_CHILDREN_READ);
                    if (node instanceof TransformGroup)          {
                        node.setCapability(TransformGroup.ALLOW_LOCAL_TO_VWORLD_READ);
                    }
                }
                else if (node instanceof Shape3D){
                    Shape3D s = (Shape3D) node;
                    s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                    Appearance app = s.getAppearance();
                    app.setCapability(Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
                    app.clearCapabilityIsFrequent(Appearance.ALLOW_TEXTURE_UNIT_STATE_READ);
                    app.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
                    app.clearCapabilityIsFrequent(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
                    app.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
                    app.clearCapabilityIsFrequent(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);

                    int tuCount = s.getAppearance().getTextureUnitCount();
                    for (int i = 0; i < tuCount; i++){
                    	TextureUnitState tu = s.getAppearance().getTextureUnitState(i);
                        if (tu != null) {
                            tu.setCapability(TextureUnitState.ALLOW_STATE_READ);
                            tu.getTexture().setCapability(Texture.ALLOW_ENABLE_WRITE);
                            tu.getTextureAttributes().setCapability(TextureAttributes.ALLOW_MODE_WRITE);
                            tu.getTexture().clearCapabilityIsFrequent(Texture.ALLOW_ENABLE_WRITE);
                            tu.getTextureAttributes().clearCapabilityIsFrequent(TextureAttributes.ALLOW_MODE_WRITE);
                        }
                    }
                }
            }
    };
 public static void setCapabilities(BranchGroup bg)  {
        TreeScan.findNode(bg, new Class[]
            {Group.class, Shape3D.class}, capabilityProcessor, false, false);
    }
}