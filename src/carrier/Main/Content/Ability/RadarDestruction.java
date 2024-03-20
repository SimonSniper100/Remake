package carrier.Main.Content.Ability;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.struct.Seq;
import arc.util.Time;
import carrier.Main.MathComplex;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;


public class RadarDestruction extends Skill {
    public float life=0f,skillCooldown,radarRadius,radarRadiusDamage,size;
    public boolean run;
    public Color color = Color.white;
    public Effect RadarEffect,BlastEffect;
    public Unit unit;
    public NDEffect ye = new NDEffect();
    Teamc target;
    private float timer;
    protected float results,lstResults;
    @Override
    public void update(Unit u){
        draw(u);
        timer+=Time.delta;
        if(u instanceof TransformEntity e){
            if(timer>=skillCooldown && e.TransformNow){
                RadarEffect.at(u);
                timer=0f;
            } 
            else if(!e.TransformNow){
                timer=0f;                                        
            }                                                                 
        }
        if(run){
            Damage.damage(u.team, u.x, u.y, radarRadius,radarRadiusDamage*0.9f);
            Seq<Unit> us= Groups.unit.intersect(u.x-radarRadius,u.y-radarRadius,2*radarRadius,2*radarRadius);
            for(var usd:us){
                if(usd.team != u.team && usd.hittable()){
                    //Ememy decteced and can hittable
                    unit = usd;
                    usd.apply(StatusEffects.melting,5f*60f);
                    if(BlastEffect != null){
                        BlastEffect.at(usd,true);
                        
                    };
                }
            }
            run=false;
        }             
    }
    @Override
    public void draw(Unit u){
        RadarEffect = new Effect(2f*60f,e->{
            if(u.dead)return;
            float fslopeCut = 5f*(e.fslope()<0.2f?e.fslope()*0.2f:0.2f);
            float finCut = 2f*(e.finpow()<0.5f?e.finpow():0.5f);
            float b = 360f * e.fin(Interp.pow4);
            Draw.color(color);
            Draw.z(Layer.effect-1);
            Draw.alpha(fslopeCut);
            Lines.stroke(10f*finCut);
            Lines.circle(u.x, u.y, radarRadius*finCut+1);
            Fill.circle(u.x, u.y, 7);
            Lines.line(u.x,u.y,MathComplex.dx(u.x,radarRadius*finCut+1,b),MathComplex.dy(u.y,radarRadius*finCut+1.001f,b));
            Draw.reset();
            Draw.color(Color.valueOf("fffff0"));
            Draw.alpha(fslopeCut);
            Draw.z(Layer.effect);
            Lines.stroke(4f*finCut);
            Lines.circle(u.x, u.y, radarRadius*finCut);
            Lines.line(u.x,u.y,MathComplex.dx(u.x,radarRadius*finCut+1,b),MathComplex.dy(u.y,radarRadius*finCut+1.001f,b));
            life = e.fin();
            float diff=Diffenals(b)*1.5f;
            for(int i =0;i<3;i++){
                Draw.color(color);
                Draw.z(Layer.bullet-1f);
                Draw.alpha(1-0.15f*i);
                Fill.tri(u.x,u.y,MathComplex.dx(u.x, radarRadius*finCut+1, b-diff*i*1.2f),MathComplex.dy(u.y, radarRadius*finCut+1, b-diff*i*1.2f),MathComplex.dx(u.x, radarRadius*finCut, b-diff*(i+1)*1.2f),MathComplex.dy(u.y, radarRadius*finCut+1, b-diff*(i+1)*1.2f));
            }
            if(e.fin()>=0.99f &&!run){
                run = true;
            }
            
        });
        target = Units.closestEnemy(u.team,u.x,u.y,radarRadius,s->!s.dead);
        if(Found(target)){
            Lines.stroke(3f,color);
            Lines.circle(target.x(), target.y(), 4);
        }

    }
    public boolean Found(Teamc t){
        return t != null;
    }
}
