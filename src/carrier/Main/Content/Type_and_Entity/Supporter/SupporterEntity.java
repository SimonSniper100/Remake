package carrier.Main.Content.Type_and_Entity.Supporter;

import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.Content.Special.EntityRegister;
import mindustry.gen.UnitEntity;

public class SupporterEntity  extends UnitEntity{
    public boolean ser=true;
    @Override
    public int classId(){
        return EntityRegister.getID(SupporterEntity.class);
    }
    @Override
    public boolean serialize(){
        return ser;
    }
    @Override
    public void write(Writes w){
        super.write(w);
        w.bool(ser);
    }
    @Override
    public void read(Reads r){
        super.read(r);
        ser = r.bool();
    }
}   
