package carrier.Main;

import arc.func.Cons;
import arc.graphics.Color;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public class CarrierVars {
    public static Color thurmixRed = Color.valueOf("#FF9492"),
		thurmixRedLight = Color.valueOf("#FFCED0"),
		thurmixRedDark = thurmixRed.cpy().lerp(Color.black, 0.9f);
    public boolean isTransformType,isTransform;
    public void TransformSystem(Unit u){
        if(u instanceof TransformEntity s){
            isTransformType = true;
            isTransform = s.TransformNow;
        }
        else if(u instanceof TransformFlying f){
            isTransformType = true;
            isTransform = f.TransformNow;
        }
        else {
            isTransformType = isTransform= false;
        }
    }
    public static Weapon copyMove(Weapon w,float x,float y){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        return n;
    }
    public static Weapon copyMove(Weapon w,float x,float y,boolean display){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        n.display = display;
        return n;
    }
    public static Weapon MoveModifed(Weapon w, float x,float y,Cons<Weapon> mod){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        mod.get(n);
        return n;
    }
}
