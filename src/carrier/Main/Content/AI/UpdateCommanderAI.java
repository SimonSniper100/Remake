package carrier.Main.Content.AI;

import arc.math.Angles;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import carrier.Main.Content.Bullet.AccelBulletType;
import carrier.Main.Content.Weapons.ConfigWeapons;
import mindustry.ai.types.CommandAI;
import mindustry.entities.Predict;
import mindustry.entities.Sized;
import mindustry.type.Weapon;

public class UpdateCommanderAI extends CommandAI{
    public Vec2 v1= new Vec2();
    @Override
    public void updateWeapons(){
        float rotation = unit.rotation - 90;
        boolean ret = retarget();
        if(ret){
            target = findMainTarget(unit.x, unit.y, unit.range(), unit.type.targetAir, unit.type.targetGround);
        }

        noTargetTime += Time.delta;

        if(invalid(target)){
            target = null;
        }else{
            noTargetTime = 0f;
        }

        unit.isShooting = false;

        for(var mount : unit.mounts){
            Weapon weapon = mount.weapon;
            float wrange = weapon.range();

            //let uncontrollable weapons do their own thing
            if(!weapon.controllable || weapon.noAttack) continue;

            if(!weapon.aiControllable){
                mount.rotate = false;
                continue;
            }

            float mountX = unit.x + Angles.trnsx(rotation, weapon.x, weapon.y),
                mountY = unit.y + Angles.trnsy(rotation, weapon.x, weapon.y);

            if(unit.type.singleTarget){
                mount.target = target;
            }else{
                if(ret){
                    mount.target = mount.weapon instanceof ConfigWeapons c ? c.findTarget(unit, mountX, mountY, wrange, weapon.bullet.collidesAir, weapon.bullet.collidesGround) : findTarget(mountX, mountY, wrange, weapon.bullet.collidesAir, weapon.bullet.collidesGround);
                }

                if(checkTarget(mount.target, mountX, mountY, wrange)){
                    mount.target = null;
                }
            }

            boolean shoot = false;

            if(mount.target != null ){
                shoot = mount.target.within(mountX, mountY, wrange + (mount.target instanceof Sized s ? s.hitSize()/2f : 0f)) && shouldShoot();
                if(mount.weapon.bullet instanceof AccelBulletType accel){
                    float distance = new Vec2().set(unit).sub(mount.target.getX(),mount.target.getY()).limit(accel.calculateRange()).len();
                    float calculateVelocity = accel.calculateVelocity(distance,accel.calculateRange());
                    Vec2 to = Predict.intercept(unit, mount.target, calculateVelocity);
                    mount.aimX = to.x;
                    mount.aimY = to.y;
                }
                else{
                    Vec2 to = Predict.intercept(unit, mount.target, weapon.bullet.speed);
                    mount.aimX = to.x;
                    mount.aimY = to.y;
                }
            }

            unit.isShooting |= (mount.shoot = mount.rotate = shoot);

            if(mount.target == null && !shoot && !Angles.within(mount.rotation, mount.weapon.baseRotation, 0.01f) && noTargetTime >= rotateBackTimer){
                mount.rotate = true;
                Tmp.v1.trns(unit.rotation + mount.weapon.baseRotation, 100f);
                mount.aimX = mountX + Tmp.v1.x;
                mount.aimY = mountY + Tmp.v1.y;
            }

            if(shoot){
                unit.aimX = mount.aimX;
                unit.aimY = mount.aimY;
            }
        }
        
    }
    @Override
    public void updateUnit(){ 
        super.updateUnit();
        if(useFallback() && (fallback != null || (fallback = fallback()) != null)){
            if(fallback.unit() != unit) fallback.unit(unit);
            fallback.updateUnit();
            return;
        }
        updateTargeting();
    }
    
}
