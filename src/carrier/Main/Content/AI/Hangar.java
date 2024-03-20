package carrier.Main.Content.AI;

import arc.math.Angles;
import arc.math.geom.Vec2;
import carrier.Main.Content.Type_and_Entity.Drone.DroneEntity;
import mindustry.entities.units.AIController;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;

public class Hangar extends AIController {
    public float x,y,rotate,xp,yp;
    public Unit carrier;
    public boolean InCarrier;
    public Vec2 v = new Vec2();
    @Override
    public void updateMovement(){
        if(carrier != null){ 
            xp = carrier.x + Angles.trnsx(carrier.rotation,y,x);
            yp = carrier.y + Angles.trnsy(carrier.rotation,y,x);
            v.set(xp,yp).sub(unit);
            if(isPossibleToLand(carrier, unit, 100)||v.len()<carrier.type.hitSize*0.7f){
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
            }
            else{
                unit.moveAt(v.setLength(unit.type.speed));
                unit.lookAt(xp,yp);
                unit.elevation = carrier.elevation+1;
                if(unit instanceof DroneEntity de){
                    de.invis = false;
                }
                InCarrier = false;
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
}
