package carrier.Main.Content.AI;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Nullable;
import arc.util.Tmp;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.entities.units.AIController;
import mindustry.gen.Unit;

public class PartUnitAI extends AIController {
    
    public boolean transformType,isTransform;
    public Unit Transformer;
    public float x,y;
    public boolean lossen;
    private float px,py;
    public PartUnitAI(){
        this.lossen = false;
    }
    public PartUnitAI(boolean lossen){
        this.lossen = lossen;
    }
    @Override
    public void updateMovement(){
        unloadPayloads();
        if(Transformer == null||Transformer.dead||!Transformer.isValid())unit.remove();
        else{
            if(Transformer instanceof TransformEntity t){
                transformType = true;
                if(t.TransformNow){
                    isTransform = true;
                }
                else isTransform = false;
            }
            else if(Transformer instanceof TransformFlying f){
                transformType = true;
                if(f.TransformNow){
                    isTransform = true;
                }
                else isTransform = false;
            }
            else {
                transformType = false;
                isTransform = false;
            }
            if(transformType&&isTransform){
                if(Transformer.isLocal()){
                    for(var mount: unit.mounts){
                        mount.aimX = Transformer.aimX;
                        mount.aimY = Transformer.aimY;
                        mount.shoot = Transformer.isShooting;
                    }
                }
                else{
                    updateWeapons();
                }
                unit.rotation = Transformer.rotation;
                unit.speedMultiplier = Transformer.speedMultiplier;
                unit.reloadMultiplier = Transformer.reloadMultiplier;
                unit.damageMultiplier = Transformer.damageMultiplier;
                unit.healthMultiplier = Transformer.healthMultiplier;
                unit.dragMultiplier = Transformer.dragMultiplier;
                px = Transformer.x+ Angles.trnsx(Transformer.rotation, y,x);
                py = Transformer.y+ Angles.trnsy(Transformer.rotation, y,x);
                if(lossen && !IsTeleport(unit,px,py, Transformer.hitSize*(float)Math.sqrt(Transformer.speedMultiplier/(unit.type.accel+unit.type.drag))))moveTo(px,py, 2,10,false,null,true);
                else unit.set(px,py);
            }
            else{
                unit.remove();
            }
        }
    }
    public boolean IsTeleport(Unit u1,Unit u2,float radius){
        return IsTeleport(u1.x,u1.y,u2.x,u2.y,radius);
    }
    public boolean IsTeleport(Unit u1,float x2,float y2,float radius){
        return IsTeleport(u1.x,u1.y,x2,y2,radius);
    }
    public boolean IsTeleport(float x1,float y1,float x2,float y2,float radius){
        Vec2 dst = new Vec2();
        float distance = dst.set(x1,y1).sub(x2,y2).len();
        return distance >= radius;
    }
    //Copy and Fix posc = > pos point
    public void moveTo(float x,float y, float circleLength, float smooth, boolean keepDistance, @Nullable Vec2 offset, boolean arrive){

        vec.set(x,y).sub(unit);

        float length = circleLength <= 0.001f ? 1f : Mathf.clamp((unit.dst(x,y) - circleLength) / smooth, -1f, 1f);

        vec.setLength(unit.speed() * length);

        if(arrive){
            Tmp.v3.set(-unit.vel.x / unit.type.accel * 2f, -unit.vel.y / unit.type.accel * 2f).add((x - unit.x), (y - unit.y));
            vec.add(Tmp.v3).limit(unit.speed() * length);
        }

        if(length < -0.5f){
            if(keepDistance){
                vec.rotate(180f);
            }else{
                vec.setZero();
            }
        }else if(length < 0){
            vec.setZero();
        }

        if(offset != null){
            vec.add(offset);
            vec.setLength(unit.speed() * length);
        }

        if(vec.isNaN() || vec.isInfinite() || vec.isZero()) return;

        if(!unit.type.omniMovement && unit.type.rotateMoveFirst){
            float angle = vec.angle();
            unit.lookAt(angle);
            if(Angles.within(unit.rotation, angle, 3f)){
                unit.movePref(vec);
            }
        }else{
            unit.movePref(vec);
        }
    }
}



