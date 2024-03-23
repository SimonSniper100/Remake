package carrier.Main.Content.Type_and_Entity.Transformer;

import arc.Core;
import arc.KeyBinds;
import arc.KeyBinds.KeyBind;
import arc.KeyBinds.KeybindValue;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.Content.Special.EntityRegister;
import mindustry.entities.Effect;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.io.TypeIO;
//Copy from Transform Entity but replace flyingUnit
public class TransformFlying extends UnitEntity{
    public float second,countDownSecond;
    public boolean TransformNow = false,runCountEffect;
    public float TimeTransform,CountDownTime,radius = 300f;
    public float velocity = vel().len();
    public Color color = Color.white;
    public String nameSprite;
    private Effect TransformEffect = new MultiEffect(new Effect(4*60,e->{
        if(e.data instanceof Unit u){
            e.rotation = u.rotation;
        }
        Draw.mixcol(color,color, e.fout());
        Draw.alpha(e.fout());
        Lines.stroke(1f);
        Draw.rect(Core.atlas.find("carrier-mod-"+nameSprite+"-white"),e.x,e.y,e.rotation-90);
    }),
    new Effect(5*60,e->{
        for(int i =0;i<2;i++){
            Draw.color(color);
            Draw.alpha(e.foutpow());
            Lines.stroke(e.foutpow()*10*i/2);
            Lines.circle(e.x, e.y, radius*e.finpow()*i);
        }
    })
    );;
    @Override
    public int classId(){
        return EntityRegister.getID(TransformFlying.class);
    }
    @Override
    public void update(){
        super.update();
        //Check heatlh unit, does not dead instanlly it will not call effect
        //Check heatlh unit, does not dead instanlly it will not call effect
        if((healthf()<0.6f||(Core.input.keyTap(KeyCode.n) && isPlayer()))&&!dead() && second <=0f && countDownSecond <=0f){
            //Transform Time Heeee
            TransformNow = true;
            second = TimeTransform;
            countDownSecond = CountDownTime;
            TransformEffect.rotWithParent(true);
            TransformEffect.followParent(true);
            TransformEffect.at(this,true);
            runCountEffect = true;
        }
        //Some buff unit
        if(second >0f && countDownSecond >0f){
            second -= Time.delta;
        }
        //If run out
        if(second <= 0f && countDownSecond >0f){
            countDownSecond -=Time.delta;
            TransformNow = false;
            //run 1 time effect prevent spam effect to much cause lag
            if(runCountEffect){
                TransformEffect.rotWithParent(true);
                TransformEffect.followParent(true);
                TransformEffect.at(this,true);
                runCountEffect = false;
            }
        }
    }
    @Override
    public void read(Reads r){
       super.read(r);
        second = r.f();
        countDownSecond = r.f();
        TimeTransform = r.f();
        CountDownTime = r.f();
        TransformNow = r.bool();
        color = TypeIO.readColor(r);
        nameSprite = r.str();
        radius = r.f();
        runCountEffect = r.bool();
    }
    @Override
    public void write(Writes w){
        super.write(w);
        w.f(second);
        w.f(countDownSecond);
        w.f(TimeTransform);
        w.f(CountDownTime);
        w.bool(TransformNow);
        TypeIO.writeColor(w, color);
        w.str(nameSprite);
        w.f(radius);
        w.bool(runCountEffect);
    }
}
