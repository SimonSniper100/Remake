package carrier.Main.Content.unit;

import static carrier.Main.MathComplex.*;
import static mindustry.Vars.tilesize;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.util.Tmp;
import carrier.Main.CarrierVars;
import carrier.Main.MathComplex;
import carrier.Main.Content.AI.UpdateCommanderAI;
import carrier.Main.Content.Ability.DrawEletricLines;
import carrier.Main.Content.Ability.PartSpawn;
import carrier.Main.Content.Ability.Skill;
import carrier.Main.Content.Bullet.AccelBulletType;
import carrier.Main.Content.Bullet.AdaptedLaserBeamBulletType;
import carrier.Main.Content.Bullet.ContinousLaserLowerBulletType;
import carrier.Main.Content.Bullet.DataBulletType;
import carrier.Main.Content.Bullet.Shoot.ShootBarrelsContinous;
import carrier.Main.Content.Effect.NDDraw;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Math.Interpolate;
import carrier.Main.Content.Part.PartProgess.SmallBlackCore;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.Type_and_Entity.Battleship.BattleshipTransformer;
import carrier.Main.Content.Type_and_Entity.Battleship.BattleshipType;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import carrier.Main.Content.Weapons.ConfigWeapons;
import carrier.Main.Content.Weapons.SecondaryWeapon;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.entities.pattern.ShootPattern;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.meta.Env;
public final class BattleshipUnit {
    public static UnitType USSMissouri;
    public static UnitType[] BaseWingPart=new UnitType[2],WingBlade1= new UnitType[2],WingBlade2 = new UnitType[2],WingBlade3=new UnitType[2];
    public static UnitType PartMissouri,Rings;
    public static Weapon MainBattery,MainBattery63,BeamLaser,SecondaryBattery,SmallBaseGunWing,CoreMissileBullet;
    private static void loadWeapons(){
        MainBattery = new ConfigWeapons("carrier-mod-MainBattery"){{
            rotate = true;
            layerOffset = 0.0011f;
            rotationLimit = 270;
            mirror = false;
            recoilTime = 1.5f*60;
            parts.add(new RegionPart("-barrel"){{
                y=1;
                layerOffset =0.0052f;
            }},new RegionPart("-upbarrel"){{
                layerOffset =0.005f;
                y =73.2f;
                progress = PartProgress.recoil;
                moveY=-4f;
            }});
            reload = 5*60;
            shootSound = ModSound.ArpShoot;
            soundPitchMax = soundPitchMin = 1f;
            SoundVolume = 12f;
            recoil = 0;
            shake = 12f;
            rotateSpeed=0.2f;
            bullet = new AdaptedLaserBeamBulletType(3, MathComplex.Factorial(7)*3){{
                lifetime = 30f;
                pierceArmor = true;
                pierceBuilding = false;
                length = 650*tilesize;
                width = 5f;
                buildingDamageMultiplier = 0.001f;
                colors = new Color[]{Color.valueOf("8ca9e8"),Color.white.cpy()};
                shootEffect = new MultiEffect(
                    new Effect(80,e->{
                    Draw.color(Color.valueOf("8ca9e8").cpy().a(e.fout()));
                    Tmp.v1.setZero();
                    Tmp.v1.trns(e.rotation,7f+3f*3f);
                    NDEffect.drawShockWave(e.x + Tmp.v1.x, e.y+Tmp.v1.y, 80f, 0f, -e.rotation - 90f, 30*e.fin(Interp.pow2Out)+1, 2f*e.fout(Interp.pow2Out)+1, 36,0.02f);
                }),new Effect(80,e->{
                    Draw.color(Color.valueOf("8ca9e8").cpy().a(e.fout()));
                    NDEffect.drawShockWave(e.x,e.y, 80f, 0f, -e.rotation - 90f, 60*e.fin(Interp.pow2Out)+1, 5f*e.fout(Interp.pow2Out)+1, 36,0.02f);
                })
                );
            }};
            shootY = 80f;
            shoot = new ShootBarrelsContinous(){{
                shots = 3;
                BulletPerShot = 3;
                barrels = new float[]{0,0,0,-20,0,0,20,0,0};
            }};
        }};
        MainBattery63 = MainBattery.copy();
        MainBattery63.name = "carrier-mod-MainBattery63";
        MainBattery63.layerOffset = 0.001f;
        SecondaryBattery = new SecondaryWeapon("carrier-mod-Secondary-Battery"){{
            ApplyIdleRotation = true;
            baseRotation = -90;
            rotationLimit= 179;
            inaccuracy = 10;
            reload =0.25f*60;
            layerOffset = 0.0011f;
            recoil =0;
            alternate = false;
            rotate = true;
            rotateSpeed = 1.5f;
            shootCone = 10f;
            recoilTime = reload*0.9f;
            IdleRotation = 0;
            controllable = false;
            autoTarget = !controllable;
            calculateSeperationDependOnRange = true;
            shootY = 34;
            velocityRnd = 0.3f;
            shoot = new ShootBarrel(){{
                shots = 2;
                barrels = new float[]{9.2f,0,0,-9.2f,0,0};
            }};
            parts.add(new RegionPart("-upbarrel"){{
                y = 35;
                progress = PartProgress.recoil;
                moveY = -2f;
                layerOffset = 0.0001f;
            }},
            new RegionPart("-barrel"){{
                layerOffset = 0.0002f;
            }}
            );
            bullet= new DataBulletType(){{
                damage = 3200;
                speed = 13;
                buildingDamageMultiplier = 0.01f;
                splashDamagePierce = true;
                splashDamageRadius = tilesize*1.2f;
                splashDamage = 700;
                scaleLife = true;
                shootSound = ModSound.SecondarySound;
                trailLength = 10;
                trailColor = Color.valueOf("8ca9e8");
                lifetime = 60*2.25f;
            }};
        }};
        CoreMissileBullet = new ConfigWeapons(){{
            x=y=shootX=shootY=0;
            shootCone = 360;
            reload = 250f;
            shootWarmupSpeed = 0.02f;
            minWarmup = 0.98f;
            shoot = new ShootPattern(){
					public void shoot(int totalShots, BulletHandler handler){
						for(int i = 0; i < shots; i++){
							handler.shoot(0, 0, Mathf.random(360), firstShotDelay + shotDelay * i);
						}
					}
					{
						shots = 30;
						shotDelay = 3f;
					}
				};
            bullet = new AccelBulletType(){
                {
                    width =27f;
                    damage = 5250;
                    height = 70f;
                    velocityBegin = 2f;
                    velocityIncrease = 24f;
                    accelInterp = a -> Interpolate.Interpolate1(a);
                    accelerateBegin = 0.001f;
                    accelerateEnd = 1f;
                    trailEffect = Fx.colorSpark;
                    pierceCap = 3;
                    splashDamage = damage / 4f;
                    splashDamageRadius = 3*8f;
                    backColor = frontColor =trailColor = Color.valueOf("8ca9e8");
                    trailLength = 30;
                    trailWidth = 3f;
                    
                    lifetime = 240f;
                    pierceArmor = true;
                    trailRotation = true;
                    trailChance = 0.6f;
                    trailParam = 4f;
                    trailInterval = 3f;
                    
                    homingPower = 0.085f;
                    homingDelay = 5;
                    
                    lightning = 4;
                    lightningLengthRand = 10;
                    lightningLength = 5;
                    lightningDamage = damage / 4;
                    
                    shootEffect = smokeEffect = Fx.none;
                    hitEffect = despawnEffect = new MultiEffect(new Effect(65f, b -> {
                        Draw.color(b.color);
                        
                        Fill.circle(b.x, b.y, 6f * b.fout(Interp.pow3Out));
                        
                        Angles.randLenVectors(b.id, 6, 35 * b.fin() + 5, (x, y) -> Fill.circle(b.x + x, b.y + y, 4 * b.fout(Interp.pow2Out)));
                    }),NDEffect.ShockWave(30f, Color.valueOf("8ca9e8"), splashDamageRadius, 2, 7, 0));
                    homingRange = rangeOverride = 480*8;
                    despawnHit = false;
                }
                
                @Override
                public void update(Bullet b){
                    super.update(b);
                }
                @Override
                public void updateTrailEffects(Bullet b){
                    if(trailChance > 0){
                        if(Mathf.chanceDelta(trailChance)){
                            trailEffect.at(b.x, b.y, trailRotation ? b.rotation() : trailParam, Color.valueOf("8ca9e8"));
                        }
                    }
                    
                    if(trailInterval > 0f){
                        if(b.timer(0, trailInterval)){
                            trailEffect.at(b.x, b.y, trailRotation ? b.rotation() : trailParam, Color.valueOf("8ca9e8"));
                        }
                    }
                }
                
                @Override
                public void hit(Bullet b, float x, float y){
                    b.hit = true;
                    hitEffect.at(x, y, b.rotation(), Color.valueOf("8ca9e8"));
                    hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
                    
                    Effect.shake(hitShake, hitShake, b);
                    
                    if(splashDamageRadius > 0 && !b.absorbed){
                        Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), collidesAir, collidesGround);
                        
                        if(status != StatusEffects.none){
                            Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
                        }
                    }
                    
                    for(int i = 0; i < lightning; i++) Lightning.create(b, Color.valueOf("8ca9e8"), lightningDamage < 0 ? damage : lightningDamage, b.x, b.y, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
                    
                    if(!(b.owner instanceof Unit))return;
                    Unit from = (Unit)b.owner;
                    if(from.dead || !from.isAdded() || from.healthf() > 0.99f) return;
                    NDEffect.chainLightningFade.at(b.x, b.y, Mathf.random(12, 20), Color.valueOf("8ca9e8"), from);
                    from.heal(damage / 8);
                }
                
                @Override
                public void despawned(Bullet b){
                    despawnEffect.at(b.x, b.y, b.rotation(), Color.valueOf("8ca9e8"));
                    Effect.shake(despawnShake, despawnShake, b);
                }
                @Override
                public void removed(Bullet b){
                    if(trailLength > 0 && b.trail != null && b.trail.size() > 0){
                        Fx.trailFade.at(b.x, b.y, trailWidth, Color.valueOf("8ca9e8"), b.trail.copy());
                    }
                }
                @Override
                public void init(Bullet b) {
                    super.init(b);
                    Rand r = new Rand(b.id);
                    b.vel.rotate(r.random(360));
                }
                @Override
                public void draw(Bullet b) {
                    Tmp.c1.set(Color.valueOf("8ca9e8")).lerp(Color.white, Mathf.absin(4f, 0.3f));
                    
                    if(trailLength > 0 && b.trail != null){
                        float z = Draw.z();
                        Draw.z(z - 0.01f);
                        b.trail.draw(Tmp.c1, trailWidth);
                        Draw.z(z);
                    }
                    
                    Draw.color(Color.valueOf("8ca9e8"), Color.white, 0.35f);
                    NDDraw.arrow(b.x, b.y, 5, 35, -6, b.rotation());
                    Draw.color(Tmp.c1);
                    NDDraw.arrow(b.x, b.y, 5, 35, 12, b.rotation());
                    
                    Draw.reset();
                }
            };
        }};
        BeamLaser = new ConfigWeapons(){{
            reload = 26.5f*60;
            recoil=0;
            mirror = false;
            continuous = true;
            rotateSpeed=0.1f;
            x=y=0f;
            shoot.firstShotDelay =2.5f*60f;
            shootY= 4;
            rotationLimit = 20f;
            shootCone = rotationLimit/2f +1f;
            parentizeEffects = true;
            rotate = true;
            layerOffset = 0.0012f;
            bullet = new ContinousLaserLowerBulletType(){{
                lifetime = 10f*60;
                shutPercent = 0.4f;
                width =30;
                length = 650*tilesize;
                damage = 30000;
                impact = true;
                colors = new Color[]{Color.valueOf("8ca9e8"),Color.white.cpy()};
                damageInterval = 5f;
                knockback = 500;
                pierceArmor = true;
                chargeEffect = new MultiEffect(
                    NDEffect.ChargeEffectStar4Wing(2.5f*60f, Color.valueOf("8ca9e8"), false, 35, 360, 45, 0, 270, 0).followParent(true).layer(Layer.effect-0.01f)
                );
                shootEffect = new MultiEffect(new Effect(80,e->{
                    Draw.color(Color.valueOf("8ca9e8").cpy().a(0.66f*e.fout()));
                    Tmp.v1.setZero();
                    Tmp.v1.trns(e.rotation, e.fin()*14+4*3f);
                    NDEffect.drawShockWave(e.x + Tmp.v1.x, e.y+Tmp.v1.y, 75f, 0f, -e.rotation - 90f, 120*e.fin(Interp.pow2Out)+1, 15f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                }),new Effect(80,e->{
                    Tmp.v1.setZero();
                    Tmp.v1.trns(e.rotation, e.fin()*70+4*3f);
                    Draw.color(Color.valueOf("8ca9e8").cpy().a(0.66f*e.fout()));
                    NDEffect.drawShockWave(e.x + Tmp.v1.x, e.y+Tmp.v1.y, 75f, 0f, -e.rotation - 90f, 150*e.fin(Interp.pow2Out)+1, 18f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                }),
                new ParticleEffect(){{
                    
                }}
                );
            }};
        }};
        SmallBaseGunWing = new ConfigWeapons("carrier-mod-SmallGunBase"){{
            shoot.shots =3;
            shoot.shotDelay = 12f;
            rotate = true;
            rotateSpeed = 1f;
            mirror = false;
            recoil = 1;
            reload = 3*60f;
            bullet = new AccelBulletType(10, 3000){{
                velocityBegin = 20;
                velocityIncrease= -19;
                accelerateBegin = 0.01f;
                accelerateEnd = 0.99f;
                trailLength = 12;
                height= width = 10;
                trailWidth = 4; 
                trailColor = Color.valueOf("8ca9e8");
                lifetime = 3*60f;
                scaledSplashDamage = true;
                splashDamage = 1000;
                splashDamageRadius= tilesize*6;
                shrinkX = shrinkY =0;
                status = StatusEffects.freezing;
                StatusEffectsStackTime = 2*60f;
                statusStackable.addAll(
                    StatusEffects.slow,
                    StatusEffects.wet,
                    StatusEffects.corroded,
                    StatusEffects.muddy
                );
            }};
        }};
    }
    public static void loadPart(){
        loadWeapons();
        PartMissouri = new PartType("Part-Missouri"){{
            health =armor= 1000000;
            flying = true;
            speed = 2f;
            Lossen = true;
            accel=drag=0.05f;
            faceTarget = true;
            parts.add(new RegionPart("-heat"){{
                heatProgress = PartProgress.heat;
                healColor = Color.white;
            }});
            weapons.add(BeamLaser);
        }};
        Rings = new PartType("Rings"){{
            health = armor = 1000000;
            flying = true;
            speed = 3.75f;
            lowAltitude = true;
            Lossen = true;
            accel=drag=0.05f;
            weapons.add(CoreMissileBullet);
            parts.add(new SmallBlackCore(){{
                size = 2.3f*8;
                color = Color.valueOf("8ca9e8");
            }});
        }};
        for(int i:Mathf.signs){
            int step = NonNegative(i);
            
            WingBlade1[step]= new PartType("WingBlade"+"-"+SignDirection(i)){{
                health = armor = 1000000;
                flying = true;
                speed = 3.5f;
                lowAltitude = true;
                Lossen = true;
                accel = drag = 0.1f;
                outlineColor = Color.valueOf("8ca9e8");
            }};
            WingBlade2[step]= new PartType("WingBlade2"+"-"+SignDirection(i)){{
                health = armor = 1000000;
                flying = true;
                speed = 3.5f;
                lowAltitude = true;
                Lossen = true;
                accel = drag = 0.1f;
                outlineColor = Color.valueOf("8ca9e8");
            }};
            WingBlade3[step]= new PartType("WingBlade3"+"-"+SignDirection(i)){{
                health = armor = 1000000;
                flying = true;
                speed = 3.5f;
                lowAltitude = true;
                Lossen = true;
                accel = drag = 0.1f;
                outlineColor = Color.valueOf("8ca9e8");
            }};
            BaseWingPart[step]= new PartType("BaseWingPart"+"-"+SignDirection(i)){{
                health = armor = 1000000;
                flying =true;
                speed = 3f;
                lowAltitude = true;
                Lossen = true;
                accel = drag = 0.07f;
                weapons.add(new ConfigWeapons(){{
                    mirror = false;
                    x =-15*i;
                    y = -120;
                    reload = 10*60f;
                    predictTarget = false;
                    shootCone = 360f;
                    ignoreRotation = true;
                    sortf = UnitSorts.strongest;
                    ignoreRotation = true;
                    bullet = new BulletType(){{
                        collidesAir = collidesGround = true;
                        despawnUnitRadius = 100;
                        spawnUnit = LaserUnit.LargeMissile;
                    }};
                }});
                weapons.addAll(
                    CarrierVars.WeaponChainAdd(SmallBaseGunWing, new float[]{-12*i,20,19*i,0}, false)
                );
            }};
        }
    }
    public static void loadUnit(){
        loadPart();
        USSMissouri = new BattleshipType("USS-Missouri"){{
            health = 12000000;
            armor = 1200;
            envDisabled = Env.none;
            controller = u -> new UpdateCommanderAI();
            constructor =()->new BattleshipTransformer(){
                {
                radius = 430;
                color = Color.valueOf("8ca9e8");
                nameSprite = "USS-Missouri";
                TimeTransform = 10*60*60;
                CountDownTime = TimeTransform *0.4f;
                invFrameSecond = 0.001f;    
            }};
            totalRequirements =firstRequirements=cachedRequirements=new ItemStack[]{
                new ItemStack(ModItem.Diamond, 3250),
                new ItemStack(ModItem.QuatanzationCrystal, 1250)
            };
            BuffStatus = StatusMod.USSMissouriBuff;
            hitSize = 21*Vars.tilesize*1.5f;
            speed = 0.5f;
            groundLayer = Layer.groundUnit-1f;
            rotateSpeed = 0.07f;
            crashDamageMultiplier = 10;
            accel = 0.007f;
            ImmuneSuction = true;
            drag = accel;
            lowAltitude = true;
            useEngineElevation = false;
            engineSize=0;
            shadowElevation = 0.7f;
            forceMultiTarget = true;
            naval = true;
            parts.addAll(new RegionPart("-sup-1"){{
                layerOffset = 0.04f;
            }});
            weapons.addAll(
                CarrierVars.copyMove(MainBattery63,0,220,false),
                CarrierVars.copyMove(MainBattery,0,130,false),
                CarrierVars.MoveModifed(MainBattery,0,-230.5f,m->{
                m.baseRotation = 180;
                m.rotationLimit =250f;
            }));
            skills.add(new Skill(){
                @Override
                public void draw(Unit u){
                super.draw(u);
                if ((u instanceof TransformFlying f && f.TransformNow)||(u instanceof TransformEntity s&&s.TransformNow)){
                    Draw.color(Color.valueOf("8ca9e8"));
                    NDDraw.DrawArrowLookCenter(u.x, u.y, 470, 30, time, 10, 4);
                    }
                }
            });
            //Secondary Weapons
            weapons.addAll(
                    CarrierVars.MoveModifed(SecondaryBattery,35,42,m->{
                        m.display = true;
                        m.layerOffset = 0.05f;
                    }),
                    CarrierVars.copyMove(SecondaryBattery, 52,-7, false),
                    CarrierVars.MoveModifed(SecondaryBattery, 52, -117, m->{
                        m.display = false;
                        if(m instanceof ConfigWeapons c){
                            c.IdleRotation = 180;
                        }
                    })
                );
            
                
            for(int i:Mathf.signs){
                int step = NonNegative(i);
                skills.addAll(
                    new PartSpawn(BaseWingPart[step],-120*i,-200,false){{
                        moveX = -5*tilesize*i;
                        moveY = -50;
                        nameSpirte ="BaseWingPart"+"-"+SignDirection(i);
                        color = Color.valueOf("8ca9e8");
                        moveSpeed = 7f;
                        DrawEletricLine = true;
                        for(int j:new int[]{0,1,2,3,4,5}){
                            linesEletric.addAll(
                                new DrawEletricLines(-85*i,-120-37*j,23*i,80-30*j,Color.valueOf("8ca9e8"))
                            );
                        }
                    }},
                    new PartSpawn(Rings, -140*i, -30, false){{
                        moveSpeed = 0.7f;
                        moveX = -20f*i;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "Rings";
                    }},
                    new PartSpawn(WingBlade1[step], -195*i, 10, false){{
                        rotationOffset = 18*i;
                        rotateSpeeds = 0.5f;
                        bodyRotation = 4*i;
                        DrawEletricLine = false;
                        color = Color.valueOf("8ca9e8");
                        moveSpeed = 0.7f;
                        moveX = -20f*i;
                        InterpRotation = Interp.pow3;
                        nameSpirte = "WingBlade"+"-"+SignDirection(i);
                    }},
                    new PartSpawn(WingBlade2[step], -195*i, -30, false){{
                        DrawEletricLine = false;
                        moveX = -20f*i;
                        moveSpeed = 0.7f;
                        rotateSpeeds = 0.5f;
                        bodyRotation = -7*i;
                        InterpRotation = Interp.pow3;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "WingBlade2"+"-"+SignDirection(i);
                    }},
                    new PartSpawn(WingBlade3[step], -195*i, -70, false){{
                        rotationOffset = -18*i;
                        moveSpeed = 0.7f;
                        moveX = -20f*i;
                        rotateSpeeds = 0.5f;
                        bodyRotation = -13*i;
                        DrawEletricLine = false;
                        InterpRotation = Interp.pow3;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "WingBlade3"+"-"+SignDirection(i);
                    }},
                    new PartSpawn(WingBlade2[step], -195*i, -180, false){{
                        rotationOffset = -30*i;
                        bodyRotation = 40*i;
                        moveSpeed = 7f;
                        rotateSpeeds = 0.5f;
                        moveX = -5*tilesize*i;
                        moveY = -50;
                        InterpRotation = Interp.pow3;
                        DrawEletricLine = false;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "WingBlade2"+"-"+SignDirection(i);
                    }},
                    new PartSpawn(WingBlade1[step],-170*i, -240, false){{
                        rotationOffset = -50*i;
                        bodyRotation = 27*i;
                        moveSpeed = 7f;
                        rotateSpeeds = 0.5f;
                        moveX = -5*tilesize*i;
                        moveY = -50;
                        InterpRotation = Interp.pow3;
                        DrawEletricLine = false;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "WingBlade"+"-"+SignDirection(i);
                    }},
                    new PartSpawn(WingBlade3[step], -128*i, -300f, false){{
                        rotationOffset = -70*i;
                        bodyRotation = 18*i;
                        moveSpeed = 7f;
                        rotateSpeeds = 0.5f;
                        moveX = -5*tilesize*i;
                        moveY = -50;
                        InterpRotation = Interp.pow3;
                        DrawEletricLine = false;
                        color = Color.valueOf("8ca9e8");
                        nameSpirte = "WingBlade3"+"-"+SignDirection(i);
                    }}
                );
                
            }
        }};
    }
}
