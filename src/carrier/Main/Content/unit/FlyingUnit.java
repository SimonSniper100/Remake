package carrier.Main.Content.unit;

import static mindustry.Vars.tilesize;

import arc.Core;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;
import carrier.Main.CarrierVars;
import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Ability.PartSpawn;
import carrier.Main.Content.Bullet.AccelBulletType;
import carrier.Main.Content.Bullet.BlackHoleType;
import carrier.Main.Content.Bullet.DataBulletType;
import carrier.Main.Content.Bullet.Shoot.ShootBarrelsContinous;
import carrier.Main.Content.Bullet.TrailFadeBulletType;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.Special.SeqMutliEffect;
import carrier.Main.Content.Special.ThursterEngine;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.entities.bullet.PointBulletType;
import mindustry.entities.bullet.ShrapnelBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.Env;

public class FlyingUnit {
    public static Seq<Effect> add = new Seq<>();
    public static UnitType Nakisaka;
    public static UnitType BaseWeapons;  
    public static Weapon RailGunBlackHole,SecondaryGun,NakisakaRailGun,NakisakaSmallgun;
    public static void loadWeapon(){
        NakisakaSmallgun = new Weapon("carrier-mod-smallGun"){{
            reload = 15f;
            recoilPow =2;
            recoilTime = reload*0.5f;
            recoil = 1f;
            shake = 2f;
            heatColor = CarrierVars.thurmixRed;
            cooldownTime=reload*0.8f;
            rotate = true;
            rotateSpeed = 1f;
            shootSound = ModSound.synchro;
            display = false;
            shootY = 6f;
            bullet = new ShrapnelBulletType(){{
                length = 100*8f;
                knockback = 2f;
                damage = 550f;
                lifetime = 0.2f*60;
                width = 5f;
                fromColor =toColor =  CarrierVars.thurmixRed;
                shootEffect = Fx.shootSmokeTitan.wrap(CarrierVars.thurmixRed);
                pierceDamageFactor = 0.2f;
                pierce = true;
                pierceCap = 3;
                hitEffect = Fx.hitLaser.wrap(CarrierVars.thurmixRedLight);
            }};
        }};
        NakisakaRailGun = new Weapon("carrier-mod-N-RailGun"){{
            parts.addAll(
                new RegionPart("-Barrel"){{
                    progress = PartProgress.reload;
                    moveY = -7f;
                }}
            );
            shoot = new ShootBarrel(){{
                shots = 2;
                barrels = new float[]{13f,20f,0,-13f,20f,0};
            }};
            rotateSpeed = 0.6f;
            mirror = false;
            rotate = true;
            layerOffset = 1.1f;
            shootCone = 20f;
            reload = recoilTime = 4*60f;
            heatColor = Color.white.cpy();
            bullet = new DataBulletType(28f,35000f){{
                lifetime = 72f;
                sprite = "circle";
                readDataUnit = true;
                splashDamage = 10000f;
                splashDamageRadius = 200f;
                width =height= 12f;
                shrinkX = shrinkY = 0;
                trailLength = 100000;
                trailWidth = 8f;
                lightning = 10;
                lightningCone = 361f;
                lightningDamage = 250f;
                lightningLength = 2;
                lightningLengthRand = 0;
                trailColor = CarrierVars.thurmixRed;
                despawnEffect = NDEffect.FragmentExplode(1f*60,CarrierVars.thurmixRed, 100, 7, 3, 38).startDelay(0.1f*60f);
                hitEffect = new MultiEffect(
                    NDEffect.SolidCircleCollapse(1.5f*60f, CarrierVars.thurmixRed, 8f).followParent(false),
                    NDEffect.BlackHoleData(CarrierVars.thurmixRed, 2, 0.8f*60f, 45, 200f, 10).startDelay(0.1f*60f).followParent(false),
                    NDEffect.FragmentExplode(1f*60,CarrierVars.thurmixRed, 100, 7, 3, 38).startDelay(0.1f*60f).followParent(false),
                    NDEffect.CircleSpikeBlastOut(CarrierVars.thurmixRed,splashDamageRadius,1.5f*60f,4).followParent(false)
                );
            }};
        }};
        SecondaryGun = new Weapon("carrier-mod-SecondaryGun"){{
            reload = 60*3f;
            recoil =0f;
            heatColor = Color.white;
            rotateSpeed =1f;
            mirror = false;
            rotate = true;
            layerOffset = 1f;
            shoot = new ShootBarrelsContinous(){{
                shots = 4;
                shotDelay = 23f;
                BulletPerShot = 2;
                barrels = new float[]{-9f,1.3f,0,9f,1.3f,0};
            }};
            parts.addAll(new RegionPart("-barrel"){{
                y =9f;
                moveY = -2f;
                layerOffset =-0.5f;
                progress = PartProgress.recoil;
            }});
            bullet= new AccelBulletType(7, 3000f){{
                sprite = "circle";
                lifetime = 4*60;
                velocityBegin = 10f;
                velocityIncrease = 0.001f;
                accelerateBegin = 0;
                accelerateEnd = 0.001f;
            }};
        }};
        RailGunBlackHole = new Weapon("carrier-mod-GiantTurret"){{
            mirror= false;  
            reload = 60*36f;
            heatColor = Color.white;
            recoil = 4f;
            recoilPow = 2;
            recoilTime = 60*8;
            shootCone = 2;
            shake = 5f;
            rotationLimit =18+5f;
            rotate = true;
            rotateSpeed = 0.3f;
            layerOffset = 3f;
            cooldownTime = 15*60f;
            shootY = 16f*tilesize;
            bullet = new PointBulletType(){
                {
                damage = 4250;
                range = 250*8f;
                speed = 10f;
                splashDamage = 1000;
                splashDamageRadius = 60;
                shootEffect = Fx.shootSmallColor.wrap(Color.white);
                hitEffect = Fx.none;
                parentizeEffects = true;
                trailEffect = Fx.railTrail.wrap(CarrierVars.thurmixRed);
                removeAfterPierce = false;
                despawnSound = hitSound = Sounds.largeExplosion;
                trailChance =0;
                trailColor = Color.white;
                lifetime = 200f;
                fragBullets = 1;
                despawnEffect = new MultiEffect(
                    NDEffect.FragmentExplode(3f*60,CarrierVars.thurmixRed, 800, 24, 4, 800).followParent(false),
                    NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 800f, 80, 23f, 3f).followParent(false)
                );
                fragBullet= new BlackHoleType(0f, 1000){                    
                    {
                    color = CarrierVars.thurmixRed;
                    BlackHoleTeamColor = false;
                    BlackHoleRadius = 30f;
                    BlackHoleRadiusRemain = 60f;
                    BlackHoleRadiusAffect = 200f;
                    BlackHoleDamage = 1000f;
                    force = 0.5f;
                    suctionRadius = 700f;
                    interval =1f;
                    fin = true;
                    AnotherBullet = null;
                    shakeStrenghtFrom =3f;
                    shakeStrenghtTo = 23f;
                    lifetime = 8f*60f;
                    removeAfterPierce = false;
                    nearEndEffectDelay = 15f;
                    ExtenalEffectDelay = 20f;
                    lightning = 10;
                    lightningCone = 361f;
                    lightningDamage = 70f;
                    lightningLength = 10;
                    lightningLengthRand = 2;
                    despawnSound =hitSound= ModSound.hugeBlasts;
                    fragBullets=1;
                    BulletSpawn = new TrailFadeBulletType(){{
                        hitEffect=despawnEffect= new MultiEffect(
                                NDEffect.CircleSpikeBlastOut(CarrierVars.thurmixRed,splashDamageRadius,70,2),
                                NDEffect.TriAngleCircleSpreardSpike(50,splashDamageRadius*0.95f,CarrierVars.thurmixRed,8,4,20,5),
                                NDEffect.SolidCircleCollapse(70, CarrierVars.thurmixRed, width),
                                NDEffect.FragmentExplode(60, CarrierVars.thurmixRed,splashDamageRadius*4f , 13, 3, 13),
                                NDEffect.Star4Wings(70, CarrierVars.thurmixRed, splashDamageRadius*2, 10, 0, true)
                        );
                        hitColor = CarrierVars.thurmixRed;
                        speed = 10;
                        damage = 4250;
                        velocityBegin = 20f;
                        splashDamage = 1000;
                        width = height = 12;
                        splashDamageRadius = 60;
                        shootEffect = Fx.shootSmallColor.wrap(Color.white);
                        hitEffect = Fx.none;
                        tracerRandX = 7f;
                        tracerSpacing = 3f;
                        tracerStroke =2f;
                        shrinkX=shrinkY = 0;
                        despawnSound = hitSound = Sounds.largeExplosion;
                        tracerFadeOffset = 10;
                        trailChance =0;
                        trailColor = Color.white;
                        tracers = 3;
                        tracerUpdateSpacing = 3f;
                        lifetime = 40f;
                        sprite = "circle";
                        fragBullets = 7;
                        fragSpread = 360;
                        fragLifeMin = 1.2f;
                        fragLifeMax = 2f;
                    }};
                    ExtenalEffect = new MultiEffect(
                        NDEffect.FragmentVaccum(2f*60,CarrierVars.thurmixRed, 800, 24, 8, 42).followParent(false),
                        NDEffect.TriAngleCircleSpreardSpike(3*60f, 700, CarrierVars.thurmixRed, Mathf.random(1,5),30, 300, 150,true,1000).followParent(false)
                    );
                    nearEndEffect = new MultiEffect(NDEffect.Absorbed(0.7f*60, CarrierVars.thurmixRed, 600, 80, 23f, 3f)).followParent(false);
                    fragBullet = new ExplosionBulletType(){
                        @Override
                        public void despawned(Bullet b){
                            if(despawnHit){
                                hit(b);
                            }else{
                                createUnits(b, b.x, b.y);
                            }

                            if(!fragOnHit){
                                createFrags(b, b.x, b.y);
                            }
                            despawnEffect.at(b.x, b.y, b.rotation(), hitColor, b);
                            Effect.shake(despawnShake, despawnShake, b);
                            despawnSound.at(b.x,b.y,1,hitSoundVolume);
                        }
                        {
                        pierce =pierceArmor=pierceBuilding= true;
                        removeAfterPierce = false;
                        lifetime = 0.5f; 
                        killShooter =false;
                        splashDamage = 150000f;
                        splashDamageRadius = 1000f;
                        despawnShake = 200f;
                        soundPitchMax = soundPitchMin = 1f;
                        hitSoundVolume = 40f;
                        for(int i = 0;i<60;i+=10){
                            add.add(NDEffect.TriAngleCircleSpreardSpike(6*60f, 900, CarrierVars.thurmixRed, 6,30, 300+24*Mathf.random(-1f, 1f), 600,true,4000).followParent(false).startDelay(i));
                        }
                        add.addAll(
                            NDEffect.BlackHole(CarrierVars.thurmixRed, 5, 1.2f*60f, 45, 300f, 10).startDelay(0.1f*60f).followParent(false),
                            NDEffect.CircleSpikeBlastOut(CarrierVars.thurmixRed, 800, 60*6, 4,80).followParent(false),
                            NDEffect.BlackHoleCollapse(1.9f*60f, CarrierVars.thurmixRed, 200f, !fin).followParent(false),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).followParent(false),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).startDelay(15f).followParent(false),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).startDelay(30f).followParent(false),
                            NDEffect.FragmentExplode(3f*60,CarrierVars.thurmixRed, 1200, 24*Mathf.random(0.5f, 1f), 10, 1300).followParent(false),
                            NDEffect.FragmentExplode(3f*60,CarrierVars.thurmixRed, 1200, 32*Mathf.random(0.5f, 1f), 10, 1300).followParent(false),
                            NDEffect.SpikeCircle2(1.8f*60f, CarrierVars.thurmixRed, 900, 14, 600f, 130f).followParent(false));
                        despawnEffect = new SeqMutliEffect(add);
                        fragBullets = 1;
                        fragBullet = new ExplosionBulletType(splashDamage*0.5f, splashDamageRadius*1.5f){{
                            removeAfterPierce = false;
                            killShooter = false;
                        }};
                        }
                    };
                }};
            }};
        }};
    }
    public static void loadParts(){
        loadWeapon();
        BaseWeapons = new PartType("PartNakisaka"){{
            flying =true;
            health = 1000000f;
            armor = 100000f;
            speed = 1;
            accel =drag =1;
            lowAltitude = true;
            controller = u-> new PartUnitAI(true);
            ammoType = new ItemAmmoType(ModItem.Diamond, 1000);
            weapons.add(CarrierVars.copyMove(RailGunBlackHole,0,12f));
            for(int i=-16;i<=16;i++){
                engines.addAll(new ThursterEngine(i, -2*tilesize, 1, 0, 90, false){{
                    z = Layer.flyingUnitLow-0.5f;
                    color = Pal.accent;
                }});
            }
        }};
    }
    public static void loadUnit(){
        loadWeapon();
        loadParts();
        Nakisaka = new TransformType("Nakisaka"){{
            description = "[#e3566a]???? <Sniper-Name-Not-Found> ????\n"+
                        " [white]Press N to Transform ";
            flying = true;
            envDisabled = Env.none;
            omniMovement = false;
            speed = 0.4f;
            totalRequirements =firstRequirements=cachedRequirements=new ItemStack[]{
                new ItemStack(ModItem.Diamond, 3250),
                new ItemStack(ModItem.QuatanzationCrystal, 1250)
            };
            constantEffect.add(StatusMod.Special);
            accel = 0.013f;
			drag = 0.013f;
            health = 1200000f;
            outlineColor = Color.valueOf("363636");
            ImmuneAll = true;
            crashDamageMultiplier = 2;
            lowAltitude = true;
            itemCapacity = 400;
            fogRadius = 220;
            engineSize = 0f;
            hitSize = 220.0f;
            ImmuneSuction = true;
            armor = 700f;
            faceTarget = false ;
            rotateSpeed = 0.23f;
            BuffStatus = StatusMod.NakisakaBuff;
            targetFlags = new BlockFlag[]{BlockFlag.turret, BlockFlag.core, null};
            constructor = () -> new TransformFlying(){
                @Override
                public float mass(){
                    return 1000000;
                }
                {
                nameSprite = "Nakisaka";
                color = CarrierVars.thurmixRed;
                radius = 340f;
                TimeTransform = 480*60;
                CountDownTime = 120*60f;
            }};
            for(int i=-16;i<=16;i++){
                engines.addAll(new ThursterEngine(48+i, -180, 1, 0, 90, false){{
                    z = Layer.flyingUnitLow-1;
                    color = Pal.accent;
                }});
                engines.addAll(new ThursterEngine(-(48+i), -180, 1, 0, 90, false){{
                    z = Layer.flyingUnitLow-1;
                    color = Pal.accent;
                }});
            }
            
            for(int i:Mathf.signs){
                weapons.addAll(
                    CarrierVars.copyMove(SecondaryGun, 60f*i, 80f,i!=1),
                    CarrierVars.copyMove(NakisakaRailGun, 60f*i, -130f,i!=1)
                );
                abilities.addAll(
                    new PartSpawn(BaseWeapons, 150f*i, -80,i!=1){{
                        SpawnCount = 2;
                        rotate = 90;
                        message="While Transforming:\nSpawn 2 Railgun that can create Strong Black Hole";
                    }}
                );
            }
            weapons.addAll(
                CarrierVars.copyMove(NakisakaSmallgun, -33f, 185, true),
                CarrierVars.copyMove(NakisakaSmallgun, 37f, 145),
                CarrierVars.copyMove(NakisakaSmallgun, -37f, 117),
                CarrierVars.MoveModifed(NakisakaSmallgun, 70.5f, 64, m->{
                    m.baseRotation = 180f;
                }),
                CarrierVars.copyMove(NakisakaSmallgun, -30, 40),
                CarrierVars.copyMove(NakisakaSmallgun, 62, -29.5f)
            );
        }};
    }
}
