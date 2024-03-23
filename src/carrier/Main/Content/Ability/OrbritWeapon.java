package carrier.Main.Content.Ability;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import arc.util.Tmp;
import carrier.Main.MathComplex;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.Vars;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.ParticleEffect;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class OrbritWeapon extends Skill{
    public float timer,duration,timeDelay,beamRadius,damage,firstDamage,lifeTimeEffect=2*60,shockWaveIncrease;
    private float actTime,delay;
    public float skillCooldown,Velocity,shake,damageInterval;
    public Color color;
    public Color[] ExtraColor = {Color.valueOf("fffff0")}; 
    private float lastMoveX,lastMoveY,times;
    private boolean clicked,boom,init = true, readyToShoot,AlreadyShoot,ColorScreenEffectRun;
    private float h,px,py,p,dp,ap,b,d,s,de;
    BulletType etou = new BulletType();
    private Vec2 v = new Vec2();
    private Effect ParticalEffect;
    private Effect Colorscreen= new Effect(lifeTimeEffect,200000000f,e->{
        Draw.z(Layer.space-1);
        Draw.color(color.cpy());
        Draw.alpha(e.fout());
        Fill.square(e.x,e.y,1000000*8);
    });
    public Effect BoomEffect,BeamEffect;
    @Override
    public void update(Unit u){
        if(init){
            lastMoveX=u.aimX;
            lastMoveY=u.aimY;
            init = false;
        }
        px = VelocityAimMoveX(u.aimX,Velocity,10);
        py = VelocityAimMoveY(u.aimY,Velocity,10);
        readyToShoot = InPostionAim(u);
        if(u instanceof TransformEntity c && c.TransformNow){
            timer +=Time.delta;
            if(timer>= skillCooldown){
                if(u.isShooting && !clicked && actTime <=0f && delay <=0 && InPostionAim(u)){
                    clicked = true;
                    actTime = duration;
                    delay = timeDelay;
                    ColorScreenEffectRun = true;
                    boom = true;
                    times=0;
                }
                if(clicked&&(readyToShoot||AlreadyShoot)){
                    AlreadyShoot = true;
                    if(delay >0){
                        delay-=Time.delta;
                        return;
                    }
                    
                    if(actTime >0){
                        actTime -=Time.delta;
                        etou.buildingDamageMultiplier = 0.00001f;
                        var test = etou.create(u, px, py, 0);
                        if(boom){
                            Damage.damage(u.team,px, py, beamRadius+shockWaveIncrease, firstDamage/60,false,true,true,true,test);
                            if(BoomEffect != null){
                                BoomEffect.at(px,py,0);
                            }
                            boom = false;
                        }
                        if((times+=Time.delta) >= damageInterval){
                            Damage.damage(u.team,px, py, beamRadius, damage*ProgressDelay(actTime/duration, 0.3f)/damageInterval,true,true,true,true,test);
                            times=0;
                            if(BeamEffect != null){
                                BeamEffect.at(px,py);
                            }
                        }
                        Vars.renderer.shake(shake*ProgressDelay(actTime/duration, 0.3f), 30*ProgressDelay(actTime/duration, 0.3f));
                        for(float i = -beamRadius*s;i<beamRadius*s;i+=2f){
                            ParticalEffect = new ParticleEffect(){{
                                colorFrom = color.cpy();
                                colorTo = color.cpy();
                                particles = 1;
                                sizeFrom = 20f;
                                sizeTo =0f;
                                lifeTimeEffect = 100;
                                lenFrom=0f;
                                lenTo = 10;
                                length = 7f;
                                randLength = true;
                                layer = Layer.effect;
                            }};
                            if(ParticalEffect != null){
                                ParticalEffect.at(px+i, py-15*1.1f);
                            }
                        }
                        if(ColorScreenEffectRun){
                            var ohno = Groups.unit.intersect(u.x-100000,u.y-100000,100000*2,100000*2);
                            for(var wat:ohno){
                                Colorscreen.at(Core.camera.position,true);
                                Colorscreen.at(wat,true);
                            }
                            ColorScreenEffectRun = false;
                        }
                    }
                    else{
                        readyToShoot = false;
                        ColorScreenEffectRun = true;
                        clicked = false;
                        timer=0f;
                        AlreadyShoot = false;
                    }
                }
                else{
                    AlreadyShoot = false;
                    timer = skillCooldown;
                    ColorScreenEffectRun = true;
                }
            }
        }
        else {
            timer =0f;
            ColorScreenEffectRun = false;
            times=0;
            actTime = 0;
            delay = 0;
            clicked = false;
            AlreadyShoot = false;
            readyToShoot = false;
        }
    }
    @Override
    public void draw(Unit u){
        Core.camera.bounds(Tmp.r1).overlaps(Tmp.r2.setCentered(px, py, Colorscreen.clip));
        p = timer/skillCooldown;
        dp =delay/timeDelay;
        ap= actTime/duration;
        b = Mathf.sin(ap*Mathf.PI2*80);
        d= InPostionAim(u)&&de<=0? Mathf.sin((h+=Mathf.PI*0.02f*(Vars.state.isPlaying()?1:0))):(h=0);
        s =ProgressDelay(ap, 0.3f);
        if(InPostionAim(u))de = Math.max(de-=Time.delta*0.05f,0);
        else de = Math.min(de+=Time.delta*0.05f,1);
        if(u instanceof TransformEntity t && t.TransformNow){
            Draw.z(Layer.bullet);
            Draw.mixcol(Color.red,color.cpy(), p);
            Lines.stroke(5);
            for(int i =1;i<=4;i++){
                v.trns(-45+90*i,(beamRadius+30*de)+10*(InPostionAim(u)?d:0));
                float dx = MathComplex.dx(px+v.x,20*(i==1||i==4?-1:1),0),
                    dy = MathComplex.dy(py+v.y,20*(i==1||i==2?-1:1),90);
                    Lines.line(px+v.x,py+v.y,dx,py+v.y);
                    Lines.line(px+v.x,py+v.y,px+v.x,dy);
            }
            Draw.reset();
            for(float i = 0;i<360*p*(ap<=0f ? 1:ap);i+=0.5f){
                float r = 240f;
                Draw.z(Layer.bullet);
                Draw.color(color.cpy());
                Lines.stroke(5);
                Lines.line(u.x+r*Mathf.cosDeg(i),u.y+r*Mathf.sinDeg(i),u.x+r*Mathf.cosDeg(i+0.5f),u.y+r*Mathf.sinDeg(i+0.5f));
            }
            if((ap>=0&&dp<=0.3f)){
                Draw.color(color.cpy());
                Fill.quad(
                    (px-beamRadius*(1+b*0.05f)*s), 
                    (py+3000*ProgressDelay(dp, 0.3f)), 
                    (px+beamRadius*(1+b*0.05f)*s), 
                    (py+3000*ProgressDelay(dp, 0.3f)), 
                    (px-beamRadius*(1+b*0.05f)*s), 
                    py+2000*8, 
                    (px+beamRadius*(1+b*0.05f)*s), 
                    py+2000*8);
                if(ExtraColor != null){
                    for(int i=0;i<ExtraColor.length;i++){
                        Draw.color(ExtraColor[i].cpy());
                        Fill.quad(
                            (px-beamRadius*(1+b*0.05f)*(1-i/beamRadius)*s), 
                            (py+3000*ProgressDelay(dp, 0.3f)*(1-i/beamRadius)), 
                            (px+beamRadius*(1+b*0.05f)*(1-i/beamRadius)*s),  
                            (py+3000*ProgressDelay(dp, 0.3f)*(1-i/beamRadius)), 
                            (px-beamRadius*(1+b*0.05f)*(1-i/beamRadius)*s), 
                            py+2000*8, 
                            (px+beamRadius*(1+b*0.05f)*(1-i/beamRadius)*s), 
                            py+2000*8
                        );
                    }
                }
            }
        } 
    }
    @Override
    public void addStats(Table t){

    }
    //Prevent Move To Fast
    public float VelocityAimMoveX(float xAim ,float Velocity,float fixNumber){
        if(lastMoveX == 0 || Float.isNaN((lastMoveX))||Float.isInfinite((lastMoveX))){
            lastMoveX = xAim;
        }
        if(Math.round(lastMoveX/fixNumber) != Math.round(xAim/fixNumber)){
            lastMoveX = lastMoveX > xAim ? lastMoveX - (Velocity/Time.delta)*(Vars.state.isPlaying()?1:0): lastMoveX + (Velocity/Time.delta)*(Vars.state.isPlaying()?1:0);

        }
        else {
            lastMoveX = xAim;
        }
        return lastMoveX;
    }
    public float VelocityAimMoveY(float yAim ,float Velocity,float fixNumber){
        if(lastMoveY == 0 || Float.isNaN((lastMoveY))||Float.isInfinite((lastMoveY))){
            lastMoveY = yAim;
        }
        if(Math.round(lastMoveY/fixNumber) != Math.round(yAim/fixNumber)){
            lastMoveY = lastMoveY > yAim ? lastMoveY - (Velocity/Time.delta)*(Vars.state.isPlaying()?1:0): lastMoveY + (Velocity/Time.delta)*(Vars.state.isPlaying()?1:0);
        }
        else {
            lastMoveY = yAim;
        }
        return lastMoveY;
    }
    public float ProgressDelay(float main,float percent){
        return main>=percent ? 1: 1-((percent-main)/percent);
    }
    private boolean InPostionX(Unit u){
        return Math.round(lastMoveX/10) == Math.round(u.aimX/10);
    }
    private boolean InPostionY(Unit u){
        return Math.round(lastMoveY/10) == Math.round(u.aimY/10);
    }
    private boolean InPostionAim(Unit u){
        return InPostionX(u) && InPostionY(u);
    }
}
