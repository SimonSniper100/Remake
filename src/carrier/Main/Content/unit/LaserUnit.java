package carrier.Main.Content.unit;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.util.Tmp;
import carrier.Main.Content.AI.NoneAI;
import carrier.Main.Content.AI.SucideAI;
import carrier.Main.Content.Type_and_Entity.Other.LaserUnitType;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class LaserUnit {
    public static UnitType LaserDrone;
    public static void loadDrone(){
        LaserDrone= new LaserUnitType("LaserDrone"){{
            health = 100f;
            speed=4.2f;
            hidden= true;
            shadowElevation=0;
            accel = 0.07f;
            drag = 0.04f;
            engines.add(new UnitEngine(0f,0f,0.01f,0f));
            hitSize = 10f;
            rotateSpeed = 4.5f;
            controller =u-> new SucideAI(){{
                TimeToDead=4*60f;
                TimeToDeadIfShot=3.075f*60f;
            }};
            aiController=NoneAI::new;
            parts.add(new RegionPart("-cast-r"){{
                moveRot = 45f;
                x=-0.75f;
                y=2f;
                outline = false;
            }},new RegionPart("-cast-l"){{
                moveRot = -45f;
                x=0.75f;
                y=2f;
                outline = false;
            }});
            weapons.add(new Weapon(){{
                rotate = false;
                x=0;
                y=0;
                mirror=false;
                reload = 7*60;
                continuous = true;
                shootStatus = StatusEffects.unmoving;
                shootStatusDuration = 6f*60f;
                bullet = new ContinuousLaserBulletType(40){
                    {
                    length = 8*25;
                    continuous =pierceArmor= pierce=true;
                    shootSound=Sounds.beam;
                    width = 0.2f;
                    lifetime = 3.1f*60f;
                    shootEffect = new Effect(60,e->{
                        Draw.z(Layer.effect);
                        Draw.color(Color.yellow,e.fout());
                        Tmp.v1.trns(e.rotation, 10+e.fin()*20f);
                        Lines.ellipse(Tmp.v1.x + e.x, Tmp.v1.y + e.y , 0.5f*e.fin()+0.1f,8*1,16, e.rotation);
                        Tmp.v2.trns(e.rotation, 10+e.fin()*10f);
                        Lines.ellipse(Tmp.v2.x + e.x, Tmp.v2.y + e.y , 0.5f*e.fin()+0.1f,8f*0.75f, 12,  e.rotation);
                        Lines.stroke(2f*e.fout());
                    });
                }
            @Override
            public void draw(Bullet b){
                float fout = Mathf.clamp(b.time > b.lifetime - fadeTime ? 1f - (b.time - (lifetime - fadeTime)) / fadeTime : 1f);
                float realLength = Damage.findLength(b, length * fout, laserAbsorb, pierceCap);
                float rot = b.rotation();

                for(int i = 0; i < colors.length; i++){
                    Draw.color(Tmp.c1.set(colors[i]).mul(1f));

                    float colorFin = i / (float)(colors.length - 1);
                    float baseStroke = Mathf.lerp(strokeFrom, strokeTo, colorFin);
                    float stroke = (width + 1) * fout * baseStroke;
                    float ellipseLenScl = Mathf.lerp(1 - i / (float)(colors.length), 1f, pointyScaling);

                    Lines.stroke(stroke);
                    Lines.lineAngle(b.x, b.y, rot, realLength - frontLength, false);

                    //back ellipse
                    Drawf.flameFront(b.x, b.y, divisions, rot + 180f, backLength, stroke / 2f);

                    //front ellipse
                    Tmp.v1.trnsExact(rot, realLength - frontLength);
                    Drawf.flameFront(b.x + Tmp.v1.x, b.y + Tmp.v1.y, divisions, rot, frontLength * ellipseLenScl, stroke / 2f);
                }

                Tmp.v1.trns(b.rotation(), realLength * 1.1f);

                Drawf.light(b.x, b.y, b.x + Tmp.v1.x, b.y + Tmp.v1.y, lightStroke, lightColor, 0.7f);
                Draw.reset();
                }};
            }});
        }};
    }
}
