package carrier.Main.Content.Effect;

import mindustry.entities.Effect;

public class CopyableEffect extends Effect implements Cloneable{
    public Effect copy(){
        try{
            return(CopyableEffect)clone();
        }
        catch(CloneNotSupportedException e){
            throw new RuntimeException("Fun"+e);
        }
    }
}
