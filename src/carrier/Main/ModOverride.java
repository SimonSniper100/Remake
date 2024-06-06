package carrier.Main;

import arc.func.Cons;
import mindustry.Vars;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;

public class ModOverride {
    public static void LoadOverride(){

    }
    public static void BulletOverride(Cons<BulletType> modifier){
        Vars.content.bullets().each(b->{
            if(b instanceof BasicBulletType){
                modifier.get(b);
            }
        });
    }
}
