package carrier.Main.Content.Type_and_Entity.Supporter;

import carrier.Main.Content.Type_and_Entity.Other.CopyUnitType;
import carrier.Main.Content.unit.CarrierUnit;

public class SupporterType extends CopyUnitType {
    public boolean ImmuneAll;
    public SupporterType(String name) {
        super(name);
        constructor =()-> new SupporterEntity(){{
            
        }};
    }
    @Override
    public void init(){
        super.init();
        if(ImmuneAll){
            CarrierUnit.immunise(this);
        }
    }
}
