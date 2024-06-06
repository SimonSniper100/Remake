package carrier.Main.Content.Type_and_Entity.Part;

import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Type_and_Entity.Other.CopyUnitType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.world.meta.Env;

public class PartType extends CopyUnitType {
    public boolean ImmuneSuction,ShootFacing,Lossen;
    public PartType(String name) {
        super(name);
        accel = 0.05f;
        ShootFacing = false;
        envDisabled = Env.none;
        drag = 0.03f;
        hittable=physics=useUnitCap=canBoost=canDrown=logicControllable=
        playerControllable=targetable=drawMinimap=drawCell=drawItems=createScorch=createWreck=killable=false;
        autoFindTarget=hidden=lowAltitude=ImmuneSuction=true;
        fallSpeed =0.0001f;
        constructor = ()-> new PartEntity();
        controller = u-> new PartUnitAI(Lossen);
        engineSize = 0;
        hitSize =0.1f;
        envDisabled = Env.none;
        groundLayer = (flying ? lowAltitude ?  Layer.flyingUnitLow: Layer.flyingUnit :Layer.groundUnit) -5;
        ImmuneSuction = true;
    }
    @Override
    public void update(Unit u){
        super.update(u);
        
        u.elevation(flying||u.isFlying() ? 1f:0.1f); 
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
