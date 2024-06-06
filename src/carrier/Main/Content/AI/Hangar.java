package carrier.Main.Content.AI;

import arc.math.Angles;
import arc.math.geom.Vec2;
import arc.util.Nullable;
import carrier.Main.Content.Type_and_Entity.Drone.DroneEntity;
import mindustry.entities.units.AIController;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;

public class Hangar extends AIController {
    public @Nullable float ex,ey,rx,ry;
    public float x,y,rotate,xp,yp;
    public Unit carrier;
    public boolean InCarrier,isReturning;
    public Vec2 v = new Vec2();
    @Override
    public void updateMovement(){
        if(carrier != null){ 
            xp = carrier.x + Angles.trnsx(carrier.rotation,y,x);
            yp = carrier.y + Angles.trnsy(carrier.rotation,y,x);
            float
            prx= carrier.x + Angles.trnsx(carrier.rotation,ry,rx),
            pry= carrier.y + Angles.trnsy(carrier.rotation,ry,rx);
            v.set(xp,yp).sub(unit);
            Vec2 v2 = new Vec2();
            v2.set(prx,pry).sub(unit);
            if(isPossibleToLand(carrier, unit, 100)||v.len()<carrier.type.hitSize*0.7f||v2.len()<carrier.type.hitSize*0.7f){
                unit.elevation = carrier.elevation+0.55f;
                unit.rotation = carrier.rotation - rotate;
                if(unit instanceof DroneEntity de){
                    de.invis = true;
                }
                unit.set(xp,yp);
                for(WeaponMount mount : unit.mounts){
                    mount.shoot = false;
                    mount.rotate = false;
                }
                InCarrier = true;
                if(IsTeleport(unit,xp,yp, carrier.type.hitSize*1.5f)){
                    unit.set(xp,yp);
                }
                isReturning = false;
            }
            else{
                
                unit.moveAt(v2.setLength(unit.speed()));
                unit.lookAt(prx,pry);
                unit.elevation = carrier.elevation+1;
                if(unit instanceof DroneEntity de){
                    de.invis = false;
                }
                InCarrier = false;
                isReturning = true;
            }
        }
        else if(carrier == null ||carrier.isNull() || !carrier.isValid()|| carrier.dead){
            unit.kill();
        }
        unit.wobble();
    }
    public boolean isPossibleToLand(Unit unit1, Unit unit2 ,float FixNumber){
        return (Math.round(unit1.x/FixNumber) == Math.round(unit2.x/FixNumber) && Math.round(unit1.y/FixNumber) == Math.round(unit2.y/FixNumber));
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
}
