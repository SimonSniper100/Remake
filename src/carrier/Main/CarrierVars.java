package carrier.Main;

import arc.func.Cons;
import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.type.Weapon;

public class CarrierVars {
    public static Color thurmixRed = Color.valueOf("#FF9492"),
		thurmixRedLight = Color.valueOf("#FFCED0"),
		thurmixRedDark = thurmixRed.cpy().lerp(Color.black, 0.9f);
    public boolean isTransformType,isTransform;
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
    public static Seq<Weapon> FlipWeapon(Weapon weapon,float x,float y,boolean flipSpirte){
        Seq<Weapon> flip = new Seq<>();
        Weapon n = weapon.copy();
        n.x = x;
        n.y = y;
        Weapon f = n.copy();
        f.x *= -1;
        f.flipSprite = flipSpirte;
        flip.add(n,f);
        return flip;
    }
    public static Seq<Weapon> WeaponChainAdd(Weapon weapon,Seq<Float> postions){
        Seq<Weapon> chainAdd = new Seq<>();
        for(int i = 0;i<(int)(postions.size/2);i++){
            float x = postions.get(2*i),y = postions.get(2*i+1);
            Weapon c = weapon.copy();
            c.display = i==0;
            c.x = x;
            c.y = y;
            chainAdd.addAll(c);
        }
        return chainAdd;
    }
}
