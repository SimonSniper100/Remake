package carrier.Main.Content.AI;


import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.ai.types.MissileAI;

public class SucideAI extends MissileAI {
    public float TimeToDead=13*60f,time=0f,killTime=0f,TimeToDeadIfShot=3.5f*60f;
    public boolean lock;
    public int ReFindTargetTime;
    //Count in Second
    public float lifetime = 1f;
    public Vec2 v = new Vec2();
    public Vec2 v2 = new Vec2();
    @Override
    public void updateMovement(){
        unloadPayloads();
        if(unit== null)return;
        if(unit.isShooting()||lock){
            lock = true;
            if(killTime>=TimeToDeadIfShot){
                unit.kill();
                killTime = 0f;
            }else{
                killTime+=Time.delta;
            }
        }
        else{
            time+=Time.delta;
            if(time >= TimeToDead){
                unit.kill();
                time = 0f;
            }
        }
        if(shooter==null)return;
        unit.team = shooter.team;
        v.set(shooter.aimX,shooter.aimY).sub(unit).setAngle(unit.rotation());
        target = findTarget(shooter.x,shooter.y,shooter.range(),true,true);
        if(target!= null&&target.team()!=shooter.team&&!shooter.isPlayer()){
            unit.lookAt(target);
            unit.moveAt(v.setLength(unit.speed()));
        }else{ 
            retarget();
            unit.lookAt(shooter.aimX,shooter.aimY);
            if(v.len() >= unit.range()*0.9){
                unit.moveAt(v.setLength(unit.speed()));
            }
        }
    }
    @Override
    public boolean retarget(){
        return timer.get(timerTarget, 4f);
    }
}

