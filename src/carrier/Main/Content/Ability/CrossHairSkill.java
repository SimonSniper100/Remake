package carrier.Main.Content.Ability;

import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.Units;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;

import static carrier.Main.MathComplex.Min;
import static carrier.Main.MathComplex.MoveStaightTowards;
import static carrier.Main.MathComplex.continues;

public class CrossHairSkill extends Skill{
    private int maxSize = 10;
    private float SclRange;
    protected Teamc target;
    boolean r = false;
    boolean[] rIndex = new boolean[maxSize];
    private float prog[]=new float[maxSize],ProgIndex[]=new float[maxSize],timeDelaySecondIndex[] = new float[maxSize];
    int t[]=new int[maxSize];
    public float sclRange(Unit u){
        return SclRange = Mathf.clamp(Mathf.dst(u.x,u.y,u.aimX,u.aimY)/ Math.abs(u.range()));
    }
    public void ExtenalTransformDrawWhenTranform(Unit u){

    }
    public void drawWeaponCrosshair(Unit u){
        
    }
    public float rngFin(){
        return SclRange;
    }
    public float rngFout(){
        return 1-rngFin();
    }
    public float targetFoundFadeIn(int index,float speed,Unit u,float range){
        boolean t = (target = findTarget(u,u.aimY,u.aimY,range,u.type.weapons.contains(w->w.bullet.collidesAir),u.type.weapons.contains(w->w.bullet.collidesGround))) != null;
        return prog[index]=MoveStaightTowards(prog[index],t ? 1:0,speed*Time.delta*continues(!Vars.state.isPaused()));
    }
    public float targetFoundFadeOut(int index,float speed,Unit u,float range){return 1-targetFoundFadeIn(index,speed,u,range);}
    public float progressDelayIndex(int index ,float TimeDelay,float speed,int time){
        if(!r){
            if(timeDelaySecondIndex[index]>=TimeDelay){
                timeDelaySecondIndex[index]=0;
                r = true;
            }
            else timeDelaySecondIndex[index]+=Time.delta*continues(!Vars.state.isPaused());
        }
        else{
            ProgIndex[index] = MoveStaightTowards(ProgIndex[index],1,speed*Time.delta/60f);
            if(ProgIndex[index]>=1){
                ProgIndex[index]=0;
                r=false;
                t[index]++;
            }
        }
        if(t[index]>=time)t[index]=0;
        return t[index]+ProgIndex[index];
    }
    public float fslope(float p){
        return (p>=0.5f? p:1-p)*2;
    }
    public Teamc findTarget(Unit unit, float x, float y, float range, boolean air, boolean ground){
        return Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground), t -> ground);
    }
    public float CrossHairRotation(Unit u){
        return Angles.angle(u.x,u.y,u.aimX,u.aimY);
    }
    public float ShortestReload(Unit u){
        float p[]= new float[u.mounts.length],smallest; 
        for(int i=0;i<u.mounts.length;i++){
            p[i] = u.mounts[i].reload;
        }
        smallest = Min(p);
        return smallest;
    }
    @Override
    public void draw(Unit u){
        SclRange = sclRange(u);
        drawWeaponCrosshair(u);
        Draw.reset();
    }
}
