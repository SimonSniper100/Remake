package carrier.Main.Content.AI;

import arc.math.geom.Vec2;
import arc.util.Nullable;
import mindustry.entities.units.AIController;
import mindustry.gen.Unit;

public class DockController extends AIController{
    public @Nullable Unit carrier;
    public float attactRadius =8*12; //constant block size * radius
    float result;
    public Vec2 v1 = new Vec2();
    public Vec2 v2 = new Vec2();
    public Vec2 v3 = new Vec2();
    public Vec2 v4 = new Vec2();
    @Override
    public void updateUnit(){
        updateWeapons();
        if(unit.isFlying())unit.wobble();
        if(carrier == null)return;
        unit.elevation = carrier.elevation+1;
        v1.set(carrier.aimX,carrier.aimY).sub(unit).setAngle(unit.rotation());
        if(carrier.isShooting){
            target = findTarget(carrier.aimX, carrier.aimY, attactRadius, unit.type.targetAir, unit.type.targetGround);
            if(target != null){
                moveTo(target, unit.range() * 0.8f,1f,true,null,true);
                unit.lookAt(target);
            }
            else {
                unit.lookAt(carrier.aimX,carrier.aimY);
                unit.moveAt(v1.setLength(unit.speed()));
            }
        }
        if(carrier == null || carrier.dead() || !carrier.isValid()){
            if(unit != null){
                unit.kill();
            }
        }
        unit.speedMultiplier = carrier.speedMultiplier*1.1f;
        unit.damageMultiplier = carrier.damageMultiplier*1.1f;
        unit.reloadMultiplier = carrier.reloadMultiplier*1.1f;
        unit.healthMultiplier = carrier.healthMultiplier*1.1f;
    }
    public boolean CoordinatesEqual(float x1, float y1, float x2, float y2, float Almost){
        return Math.round(x1 / Almost) == Math.round(x2 /  Almost) && Math.round(y1 /  Almost) == Math.round(y2 /  Almost);
    }
}
