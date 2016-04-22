package org.aqua.craft.component.j3d;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import org.aqua.struct.IContent;

public class AbstractNode extends BranchGroup implements IContent {
    public static float      unit           = 1f;
    protected TransformGroup transformGroup = new TransformGroup();
    protected int            data;
    protected int[]          coord3         = new int[3];

    public AbstractNode() {
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
        transformGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        addChild(transformGroup);
        setCapability(Group.ALLOW_CHILDREN_WRITE);
        setCapability(Group.ALLOW_CHILDREN_EXTEND);
        setCapability(BranchGroup.ALLOW_DETACH);
        //        RenderingAttributes attr = new RenderingAttributes();
        //        attr.setVisible(false);
    }

    public final int[] getCoord3() {
        return coord3.clone();
    }

    public final void setCoord3(int[] coord3) {
        this.coord3 = coord3.clone();
        Transform3D trans = new Transform3D();
        Vector3d vector = new Vector3d(coord3[0] * unit, coord3[2] * unit, coord3[1] * unit);
        trans.setTranslation(vector);
        transformGroup.setTransform(trans);
    }

    /**
     * 获得焦点
     */
    public void focus() {
    }

    /**
     * 失去焦点
     */
    public void blur() {
    }

    /**
     * 获得对应面的id
     *
     * @param pickFace
     * @return
     */
    public int getFaceID(Shape3D pickFace) {
        return 0;
    }

    @Override
    public Object serialize() {
        return 0;
    }

    @Override
    public void deserialize(Object data) {
    }
}