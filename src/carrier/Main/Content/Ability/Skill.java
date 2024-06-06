package carrier.Main.Content.Ability;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import carrier.Main.MathComplex;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class Skill implements Cloneable{
    private float results;
    public float rotate,time;
    private final float[] p1= new float[256];
    public int SpawnCount=1;
    public boolean display;
    protected boolean start,end;
    public void displayBars(Unit unit, Table bars){}
    public float data;
    public float shootFadeIn(float speed,Unit u,int index){
        return p1[index]=MathComplex.MoveStaightTowards(p1[index], u.isShooting ? 1:0, speed*Time.delta/60f);
    }
    public boolean displayed(){
        return display;
    }
    public float shootFadeOut(float speed,Unit u,int index){
        return 1- shootFadeIn(speed, u,index);
    }
    //TODO need check delta function
    public float Diffenals(float after){
        lstResults = results;
        results = after;
        return (after-lstResults)/Time.delta;
    }
    public void update(Unit u){
        time+= Time.delta;
    }

    public void addStats(Table t){

    }
    public void death(Unit u){

    }
    public void draw(Unit u){
        
    }
    public boolean isTransformTypeAndAlreadyTransform(Unit u){
        return (u instanceof TransformFlying f && f.TransformNow)||(u instanceof TransformEntity s&&s.TransformNow);
    }
    public float TransformCountFin(Unit u){
        if(u instanceof TransformFlying f){
            return f.second/f.TimeTransform;
        }
        else if(u instanceof TransformEntity f){
            return f.second/f.TimeTransform;
        }
        else return 0;
        
    }
    public float CountDounFin(Unit u){
        if(u instanceof TransformFlying f){
            return f.countDownSecond/f.CountDownTime;
        }
        else if(u instanceof TransformEntity f){
            return f.countDownSecond/f.CountDownTime;
        }
        else return 0;
    }
    public float CountDounFout(Unit u){
        return 1-CountDounFin(u);
    }
    public float TransformCountFout(Unit u){
        return 1-TransformCountFin(u);
    }
    public String localized(){
        var type = getClass();
        return Core.bundle.get("skill." + (type.isAnonymousClass() ? type.getSuperclass() : type).getSimpleName().replace("Skill", "").toLowerCase());
    }
    public void init(UnitType uts){}
    public Skill copy(){
        try{
            return(Skill)clone();
        }
        catch(CloneNotSupportedException err){
            err.printStackTrace();
            throw new RuntimeException("Something Wrong"+err);
        }
    }
    public boolean isRip(Unit u){
        return u==null||u.dead()||!u.isValid()||u.isNull();
    }
}
