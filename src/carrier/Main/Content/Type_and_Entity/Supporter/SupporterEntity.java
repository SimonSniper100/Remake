package carrier.Main.Content.Type_and_Entity.Supporter;

import carrier.Main.Content.Special.EntityRegister;
import mindustry.gen.UnitEntity;

public class SupporterEntity  extends UnitEntity{
    @Override
    public int classId(){
        return EntityRegister.getID(SupporterEntity.class);
    }
    
}   
