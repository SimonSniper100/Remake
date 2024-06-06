package carrier.Main.Content.Ability;

import arc.Core;
import arc.KeyBinds.KeyBind;
import arc.math.Mathf;
import arc.struct.FloatSeq;
import arc.struct.ObjectMap;
import arc.struct.OrderedMap;
import arc.util.Log;
import arc.util.Time;
import mindustry.gen.Unit;

public class PressableSkill extends Skill{
    //set Key, Skill, And Cooldown
    public ObjectMap<KeyBind,Skill> skillKey = new ObjectMap<>();
    public FloatSeq cooldownArray = new FloatSeq();
    private float[] timer = new float[12];
    public float[] foutIndex = new float[12],
            finIndex = new float[12];
    public void SkillPlace(Object ... object){
        for(int i = 0;i<object.length/2;i++){
            if(object[2*i] instanceof KeyBind key &&object[2*i+1] instanceof Skill skills){
                skillKey.put(key,skills);
            }
        }
    }
    public void setCooldownTimer(float ... cooldownTime){
        cooldownArray.addAll(cooldownTime);
    }
    @Override
    public void update(Unit u){
        int i = -1;
        for(var sa:skillKey){
            i++;
            if(i>=cooldownArray.size)break;
            float skillMaxTime = cooldownArray.get(i);
            var s = sa.value;
            if((Core.input.keyDown(sa.key)&&timer[i]<1&&u.isPlayer())||(u.isShooting()&&!u.isPlayer()&&timer[i]<1)){
                s.start = true;
                timer[i] = skillMaxTime;
            }
            timer[i] = Mathf.clamp(timer[i]-Time.delta, 0, skillMaxTime);
            finIndex = FadeInIndex(timer, cooldownArray.toArray());
            foutIndex =FadeOutIndex(timer, cooldownArray.toArray());
            s.update(u);
        }
    }
    @Override
    public void draw(Unit u){
        int i = -1;
        for(var sa:skillKey){
            i++;
            if(i>=cooldownArray.size)break;
            sa.value.draw(u);
        }
    }
    public float[] FadeInIndex(float[] timer,float[] maxTimer){
        float[] f = new float[12];
        for(int i=0;i<Math.min(timer.length,maxTimer.length);i++){
            f[i]=Mathf.clamp(timer[i]/maxTimer[i]);
        }
        return f;
    }
    public float[] FadeOutIndex(float[] timer,float[] maxTimer){
        float[] f = new float[12];
        for(int i=0;i<Math.min(timer.length,maxTimer.length);i++){
            f[i]=1-Mathf.clamp(timer[i]/maxTimer[i]);
        }
        return f;
    }
    @Override
    public boolean displayed(){
        return false;
    }
}
