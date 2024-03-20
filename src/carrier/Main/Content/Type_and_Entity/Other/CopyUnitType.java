package carrier.Main.Content.Type_and_Entity.Other;

import mindustry.entities.UnitSorts;
import mindustry.entities.Units.Sortf;
import mindustry.type.UnitType;

public class CopyUnitType extends UnitType implements Cloneable{
    public Sortf unitSort = UnitSorts.closest;
    public CopyUnitType(String name) {
        super(name);
    }
    public UnitType copy(){
        try{
            return (UnitType)clone();
        }
        catch(CloneNotSupportedException c){
            throw new RuntimeException("Fun now you know it"+c);
        }
    }
}
