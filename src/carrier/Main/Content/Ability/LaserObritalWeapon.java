package carrier.Main.Content.Ability;

import static mindustry.Vars.tilesize;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import carrier.Main.MathComplex;
import mindustry.Vars;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;


public class LaserObritalWeapon extends Skill{
    public CrossHairSkill crossHair;
    public float LaserRadius,TopLaserRadius,WeaponsDeployDelay = 2,WeaponBrustTimer = 8,LaserDamage=60000,LaserMoveSpeed,Height;
    public Color color= Color.white;
    private final Effect sceneEffect = new Effect(120,200000,e->{
        Draw.color(e.color.cpy().a(e.fout()));
        Fill.square(e.x, e.y, 10000*tilesize);
    });
    private float delaytime,WeaponBrustSecond,posX,posY;
    private boolean runEffect,stop,init = true;
    @Override
    public void update(Unit u){
        super.update(u);
        if(crossHair != null){
            crossHair.update(u);
        }
        if(init){
            posX = u.x;posY=u.y;
            init=false;
        }
        float a =Mathf.angle(u.aimX-posX, u.aimY-posY),wacut = MathComplex.ProgressFadeOutDelay(WeaponBrustSecond/(WeaponBrustTimer*60f), 0.3f);
        posX = MathComplex.MoveStaightTowards(posX, u.aimX, Time.delta*LaserMoveSpeed*Math.abs(Mathf.cosDeg(a)));
        posY = MathComplex.MoveStaightTowards(posY, u.aimY, Time.delta*LaserMoveSpeed*Math.abs(Mathf.sinDeg(a)));
        Log.info(delaytime);
        Log.info(WeaponBrustSecond);
        if(start){
            if(!runEffect&&!stop){
                runEffect = true;
            }
            delaytime =Mathf.clamp(delaytime+Time.delta,0,WeaponsDeployDelay*60); 
            if(delaytime>=WeaponsDeployDelay*60){
                WeaponBrustSecond = Mathf.clamp(WeaponBrustSecond-Time.delta,0,WeaponBrustTimer*60);
                if(WeaponBrustSecond>0){
                    Damage.damage(u.team,posX,posY,LaserRadius+8,(LaserDamage*u.damageMultiplier*wacut)/(60*Time.delta));
                    AdditionalEffect(u);
                    if(runEffect){
                        Units.nearby(null, u.x(), u.y(), 10000*tilesize, e->{
                            sceneEffect.wrap(color).layer(Layer.effect+10).at(e,true);
                        });
                        stop = true;
                        runEffect = false;
                    }
                }
                else{
                    start = false;
                }
            }
        }
        else {
            delaytime = 0;
            stop = false;
            WeaponBrustSecond = WeaponBrustTimer*60;
        }
    }
    public void AdditionalEffect(Unit u){}
    @Override
    public void draw(Unit u){
        super.draw(u);
        float da = delaytime/(WeaponsDeployDelay*60f),wa = WeaponBrustSecond/(WeaponBrustTimer*60f),b = Mathf.absin(4, 8),wacut = MathComplex.ProgressFadeOutDelay(wa, 0.3f),dacut=MathComplex.ProgressFadeInDelay(da, 0.8888f);
        final Vec2 camPos = Core.camera.position;
        float angle = Mathf.angle(posX-camPos.x, posY-camPos.y),dst = Mathf.dst(posX, posY, camPos.x, camPos.y);
        Draw.z(Layer.effect);
        Draw.color(color);
        if(da>=0.9999f){
            Draw.z(Layer.effect-0.01f);
            Fill.circle(posX,posY,(LaserRadius+b)*wacut);
            Fill.circle(posX+dst*Height*Mathf.cosDeg(angle)/32f,posY+dst*Height*Mathf.sinDeg(angle)/32f,(LaserRadius+b+Height/16f)*wacut);
        }
        if(crossHair != null){
            crossHair.draw(u);
        }
    }
}
