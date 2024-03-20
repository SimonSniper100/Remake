package carrier.Main.Content.Type_and_Entity.Other;

import mindustry.entities.part.RegionPart;

public class CopyablePart extends RegionPart implements Cloneable{
    public CopyablePart(String regions) {
        super();
    }
    public CopyablePart copy(){
        try{
            return(CopyablePart)clone();
        }
        catch(CloneNotSupportedException e){
            throw new RuntimeException("Fun" + e);
        }
    }
}
