package carrier.Main.Content.Ability;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.struct.Seq;
import arc.util.Time;
import carrier.Main.MathComplex;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;


public class RadarDestruction extends PassiveSkill {
    public float life=0f,skillCooldown,radarRadius,radarRadiusDamage,size,healCount;
    private boolean run;
    public int EffectCount=3,EffectRandCount=2;
    public Color color = Color.white;
    public Effect RadarEffect,BlastEffect;
    public Unit unit;
    private float timer;
    public boolean healApply;
    protected float results,lstResults;
    @Override
    public void update(Unit u){
        draw(u);
        timer+=Time.delta;
        if(u instanceof TransformEntity e){
            if(timer>=skillCooldown && e.TransformNow&&
                (Units.closestEnemy(u.team, u.x, u.y,radarRadius, s->!s.dead())!=null ||(healApply&&healCount>0&&Units.closestEnemy(Team.get(255), u.x, u.y,radarRadius, s->!s.dead())!=null))){
                RadarEffect.rotWithParent(true).followParent(true);
                RadarEffect.at(u,true);
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
                boolean checkedRange =Mathf.dst(u.x, u.y,usd.x,usd.y)<=radarRadius; 
                if(usd.team != u.team && usd.hittable()&&checkedRange){
                    //Ememy decteced and can hittable
                    unit = usd;
                    Rand r = new Rand(usd.id);
                    usd.apply(StatusEffects.melting,5f*60f);
                    for(int i = 0;i<EffectCount+r.range(EffectRandCount);i++){
                        if(BlastEffect != null){
                            BlastEffect.followParent(false);
                            BlastEffect.at(usd.x+7f*r.random(-1,1),usd.y+7f*r.random(-1,1),usd.rotation,usd);
                        }
                    }
                    if(usd.dead){
                        Events.fire(new EventType.UnitDestroyEvent(usd));
                    }
                }
                else if(usd.team == u.team&&usd.damaged()&&healApply&&healCount>0&&checkedRange){
                    usd.heal(healCount);
                }
            }
            run=false;
        }             
    }
    @Override
    public void draw(Unit u){
        RadarEffect = new Effect(2f*60f,e->{
            if(u.dead)return;
            float fslopeCut = 5f*(Math.min(e.fslope(), 0.2f));
            float finCut = 2f*(Math.min(e.finpow(), 0.5f));
            float b = 360f * e.fin(Interp.pow4);
            Draw.color(color);
            Draw.z(Layer.effect-1);
            Draw.alpha(fslopeCut);
            Lines.stroke(10f*finCut);
            Lines.circle(e.x, e.y, radarRadius*finCut+1);
            Fill.circle(e.x, e.y, 7);
            Lines.line(e.x,e.y,MathComplex.dx(e.x,radarRadius*finCut+1,b),MathComplex.dy(e.y,radarRadius*finCut+1.001f,b));
            Draw.reset();
            Draw.color(Color.valueOf("fffff0"));
            Draw.alpha(fslopeCut);
            Draw.z(Layer.effect);
            Lines.stroke(4f*finCut);
            Lines.circle(e.x, e.y, radarRadius*finCut);
            Lines.line(e.x,e.y,MathComplex.dx(e.x,radarRadius*finCut+1,b),MathComplex.dy(e.y,radarRadius*finCut+1.001f,b));
            life = e.fin();
            float diff=MathComplex.DeltaVaule(b)*1.5f;
            for(int i =0;i<3;i++){
                Draw.color(color);
                Draw.z(Layer.bullet-1f);
                Draw.alpha(1-0.15f*i);
                Fill.tri(e.x,e.y,MathComplex.dx(e.x, radarRadius*finCut+1, b-diff*i*1.2f),MathComplex.dy(e.y, radarRadius*finCut+1, b-diff*i*1.2f),MathComplex.dx(e.x, radarRadius*finCut, b-diff*(i+1)*1.2f),MathComplex.dy(e.y, radarRadius*finCut+1, b-diff*(i+1)*1.2f));
            }
            if(e.fin()>=0.99f &&!run){
                run = true;
            }
            
        });
        Units.nearbyEnemies(u.team,u.x,u.y,radarRadius ,e->{
            if(Found(e)){
                Lines.stroke(3f,color);
                Lines.circle(e.x(), e.y(), 10);
            }
        });

    }
    public boolean Found(Teamc t){
        return t != null;
    }
}
