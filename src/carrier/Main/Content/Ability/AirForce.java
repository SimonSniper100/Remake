package carrier.Main.Content.Ability;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import carrier.Main.Content.AI.AirForceAI;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.entities.Effect;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AirForce extends Skill{
    public Unit airforce;
    public Color color;
    public Effect airforceEffect,spawnEffect;
    public UnitType type;
    public float x,y,roadLenght = 10000f;
    public boolean run=false,oneTime = true;
    public String name;

    public AirForce(float x,float y,UnitType type,Color color,String name){
        this.type=type;
        this.x=x;
        this.y=y;
        this.color = color;
        this.name = name;
    }
    @Override
    public void update(Unit u){
        setupEffect(u);
        float r = u.rotation-Diffenals(u.rotation);
        if(isRip(airforce)){
            if(u instanceof TransformEntity t){
                if(t.TransformNow && oneTime){
                    airforce = type.create(u.team);
                    float sx = u.x + Angles.trnsx(r, y,x),
                        sy = u.y + Angles.trnsy(r, y, x);
                    airforceEffect.at(u,r-Diffenals(r));
                    airforce.rotation = r;
                    airforce.set(sx,sy);
                    if(run){
                        Effect.shake(6,1*60,sx,sy);
                        airforce.add();
                        spawnEffect = new Effect(2*120,e->{
                            Draw.color(color);
                            Draw.alpha(e.foutpow());
                            Lines.stroke(5f*e.fout());
                            Lines.circle(airforce.x, airforce.y, type.hitSize*5*e.fin());
                            Draw.rect(Core.atlas.find("carrier-mod-"+name+"-white"),airforce.x,airforce.y,airforce.rotation-90);
                        });
                        spawnEffect.at(airforce.x,airforce.y,airforce.rotation,color,airforce);
                        airforce.controller(new AirForceAI());
                        run=false;
                        oneTime = false;
                        if (airforce.controller() instanceof AirForceAI ai){
                            ai.caller=u;
                            ai.x=x;
                            ai.y=y;
                        }
                    }
                }
                else if(!t.TransformNow){
                    oneTime = true;
                }
            }
        }
        else{
            if(u instanceof TransformEntity t){
                if(!t.TransformNow){
                    airforce.remove();
                }
                else if (airforce.controller() instanceof AirForceAI ai){
                    ai.caller=u;
                    ai.x=x;
                    ai.y=y;
                }
            }
        }
    }
    public boolean isRip(Unit u){
        return u==null||u.dead()||!u.isValid()||u.isNull();
    }
    @Override
    public void draw(Unit u){
        
    }
    @Override
    public void death(Unit u){
        if(!isRip(airforce)){
            airforce.kill();
        }
    }
    public void setupEffect(Unit u){
        float r = u.rotation-Diffenals(u.rotation);
        airforceEffect= new Effect(120,e->{
            for(int i = 0;i<(int)roadLenght/1000;i++){
                Draw.color(color);
                Draw.alpha(e.fin(Interp.pow2In)*e.fout(Interp.pow3In));
                Lines.stroke(4);
                float sx = u.x + Angles.trnsx(r, y-i*type.hitSize*1.12f-40,x),
                sy = u.y + Angles.trnsy(r, y-i*type.hitSize*1.12f-40, x);
                Draw.rect(Core.atlas.find("carrier-mod-ArrowTexture"), sx,sy,r);
            }
            Draw.reset();
            if(e.fin()>=0.9888f&&!run){
                run =true;
            }   
        });
    }
}
