package org.aqua.craft.component.j3d;

import java.util.Arrays;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3i;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

public class CursorNode extends AbstractNode {
    private static float Edge = 0.01f;
    private Box          box;
    public CursorNode() {
        super();
        float size = unit / 8;

        int tMode = TransparencyAttributes.BLENDED;
        float tVal = 0.8f;
        int srcBlendFunction = TransparencyAttributes.BLEND_SRC_ALPHA;
        int dstBlendFunction = TransparencyAttributes.BLEND_ONE;
        TransparencyAttributes attributes = new TransparencyAttributes(tMode, tVal,
                srcBlendFunction, dstBlendFunction);
        Appearance app = new Appearance();
        app.setTransparencyAttributes(attributes);
        box = new Box(size / 2, size / 2, size / 2, app);
        box.setPickable(false);
        transformGroup.addChild(box);
    }

    //    public final void setCoord3(int[] coord3) {
    //        this.coord3 = coord3.clone();
    //        Transform3D trans = new Transform3D();
    //        Vector3d vector = new Vector3d(coord3[0] * unit, coord3[2] * unit, coord3[1] * unit);
    //        trans.setTranslation(vector);
    //        transformGroup.setTransform(trans);
    //    }

    private int[] shift3d(int[] src) {
        int[] dst = new int[3];
        System.arraycopy(src, 0, dst, 0, src.length);
        int temp = dst[1];
        dst[1] = dst[2];
        dst[2] = temp;
        return dst;
    }

    public void setCoord3(int[] one, int[] another) {
        one = shift3d(one);
        another = shift3d(another);
        Point3d p1 = new Point3d(one[0], one[1], one[2]);
        Point3d p2 = new Point3d(another[0], another[1], another[2]);
        Vector3d vec = new Vector3d();
        vec.sub(p2, p1);

        Vector3d scale = new Vector3d(vec.length() * 8, 1, 1);  // an normal on X
        //        scale.x = 0 == scale.x ? 1 : scale.x * 8;
        //        scale.y = 0 == scale.y ? 1 : scale.y * 8;
        //        scale.z = 0 == scale.z ? 1 : scale.z * 8;
        System.out.println("from:" + Arrays.toString(one) + "to:" + Arrays.toString(another));
        Transform3D trans = new Transform3D();
        transformGroup.getTransform(trans);
        System.out.println("scale:" + scale);
        trans.setScale(scale);
        Vector3d offset = new Vector3d();
        offset.add(p1, p2);
        offset.scale(0.5);
        System.out.println("offset:" + offset);
        trans.setTranslation(offset);

        Vector3d normalX = new Vector3d(1, 0, 0);

        double angleXZ = normalX.angle(new Vector3d(vec.x, 0, vec.z));
        double angleXY = normalX.angle(new Vector3d(vec.x, vec.y, 0));
        if (!Double.isNaN(angleXZ)) {
            trans.setRotation(new AxisAngle4d(0, 1, 0, angleXZ));
        }
        if (!Double.isNaN(angleXY)) {
            trans.setRotation(new AxisAngle4d(0, 0, 1, angleXY));
        }
        transformGroup.setTransform(trans);
    }
}
