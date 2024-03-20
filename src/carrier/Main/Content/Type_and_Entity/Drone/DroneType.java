package carrier.Main.Content.Type_and_Entity.Drone;

import carrier.Main.Content.unit.CarrierUnit;
import mindustry.type.UnitType;

public class DroneType extends UnitType{
    public boolean ImmuneAll;
    public DroneType(String name) {
        super(name);
        constructor = DroneEntity::new;
        autoFindTarget = true;
        playerControllable = drawMinimap= physics = false;
    }
    @Override
    public void drawSoftShadow(float x, float y, float rotation, float alpha){
    }
    @Override
    public void init(){
        super.init();
        if(ImmuneAll){
            CarrierUnit.immunise(this);
        }
    }
}

