package carrier.Main.Content.Type_and_Entity.Transformer;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.FloatSeq;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import carrier.Main.CarrierVars;
import carrier.Main.Content.Ability.Skill;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Special.EntityRegister;
import carrier.Main.SomeThing.ModKeyBinds;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;
import mindustry.io.TypeIO;
import mindustry.type.UnitType;
//Copy from Transform Entity but replace flyingUnit
public class TransformFlying extends UnitEntity{
    @Override
    public int classId(){
        return EntityRegister.getID(TransformFlying.class);
    }
    public Skill[] skills={};
    public Vec2 v1 = new Vec2();
    public float invFrameSecond = 0.02f;
    protected float TimeUpdate;
    float lastDamage = 0f,invFrame;
    public float second,countDownSecond;
    public float hitBoxX=type.hitSize,hitBoxY=type.hitSize;
    public boolean TransformNow = false,runCountEffect,draw,boom,AutoTransformWhenHealRemain = true;
    public float TimeTransform,CountDownTime,radius = 300f;
    public float velocity = vel().len();
    public Color color = Color.white;
    public String nameSprite;
    public Effect ExtraTransformEffect = Fx.none;
    private Effect TransformEffect = new MultiEffect(ExtraTransformEffect,new Effect(4*60,e->{
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
    );
    @Override
    public void update(){
        super.update();
        //Check heatlh unit, does not dead instanlly it will not call effect
        if(((healthf()<0.6f&&AutoTransformWhenHealRemain)||(Core.input.keyTap(ModKeyBinds.Transform) && isPlayer()))&&!dead() && second <=0f && countDownSecond <=0f&&!TransformNow){
            //Transform Time Heeee
            TransformNow = true;
            second = TimeTransform;
            countDownSecond = CountDownTime;
            runCountEffect = true;
            boom = true;
        }
        if(Core.input.keyTap(ModKeyBinds.UnTransform) && isPlayer() &&TransformNow&second >0){
            //RemainTime Will subtract the cooldownTime
            countDownSecond = CountDownTime;
            countDownSecond-=second;
            //Set second to zero to run effect
            second = 0;
            runCountEffect = true;
            TransformNow = false;
        }
        //Some buff unit
        if(second >0f && countDownSecond >0f){
            second -= Time.delta;
        }
        //If run out
        if(second <= 0f && countDownSecond >0f){
            if(boom){
                runCountEffect = true;
                boom = false;
            }
            countDownSecond -=Time.delta;
            TransformNow = false;
            
        }
        //run 1 time effect prevent spam effect to much cause lag
        if(runCountEffect){
            TransformEffect.rotWithParent(true);
            TransformEffect.followParent(true);
            TransformEffect.at(this,true);
            runCountEffect = false;
        }
        if(invFrame > 0 && invFrameSecond >0f){
            invFrame -= Time.delta;
            if(invFrame <= 0f){
                lastDamage = 0f;
            }
        }
        TimeUpdate+=Time.delta;
        if(TimeUpdate>3600){
            TimeUpdate = Time.delta;
        }
        Skill[] var1 = skills;
        int l = var1.length;
        for(int acc = 0; acc < l; ++acc) {
            Skill s = var1[acc];
            s.update(this);
        }
        
        Units.nearbyEnemies(team, x ,y ,range() * 2f, u-> {
            if(u.controller().toString().contains("Despondency")){
                //Giam Cầm Mode
                //Đố Tới Dc
                v1.trns(rotation(),u.range()*1.5f);
                u.set(x+v1.x,y+v1.y);
                u.rotation = rotation();
                for(WeaponMount w : u.mounts){
                    w.rotate = false;
                    w.shoot = false;
                }
                Seq<WeaponMount> mountseq = new Seq<>();
                mountseq.add(u.mounts);
                FloatSeq dpsCal = new FloatSeq();
                dpsCal.add(mountseq.sumf(d->d.weapon.bullet.continuousDamage()));
                float godDamage = u.maxHealth*(float)Math.pow(u.armor(),u.healthMultiplier()+1)*u.healthMultiplier()*dpsCal.sum()/10000f;
                if(Float.isInfinite(godDamage)||Float.isNaN(godDamage)) godDamage = Float.MAX_VALUE - 1000;
                u.rawDamage(godDamage);
            }
        });
    }
    @Override
    public void rawDamage(float amount){
        if((invFrame<=0||amount >lastDamage) && invFrameSecond >0f){
            float lam = amount;
            amount -= lastDamage;
            lastDamage = lam;
            amount = Math.min(amount, type.health / 220f);
            super.rawDamage(amount);
            invFrame = invFrameSecond*60f;
        }
        else if (invFrameSecond == 0)super.rawDamage(amount);
        
    }
    @Override
    public void draw(){
        super.draw();
        NDEffect.DrawProgressCircle(Layer.effect, radius, 5f, TransformCountFin(), this, color,0);
        Draw.reset();
        if(TransformNow)
        Draw.reset();
    }
    public float TransformCountFin(){
        return second/TimeTransform;
    }
    public float CountDounFin(){
        return countDownSecond/CountDownTime;
    }
    public float CountDounFout(){
        return 1-CountDounFin();
    }
    public float TransformCountFout(){
        return 1-TransformCountFin();
    }
    @Override
    public void read(Reads r){
        super.read(r);
        ExtraTransformEffect = TypeIO.readEffect(r);
        hitBoxX = r.f();
        hitBoxY = r.f();
        second = r.f();
        countDownSecond = r.f();
        TimeTransform = r.f();
        CountDownTime = r.f();
        TransformNow = r.bool();
        AutoTransformWhenHealRemain=r.bool();
        color = TypeIO.readColor(r);
        nameSprite = r.str();
        radius = r.f();
        runCountEffect = r.bool();
        v1 = TypeIO.readVec2(r);
        skills = CarrierVars.ReadSkill(r, skills);
    }
    @Override
    public void write(Writes w){
        super.write(w);
        TypeIO.writeEffect(w, ExtraTransformEffect);
        w.f(hitBoxX);
        w.f(hitBoxY);
        w.f(second);
        w.f(countDownSecond);
        w.f(TimeTransform);
        w.f(CountDownTime);
        w.bool(TransformNow);
        w.bool(AutoTransformWhenHealRemain);
        TypeIO.writeColor(w, color);
        w.str(nameSprite);
        w.f(radius);
        w.bool(runCountEffect);
        TypeIO.writeVec2(w, v1);
        CarrierVars.WriteSkill(w, skills);
    }
    @Override
    public void destroy(){
        super.destroy();
        if (isAdded() && type.killable){
            Skill[] var1 = skills;
            int var2 = var1.length;
            for(int var3 = 0;var3<var2;++var3){
                Skill s = var1[var3];
                s.death(this);
            }
        }
    }
    @Override
    public void readSync(Reads read){
        super.readSync(read);
        skills = CarrierVars.ReadSkill(read, skills);
    }
    @Override
    public void setType(UnitType t){
        super.setType(t);
        if(type instanceof TransformType nt){
            if(skills.length != nt.skills.size){
                skills = new Skill[nt.skills.size];
            }
            for(int i = 0; i < nt.skills.size; ++i){
                skills[i]= ((Skill)nt.skills.get(i)).copy();
            }
        }
    }
    @Override
    public void writeSync(Writes write){
        super.writeSync(write);
        CarrierVars.WriteSkill(write, skills);
    }
    
}