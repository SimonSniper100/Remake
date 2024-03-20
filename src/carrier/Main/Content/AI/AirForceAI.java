package carrier.Main.Content.AI;

import arc.math.Angles;
import arc.math.geom.Vec2;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.entities.units.AIController;
import mindustry.gen.Unit;

public class AirForceAI extends AIController{
    public Unit caller;
    public float x,y;
    public Vec2 v = new Vec2();
    @Override
    public void updateMovement(){
        unloadPayloads();
        if(caller== null || caller.dead)unit.kill();
        else if(caller instanceof TransformEntity t){
            if(t.TransformNow){
                v.setZero();
                target = findTarget(caller.x, caller.y, caller.range(), unit.type.targetAir, unit.type.targetGround);
                float
                    px = caller.x+ Angles.trnsx(caller.rotation, y,x),
                    py = caller.y+ Angles.trnsy(caller.rotation, y,x);
                v.set(px,py);
                unit.speedMultiplier = caller.speedMultiplier*1.1f;
                unit.damageMultiplier = caller.damageMultiplier*1.1f;
                unit.reloadMultiplier = caller.reloadMultiplier*1.1f;
                unit.healthMultiplier = caller.healthMultiplier*1.1f;
                moveTo(v, 1,0.5f,true,null,true);
                unit.lookAt(caller.aimX,caller.aimY);
                if(caller.isLocal()){
                    for(var m:unit.mounts){
                        m.shoot = caller.isShooting;
                        m.aimX = caller.aimX;
                        m.aimY = caller.aimY;
                    }
                }
                else updateWeapons();
            }
        }
    }
}
