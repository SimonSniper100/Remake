package carrier.Main.Content.Type_and_Entity.Other;

import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;

public class LaserUnitType extends UnitType {
    public LaserUnitType(String name) {
        super(name);
        constructor = LaserEntity::new;
        hittable=createScorch=createWreck=targetable=useUnitCap=outlines=drawCell=drawMinimap=omniMovement=false;
        killable =flying =true;
        engines.clear();
    }
    public class LaserEntity extends UnitEntity {
        @Override
        public void wobble(){
            
        }
        @Override
        public void update(){
            super.update();
            elevation(0.4f);
        }
    }
}
