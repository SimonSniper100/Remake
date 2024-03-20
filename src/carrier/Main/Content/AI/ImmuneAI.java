package carrier.Main.Content.AI;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.units.AIController;

public class ImmuneAI extends AIController{
    public float timer;
    public float InmuneChance = 10f;
    public Object data;
    public static class ImmuneCountdown{
        public boolean end;
        public ImmuneCountdown(boolean end){
            this.end = end;
        }
    }
    @Override
    public void updateMovement(){
        unloadPayloads();
        if(TakingDamage() && Mathf.chance(InmuneChance/100f)&&timer<=0.5f){
            data = new ImmuneCountdown(false);
            timer = 90f;
        }
        if(timer >=0 && !((ImmuneCountdown)data).end){
            unit.type.hittable = false;
            unit.speedMultiplier(1.25f);
            timer -= Time.delta;
        }
        else{
            data = new ImmuneCountdown(true);
            unit.type.hittable = true;
            unit.speedMultiplier(1);
        }

    }
    public boolean TakingDamage(){
        float h = unit.health();
        float delta = h - unit.health();
        return delta > 0;
    }
    public boolean IsHealing(){
        float h = unit.health();
        float delta = h - unit.health();
        return delta < 0;
    }
}
