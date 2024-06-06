package carrier.Main.Content.unit;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import carrier.Main.Content.Bullet.PointCritBullet;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Type_and_Entity.Supporter.SupporterType;
import mindustry.content.Items;
import mindustry.content.UnitTypes;
import mindustry.entities.Effect;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.weapons.RepairBeamWeapon;

public class SupporterUnit {
    public static UnitType Terebusta;
    public static Weapon BigCannon,RepairCannon;
    public static void loadWeapon(){
        RepairCannon = new RepairBeamWeapon("carrier-mod-repair-gun"){{
            repairSpeed = 0.2f;
            fractionRepairSpeed =0.005f;
            targetBuildings =mirror = true;
            pulseRadius = 1f;
            pulseStroke = 1f;
            reload = 0.5f;
            rotateSpeed = 1f;
            useAttackRange=false;
        }};
        BigCannon = new Weapon("carrier-mod-railgun"){{
            mirror = true;
            reload = 1.5f*60f;
            recoil = 0.5f;
            shoot.shotDelay = 10f;
            predictTarget = false;
            shootY = 4f;
            rotate = true;
            predictTarget = false;
            shootSound = Sounds.laserblast;
            rotateSpeed = 1.5f;
            bullet = new PointCritBullet(250, 5f, "", 30, 300){{
                hitEffect = new Effect(30, 200f, e -> {
                    Draw.color(Color.white);
                    for(int i : Mathf.signs){
                        Drawf.tri(e.x, e.y, 10f * e.fout(), 60f, e.rotation + 140f * i);
                    }
                    Draw.reset();
                    Draw.color(Color.white.cpy().a(0.66f*e.foutpow()));
                    NDEffect.drawShockWave(e.x, e.y, 75f, 0f, -e.rotation - 90f, 60*e.fin(Interp.pow2Out)+1, 10f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                });
                shootEffect = new Effect(24f, e -> {
                    e.scaled(10f, b -> {
                        Draw.color(Color.white, Color.lightGray, b.fin());
                        Lines.stroke(b.fout() * 3f + 0.2f);
                        Lines.circle(b.x, b.y, b.fin() * 50f);
                    });
            
                    Draw.color(Color.valueOf("fffff0"));
                    for(int i : Mathf.signs){
                        Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
                    }
                });
                hitColor = trailColor = Color.white;
                trailEffect = new Effect(20f,e->{
                    Draw.color(Color.white);

                for(int i : Mathf.signs){
                Drawf.tri(e.x, e.y, 10f * e.fout(), 24f, e.rotation + 90 + 90f * i);
                }

                 Drawf.light(e.x, e.y, 60f * e.fout(), Color.white, 0.5f);
                });
                CritEffect= new MultiEffect(NDEffect.BlackHoleData(Color.white,2, 80, 45,60f,7f),
                new Effect(80,e->{
                    Draw.color(Color.white.cpy().a(0.66f*e.fout()));
                    NDEffect.drawShockWave(e.x, e.y, 75f, 0f, -e.rotation - 90f, 120*e.fin(Interp.pow2Out)+1, 15f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                }));
            }};
        }};
    }
    public static void loadSupporter(){
        loadWeapon();
        Terebusta = new SupporterType("Terebusta"){{
            health = 6000f;
            armor = 15f;
            speed = 1f;
            accel = 0.05f;
            drag = 0.07f;
            flying=lowAltitude=true;
            firstRequirements = totalRequirements = cachedRequirements = new ItemStack[]{
                new ItemStack(Items.plastanium,300),
                new ItemStack(ModItem.QuatanzationCrystal,100),
                new ItemStack(Items.silicon,650),
                new ItemStack(Items.titanium,400),
                new ItemStack(Items.metaglass,100)
            };
            hitSize = UnitTypes.antumbra.hitSize*0.7f;
            rotateSpeed = 1f;
            abilities.add(new RepairFieldAbility(300f, 4*60, 8*10));
            weapons.addAll(MoveCopy(RepairCannon, 18f, -5f,true),MoveCopy(RepairCannon, 4.5f, 7f,false),
                MoveCopy(BigCannon,-8.5f, 0f,true)
            );
        }};
    }
    public static Weapon copyFix(Weapon weapon, float x, float y,boolean mirror,float OffSet,float delay,boolean display){
		Weapon n = weapon.copy();
		n.x = x;
		n.y = y;
        n.mirror = mirror;
        n.baseRotation = OffSet;
        n.shoot.firstShotDelay = delay;
        n.display = display;
		return n;
	}
    public static Weapon MoveCopy(Weapon weapon,float x,float y,boolean display){
        Weapon w = weapon.copy();
        w.x=x;
        w.y=y;
        w.display = display;
        return w;
    }
}
