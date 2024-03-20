package carrier.Main.Content.Type_and_Entity.Part;

import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Type_and_Entity.Other.CopyUnitType;
import mindustry.gen.Unit;
import mindustry.world.meta.Env;

public class PartType extends CopyUnitType {
    public PartType(String name) {
        super(name);
        accel = 0.05f;
        envDisabled = Env.none;
        drag = 0.03f;
        hittable=physics=useUnitCap=logicControllable=playerControllable=targetable=drawMinimap=drawCell=drawItems=createScorch=createWreck=killable=false;
        autoFindTarget=hidden=lowAltitude=true;
        constructor = ()-> new PartEntity();
        controller = u-> new PartUnitAI();
        hitSize =0.1f;
    }
    @Override
    public void update(Unit u){
        super.update(u);
        //ko có lí do gì để nó chết cả 
        //cho dù có chết thì nó củng spawn cái thứ 2
        if(u.health <= 10){
            u.healthMultiplier(Float.POSITIVE_INFINITY);
            u.health = u.maxHealth;
            u.healthMultiplier(1);
        }
        u.elevation(flying ? 1.4f:0.4f); 
    }
    @Override
    public void drawSoftShadow(float x, float y, float rotation, float alpha){
        //none
    }
    @Override
    public void drawSoftShadow(Unit unit, float alpha){
        drawSoftShadow(unit.x, unit.y, unit.rotation, alpha);
    }
    
}
