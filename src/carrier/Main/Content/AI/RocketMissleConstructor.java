package carrier.Main.Content.AI;

import mindustry.ai.types.MissileAI;
import mindustry.gen.TimedKillc;
import mindustry.type.UnitType;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;

public class RocketMissleConstructor extends MissileAI{
    public float pushProgress = 0.1f,veclocity;
    public boolean reversePush;
    public UnitType spawnType;
    public int maxSpawn;
    public final Vec2 v = new Vec2(),v2=new Vec2();
    @Override
    public void updateMovement(){
        unloadPayloads();
        float time = unit instanceof TimedKillc t ? t.time() : 1000000f;
        float fin = unit instanceof TimedKillc t ? t.fin() :0;
        v.set(unit).setAngle(unit.rotation).rotate(90f*Mathf.sign(!reversePush)).setLength((veclocity)*Interp.pow2Out.apply(1-(Mathf.clamp(fin, 0, pushProgress)/pushProgress)));
        if(Interp.pow2Out.apply(1-(Mathf.clamp(fin, 0, pushProgress)/pushProgress))<=1-pushProgress){
            super.updateMovement();
        }
        else{
            unit.vel.add(v);
            unit.moveAt(vec.trns(unit.rotation, unit.type.missileAccelTime <= 0f ? unit.speed() : Mathf.pow(Math.min(time / unit.type.missileAccelTime, 1f), 2f) * unit.speed()));
        }
    }
}
