package org.aqua.craft.component.craft.j3d;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.aqua.craft.component.wrapper.IComponent;
import org.aqua.framework.ui.wrapper.IPaintable;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class J3DUniverse implements IComponent {
    protected Map<String, BranchGroup> groupMap;
    private Canvas3D                   canvas;
    private BranchGroup                baseGroup;
    private BranchGroup                rootGroup;
    /** 仰角变换 */
    private TransformGroup             elevationGroup;
    /** 偏角变换 */
    private TransformGroup             deflectionGroup;
    /** 缩放变换 */
    private TransformGroup             scaleGroup;

    public J3DUniverse() {
        BoundingSphere behaviorBounding = new BoundingSphere(new Point3d(), 200);
        rootGroup = new BranchGroup();
        groupMap = new HashMap<String, BranchGroup>();
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        {
            baseGroup = new BranchGroup();
            baseGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        }
        {
            scaleGroup = new TransformGroup();
            scaleGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            scaleGroup.addChild(baseGroup);
        }
        {
            deflectionGroup = new TransformGroup();
            deflectionGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            deflectionGroup.addChild(scaleGroup);
            {
                BranchGroup beaconGroup = new BranchGroup();
                beaconGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
                TransformGroup transform = new TransformGroup();
                transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                transform.addChild(beaconGroup);
                deflectionGroup.addChild(transform);
                groupMap.put("anchor", beaconGroup);
            }
        }
        {
            elevationGroup = new TransformGroup();
            elevationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            elevationGroup.addChild(deflectionGroup);
        }
        {   // TODO to review
            AmbientLight ambientLight = new AmbientLight(new Color3f(Color.white));
            ambientLight.setInfluencingBounds(behaviorBounding);

            DirectionalLight directionalLight = new DirectionalLight(new Color3f(Color.white),
                    new Vector3f(-40f, -40f, -40f));
            directionalLight.setInfluencingBounds(behaviorBounding);
            rootGroup.addChild(directionalLight);
            rootGroup.addChild(ambientLight);
            rootGroup.addChild(elevationGroup);
        }
        {
            PickMouseBehavior pickBehavior = new PickMouseBehavior(canvas, rootGroup,
                    behaviorBounding) {
                @SuppressWarnings("rawtypes")
                @Override
                public void processStimulus(Enumeration criteria) {
                    super.processStimulus(criteria);
                    if (mevent.getID() == MouseEvent.MOUSE_MOVED) {
                        updateScene(mevent.getX(), mevent.getY());
                    }
                }

                @Override
                public void updateScene(int xpos, int ypos) {
                    pickCanvas.setShapeLocation(xpos, ypos);
                    PickResult result = pickCanvas.pickClosest();
                    if (result != null) {
                        Node pickNode = result.getNode(PickResult.PRIMITIVE);
                        for (; pickNode != null && !(pickNode instanceof AbstractNode); pickNode = pickNode
                                .getParent()) {
                        }
                        if (pickNode instanceof AbstractNode) {
                            AbstractNode pickUnit = (AbstractNode) pickNode;
                            int pickFace = pickUnit.getFaceID((Shape3D) result
                                    .getNode(PickResult.SHAPE3D));
                            picking(pickUnit, pickFace, mevent);
                        }
                    } else {
                        picking(null, 0, mevent);
                    }
                }
            };
            MouseRotate rotateBehavior = new MouseRotate(deflectionGroup) {
                @Override
                public void processMouseEvent(MouseEvent evt) {
                    y_last = evt.getY();
                    super.processMouseEvent(evt);
                }
            };

            pickBehavior.setMode(PickTool.GEOMETRY);
            pickBehavior.setSchedulingBounds(behaviorBounding);
            rotateBehavior.setSchedulingBounds(behaviorBounding);
            baseGroup.addChild(pickBehavior);
            baseGroup.addChild(rotateBehavior);
        }

        scale(10);
        deflect(Math.PI / 4);
        eleveate(Math.PI / 6);
    }

    public void compile() {
        {
            Transform3D stadiaTrans = new Transform3D();
            stadiaTrans.setTranslation(new Vector3d(0f, 0f, 20f)); // distance to Camera
            SimpleUniverse universe = new SimpleUniverse(canvas);
            universe.getViewingPlatform().getViewPlatformTransform().setTransform(stadiaTrans);
            universe.addBranchGraph(rootGroup);
        }
    }

    public void display(String branch, Node node) {
        BranchGroup group;
        if (groupMap.containsKey(branch)) {
            group = groupMap.get(branch);
        } else {
            groupMap.put(branch, group = new BranchGroup());
            group.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
            group.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
            TransformGroup transform = new TransformGroup();
            transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            transform.addChild(group);
            BranchGroup shell = new BranchGroup();
            shell.addChild(transform);
            baseGroup.addChild(shell);
        }
        if (null == node) {
            group.removeAllChildren();
        } else {
            group.addChild(node);
        }
    }

    public void restore(String branch, Node node) {
        if (groupMap.containsKey(branch)) {
            groupMap.get(branch).removeChild(node);
        }
    }

    public void translate(float[] coord3, String branch) {
        Transform3D transform = new Transform3D();
        TransformGroup group = (TransformGroup) groupMap.get(branch).getParent();
        group.getTransform(transform);
        Vector3f vector = new Vector3f();
        if (null != coord3) {
            transform.get(vector);
            vector.add(new Vector3f(coord3));
        }
        transform.setTranslation(vector);
        group.setTransform(transform);
    }

    public void scale(int calibration) {
        Transform3D scaleTransform = new Transform3D();
        scaleGroup.getTransform(scaleTransform);
        scaleTransform.setScale(Math.max(scaleTransform.getScale() + calibration * 0.1f, 1));
        scaleGroup.setTransform(scaleTransform);
    }

    public void deflect(double angle) {
        Transform3D deflectionTrans = new Transform3D();
        deflectionGroup.getTransform(deflectionTrans);
        Quat4d quat4d = new Quat4d();
        deflectionTrans.get(quat4d);
        AxisAngle4d angle4d = new AxisAngle4d();
        angle4d.set(quat4d);
        angle4d.angle = (angle4d.angle + (angle4d.y < 0 ? -angle : angle));
        deflectionTrans.setRotation(angle4d);
        deflectionGroup.setTransform(deflectionTrans);
    }

    public void eleveate(double angle) {
        Transform3D elevationTrans = new Transform3D();
        elevationTrans.setRotation(new AxisAngle4d(1, 0, 0, angle));
        elevationGroup.setTransform(elevationTrans);
    }

    public void picking(AbstractNode pickUnit, int id, MouseEvent mevent) {

    }

    @Override
    public IPaintable getPaint() {
        return null;
    }
    @Override
    public void paint(IPaintable paint, Object... params) {
    }
    @Override
    public void handleCommand(Object... commands) {
    }
    @Override
    public Canvas getComponent() {
        return canvas;
    }
}
