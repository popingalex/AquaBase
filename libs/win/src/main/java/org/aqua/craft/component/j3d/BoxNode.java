package org.aqua.craft.component.j3d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;

public class BoxNode extends AbstractNode {
    private static final int[][] FACE_OFFSET;
    private Box                  box;
    private Appearance           app;
    public static Color[]        DEFAULT_COLOR;
    static {
        DEFAULT_COLOR = new Color[16];
        DEFAULT_COLOR[0] = new Color(0xDDDDDD);
        DEFAULT_COLOR[1] = new Color(0x9AA1A1);
        DEFAULT_COLOR[2] = new Color(0x404040);
        DEFAULT_COLOR[3] = new Color(0x191616);
        DEFAULT_COLOR[4] = new Color(0x963430);
        DEFAULT_COLOR[5] = new Color(0xDB7D3E);
        DEFAULT_COLOR[6] = new Color(0xB1A627);
        DEFAULT_COLOR[7] = new Color(0x41AE38);
        DEFAULT_COLOR[8] = new Color(0x35461B);
        DEFAULT_COLOR[9] = new Color(0x6B8AC9);
        DEFAULT_COLOR[10] = new Color(0x2E6E89);
        DEFAULT_COLOR[11] = new Color(0x2E388D);
        DEFAULT_COLOR[12] = new Color(0x7E3DB5);
        DEFAULT_COLOR[13] = new Color(0xB350BC);
        DEFAULT_COLOR[14] = new Color(0xD08499);
        DEFAULT_COLOR[15] = new Color(0x4F321F);
    }
    static {
        FACE_OFFSET = new int[6][];
        FACE_OFFSET[Box.FRONT] = new int[] { 0, 0, 1 };
        FACE_OFFSET[Box.BACK] = new int[] { 0, 0, -1 };
        FACE_OFFSET[Box.RIGHT] = new int[] { 0, 1, 0 };
        FACE_OFFSET[Box.LEFT] = new int[] { 0, -1, 0 };
        FACE_OFFSET[Box.TOP] = new int[] { 1, 0, 0 };
        FACE_OFFSET[Box.BOTTOM] = new int[] { -1, 0, 0 };
    }
    public static int[] getFaceOffset(int id) {
        return FACE_OFFSET[id].clone();
    }

    public BoxNode() {
        super();
        float size = unit / 4;
        box = new Box(size / 2, size / 2, size / 2, Box.GENERATE_TEXTURE_COORDS, null);
        //        box.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        app = new Appearance();
        app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        box.setAppearance(app);
        transformGroup.addChild(box);
    }

    @Override
    public int getFaceID(Shape3D pickFace) {
        if (box.getShape(Box.FRONT) == pickFace) {
            return Box.FRONT;
        } else if (box.getShape(Box.BACK) == pickFace) {
            return Box.BACK;
        } else if (box.getShape(Box.RIGHT) == pickFace) {
            return Box.RIGHT;
        } else if (box.getShape(Box.LEFT) == pickFace) {
            return Box.LEFT;
        } else if (box.getShape(Box.TOP) == pickFace) {
            return Box.TOP;
        } else if (box.getShape(Box.BOTTOM) == pickFace) {
            return Box.BOTTOM;
        } else {
            return -1;
        }
    }

    public void focus(int id) {
    }

    @Override
    public void focus() {
    }

    @Override
    public Object serialize() {
        return data;
    }

    @Override
    public void deserialize(Object data) {
        /**
         * 隐藏物体
         * RenderingAttributes attr = new RenderingAttributes();
         * attr.setVisible(false);
         * app.setRenderingAttributes(attr);
         */
        this.data = (Integer) data;
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = image.createGraphics();
        graph.setColor(DEFAULT_COLOR[this.data]);
        graph.fillRect(1, 1, 30, 30);
        Texture texture = new TextureLoader(image).getTexture();
        app.setTexture(texture);
    }
}
