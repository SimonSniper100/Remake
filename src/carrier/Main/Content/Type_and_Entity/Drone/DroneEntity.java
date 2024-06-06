package carrier.Main.Content.Type_and_Entity.Drone;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.Content.Special.EntityRegister;
import carrier.Main.Content.Type_and_Entity.Carrier.CarrierClass;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;

public class DroneEntity extends UnitEntity implements CarrierClass{
    public float timer,h,ImmuneChance = 30f,ImmuneTime,speedMultiplierWhenImmune,deltaHealthPerSecond,currentHealth;  
    public boolean chance,invis;
    @Override
    public int classId(){
        return EntityRegister.getID(DroneEntity.class);
    }
    @Override
    public void update(){
        super.update();
        if(TakingDamage(this) && chance &&timer<=0.5f){
            timer = ImmuneTime;
        }
        else if(timer<=0.5f){
            chance= Mathf.chance(ImmuneChance/100f);
        }
        if(timer >=0){
            healthMultiplier(Float.POSITIVE_INFINITY);
            speedMultiplier(speedMultiplierWhenImmune);
            timer -= Time.delta;
        }
        else{
            speedMultiplier(1);
            healthMultiplier(1);
        }
    }
    @Override
    public void draw(){
        if(invis){
            Draw.rect(Core.atlas.find("clear"), x, y);
        }
        else {
            super.draw();
        }
    }
    
    @Override
    public void hitbox(Rect r){
        if(!invis)super.hitbox(r);
        else r.setCentered(x, y,0);
    }
    public boolean TakingDamage(Unit unit){
        currentHealth = deltaHealthPerSecond;
        float c=0;
        while(c<12){
            deltaHealthPerSecond = unit.health();
            c+=Time.delta;
        }
        return (deltaHealthPerSecond - currentHealth)/Time.delta < 0;
    }
    public boolean Healing(Unit unit){
        currentHealth = deltaHealthPerSecond;
        float c=0;
        while(c<12){
            deltaHealthPerSecond = unit.health();
            c+=Time.delta;
        }
        return (deltaHealthPerSecond - currentHealth)/Time.delta > 0;
    }
    @Override
    public void write(Writes write){
        super.write(write);
        write.f(ImmuneChance);
        write.f(timer);
        write.f(ImmuneTime);
        write.f(speedMultiplierWhenImmune);
    }
    @Override
    public void read(Reads read){
        super.read(read);
        ImmuneChance = read.f();
        timer = read.f();
        ImmuneTime = read.f();
        speedMultiplierWhenImmune = read.f();
    }
}
