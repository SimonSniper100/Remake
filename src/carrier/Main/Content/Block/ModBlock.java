package carrier.Main.Content.Block;

import carrier.Main.MathComplex;
import carrier.Main.Content.Bullet.AccelBulletType;
import carrier.Main.Content.Bullet.CriticalBulletType;
import carrier.Main.Content.Bullet.Shoot.ShootBarrelsContinous;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Part.PartBow;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.entities.Effect;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.BuildVisibility;

import static carrier.Main.MathComplex.SignDirection;
import static mindustry.Vars.tilesize;
import static mindustry.type.ItemStack.with;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
public class ModBlock {
    public static float AH;
    public static Block LargeQuatanzationWall,DestroyerCrossbow,Despot;
    public static Effect ef = new Effect(30f,1f,e->{
        Lines.stroke(0.1f,Items.surgeAlloy.color);
        Fill.circle(e.x,e.y,e.fout()*5);
    });
    public static void LoadFactory(){}
    public static void loadTurret(){
        Despot = new ItemTurret("Despot"){{
            drawer = new DrawMulti(new DrawTurret(){{
                float moveAllX=4;
                for(int i:new int[]{0,1,2,3}){
                    parts.addAll(new RegionPart("-Spine"){{
                        progress = PartProgress.warmup;
                        turretShading =true;
                        mirror =true;
                        moveX =moveAllX;
                        moveRot = 5;
                        y=-3;
                        moveY= -3;
                        rotation=-90;
                        moveRot = 30*i;
                        layerOffset = 0.001f;
                    }});
                }
                parts.add(new RegionPart("-mid"){{
                    layerOffset = 0.001f;
                }});
                for(int i:Mathf.signs){
                    parts.addAll(
                    new RegionPart("-blade-"+SignDirection(i)){{
                        moveX =moveAllX*i;
                        mirror = false;
                        progress = PartProgress.warmup;
                        layerOffset = 0.002f;
                    }},
                    new RegionPart("-back-"+SignDirection(i)){{
                        moveX =moveAllX*i;
                        mirror = false;
                        progress = PartProgress.warmup;
                        layerOffset = 0.002f;
                    }},
                    new RegionPart("-front-"+SignDirection(i)){{
                        moveX =moveAllX*i;
                        mirror = false;
                        progress = PartProgress.warmup;
                        layerOffset = 0.002f;
                    }}
                );
                }
                
            }});
            size = 8;
            health = 32000;
            armor = 128;
            range = 100f*tilesize;
            ammoPerShot = 4;
            reload = 60*10f;
            maxAmmo = ammoPerShot*20;
            consPower = consumePower(4000/60f);
            unitSort = (u,x,y)->{return u.speed();};
            recoil = 1;
            buildCostMultiplier = 2f;
            shootWarmupSpeed = 0.07f;
            minWarmup = 0.95f;
            coolant = consumeCoolant(2f);
            coolantMultiplier = 3f;
            shoot = new ShootBarrelsContinous(){{
                barrels = new float[]{-12,0,0,12,0,0};
                shots = 12;
                shotDelay = 5;
                BulletPerShot = 2;
            }};
            requirements(Category.turret, with(ModItem.QuatanzationCrystal,1000,Items.silicon,1000));
            ammo(ModItem.Diamond,new AccelBulletType(){{
                damage = 2500;
                accelInterp =Interp.exp10Out;
                accelerateBegin = 0.01f;
                accelerateEnd=0.99f;
                velocityBegin = 5;
                velocityIncrease = 6;
                lifetime = 1*60f;
                width = 22f;
                height = 60f;
                trailLength = 15;
                splashDamage = damage/5f;
                splashDamageRadius = damage/(10f*8f);
                lightning = 4;
                lightningCone = 360;
                lightningLength = 4;
                lightningDamage = damage/5f;
                lightningColor = trailColor = Color.valueOf("b8e0f4");
                trailWidth = width/5f;
                
            }});
            
        }};
        DestroyerCrossbow= new ItemTurret("baseCrossbow"){{
            health = 22800;
            armor = 34;
            description = "A Strong Sniper Crossbow, You Can Use Between 2 Ammo, One For Fast Reload Low Damage, One For Low Reload But Massive Damage";
            size = 8;
            reload=15*60f;
            range = 250*8;
            ammoPerShot = 10;
            maxAmmo = ammoPerShot*10;
            recoil = 0;
            consumesPower = true;
            consPower = consumePower(12000/60);
            rotateSpeed = 0.5f;
            shootY = -1f;
            unitSort = UnitSorts.strongest;
            buildCostMultiplier = 2f;
            requirements(Category.turret, with(ModItem.QuatanzationCrystal,1000,Items.silicon,6000));
            shootWarmupSpeed = 0.035f;
            minWarmup = 0.95f;
            coolant = consumeCoolant(1.5f);
            coolantMultiplier = 2f;
            coolEffect = Fx.none;
            drawer = new DrawMulti(new DrawTurret(){{
                parts.addAll(
                    new RegionPart("-back"){{
                        y = -8*2f;
                        moveY = -7f*8;
                        mirror = false;
                        layerOffset = 6f;
                    }},
                    new RegionPart("-front"){{
                        y = 8*2f;
                        moveY = 6.9f*8;
                        layerOffset = 7f;
                        under = true;
                        mirror = false;
                        children.addAll(
                            new PartBow(){{
                                arrowSp ="carrier-mod-bolt";
                                bowMoveY = -8*10*2.05f;
                                bowFY = -10;
                                bowWidth = 72*2f;
                                bowTk = 26f;
                                turretTk = 34;
                                bowHeight = 14f;
                                arrowHeight = AH = 160f;
                                visRad = 13;
                                ArrowYOffset = 30f*1.4f;
                                color = ModItem.QuatanzationCrystal.color;
                                bulletColor = Color.white;
                            }}
                        );
                    }}
                );
            }});
            ammo(
                Items.surgeAlloy,new CriticalBulletType(1000,2.5f*20,"",15f,250f){
                @Override
                public void draw(Bullet b) {
                    super.draw(b);
                    Draw.color(Items.surgeAlloy.color);
                    Draw.rect(Core.atlas.find("carrier-mod-bolt"), b.x, b.y, 32, AH, b.rotation() - 90);
                    Draw.z(Layer.effect);
                    for(int i : Mathf.signs){
                        ef.create(MathComplex.dx(b.x,20*i,b.time*20+b.rotation()),MathComplex.dy(b.y,20*i,b.time*20+b.rotation()), 0, Items.surgeAlloy.color, null);
                    }
                }
                {
                    reloadMultiplier = 15f;
                    CritEffect= new MultiEffect(NDEffect.CircleSpikeBlastOut(Items.surgeAlloy.color,180,90,2),
                            NDEffect.BigBlast(Items.surgeAlloy.color,90f,2f));
                    hitEffect = despawnEffect=Fx.none;
                    splashDamageRadius= 50f;
                    splashDamage = 500f;
                    splashDamageRadiusCrit=20;
                    ammoMultiplier = reloadMultiplier;
                    lifetime=2f*20f;
                    SlapedCrit=SlapedRadiusIncreaseIfCrit=pierce=true;
                    pierceCap= crit ? 4:2;
                    fragBullets=12;
                    fragBullet=new BasicBulletType(){{
                        speed=6f;
                        damage=200;
                    }};
                    lightning=crit?16:14;
                    lightningDamage= crit ? 70:30;
                    lightningLength= 20;
                    lightningCone = 360;
                }}
                ,ModItem.QuatanzationCrystal,new CriticalBulletType(1200,2.5f*20,"",40f,350f){{
                    hitEffect=despawnEffect=Fx.reactorExplosion;
                    trailEffect=Fx.fire;
                    lifetime=2f*20f;
                    shootSound=Sounds.shootBig;
                    splashDamage=damage*10f;
                    splashDamageRadius=70f;
                    splashDamageRadiusCrit=30;
                    SlapedCrit=SlapedRadiusIncreaseIfCrit=pierce=true;
                    shootY=4f*8;
                    pierceCap=3;
                    fragBullets=12;
                    fragBullet=new BasicBulletType(){{
                        speed=2f;
                        damage=3000;
                        
                    }};

                }
                @Override
                public void draw(Bullet b) {
                    super.draw(b);
                    boolean crit =(((CriticalBulletData)b.data).crit);
                    Draw.color(crit ? Color.valueOf("008000").a(4):ModItem.QuatanzationCrystal.color);
                    Draw.rect(Core.atlas.find("carrier-mod-bolt"), b.x, b.y, 32, AH, b.rotation() - 90);
                    Draw.z(Layer.effect);
                }
            }
            );
        }};
    } 
    public static void loadWall(){
        LargeQuatanzationWall = new Wall("quantanzation-wall"){{
            health = 4200;
            size = 2;
            description = "A large wall with high health, but it more cost to bulid. Absorb laser";
            details ="Very cheap ye";
            armor = 20f;
            crushDamageMultiplier=0.2f;
            absorbLasers =insulated= true;
            researchCostMultiplier = 5;
            requirements(Category.defense,BuildVisibility.shown,with(
                ModItem.QuatanzationCrystal,18,
                Items.thorium,12,
                Items.plastanium,10,
                Items.metaglass,10
            ));
        }};
    }
}
