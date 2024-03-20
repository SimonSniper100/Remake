package carrier.Main.Content.unit;

import carrier.Main.Content.Ability.AirForce;
import carrier.Main.Content.Ability.DrawPartOnly;
import carrier.Main.Content.Ability.OrbritWeapon;
import carrier.Main.Content.Ability.PartSpawn;
import carrier.Main.Content.Ability.RadarDestruction;
import carrier.Main.Content.Bullet.TrailFadeBulletType;
import carrier.Main.Content.Bullet.Shoot.ShootBarrelsContinous;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Part.HaloParts;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.Type_and_Entity.Carrier.CarrierEntity;
import carrier.Main.Content.Type_and_Entity.Carrier.CarrierType;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.abilities.RepairFieldAbility;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.DrawPart.PartProgress;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Bullet;
import mindustry.gen.EntityMapping;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf ;
import mindustry.graphics.Layer;
import mindustry.type.ItemStack;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.type.ammo.PowerAmmoType;

public class CarrierUnit {
    public static Weapon MainTurret,TransformGun,RailBeam,LargeTurret;
    public static UnitType Disbenzire,Dissesana;
    public static UnitType PartDisbenzire;
    static{
        EntityMapping.nameMap.put("carrier-mod-Disbenzire",EntityMapping.idMap[20]);
    }
    private static void loadWeapon(){
        ModSound.loadSounds();
        RailBeam = new Weapon("carrier-mod-dis-Railgun-Turret"){{
            reload = 38*60;
            shake = 4;
            rotate = true;
            rotateSpeed = 0.3f;
            recoil = 0;
            shootSound = Sounds.beam;
            cooldownTime = 7*60;
            parentizeEffects = true;
            targetInterval = 1f;
            targetSwitchInterval =2f;
            baseRotation = 180;
            
            mirror = false;
            shootWarmupSpeed = 0.2f;
            shoot.shotDelay = 10f;
            continuous = true;
            parts.addAll(new HaloParts(14,Color.white){{
                y = -13f;
                z = Layer.bullet+1f;
            }});
            bullet = new ContinuousLaserBulletType(15000){{
                continuous=pierceArmor=largeHit=true;
                damageInterval = 6;
                lifetime = 480f;    
                smokeEffect = Fx.smokeCloud;
                width = 8;
                length =8*176;
                buildingDamageMultiplier = 0.01f;
                shootY =24f;
                status = StatusEffects.electrified;
                statusDuration = 4f*60;
                fragBullet = new BulletType(){{
                    damage = 1;
                    lightning = 7;
                    lightningCone = 0;
                    lightningColor = Color.white;
                    lightningDamage = 50;
                    lightningLength = 12;
                }};
                colors = new Color[]{Color.white,Color.valueOf("fffff0")};
                shootEffect = new MultiEffect(new Effect(80,e->{
                    Draw.color(Color.white.cpy().a(0.66f*e.fout()));
                    Tmp.v1.setZero();
                    Tmp.v1.trns(e.rotation, e.fin()*14);
                    NDEffect.drawShockWave(e.x + Tmp.v1.x, e.y+Tmp.v1.y, 75f, 0f, -e.rotation - 90f, 120*e.fin(Interp.pow2Out)+1, 15f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                }),new Effect(80,e->{
                    Tmp.v1.setZero();
                    Tmp.v1.trns(e.rotation, e.fin()*70);
                    Draw.color(Color.white.cpy().a(0.66f*e.fout()));
                    NDEffect.drawShockWave(e.x + Tmp.v1.x, e.y+Tmp.v1.y, 75f, 0f, -e.rotation - 90f, 150*e.fin(Interp.pow2Out)+1, 18f*e.fin(Interp.pow2Out)+1, 36,0.02f);
                }));
            }};
        }};
        TransformGun = new Weapon("carrier-mod-d-beam-gun"){{
            continuous = true;
            reload = 5*60f;
            shake = 2f;
            rotate = true;
            rotateSpeed = 1f;
            recoil = 0f;
            targetInterval = 1f;
            targetSwitchInterval =2f;
            shootSound = Sounds.laserbig;
            bullet = new ContinuousLaserBulletType(50){{
                continuous=pierceArmor=largeHit=true;
                damageInterval = 5;
                lifetime = 200f;
                shootY =5f;
                mirror = false;
                smokeEffect = Fx.smokeCloud;
                width = 2.5f;
                length =8*52;
                status = StatusEffects.melting;
                colors = new Color[]{Color.valueOf("E8E8E8"),Color.valueOf("fffff0"),Color.valueOf("ffffff")};
                hitColor = Color.white;
                hitEffect = Fx.hitMeltdown;
            }};
        }};
        MainTurret = new Weapon("carrier-mod-d-railgun"){{
            shoot.shotDelay = 1.5f*60;
            inaccuracy = 0f;
            shoot = new ShootBarrel(){{
                barrels = new float[]{5.02f,20,0,-5.02f,20,0}; 
            }};
            shoot.shots = 2;
            shootSound = Sounds.laserblast;
            shake = 1f;
            recoil = 0f;
            rotateSpeed = 1f;
            controllable = true;
            parentizeEffects = true;
            autoTarget = true;
            rotate = true;
            predictTarget = false;
            inaccuracy = 0.25f;
            mirror = false;
            targetInterval = 1f;
            targetSwitchInterval =2f;
            layerOffset = 1;
            parts.add(new RegionPart("-barrels"){{
                x= 0;
                y= 5f;
                moveY= -1.25f;
                progress = PartProgress.reload;
                outline = true;
                top = true;
            }});
            bullet = new ContinuousLaserBulletType(){{
                continuous=pierceArmor=largeHit=true;
                damage = 200f;
                damageInterval = 6;
                length = 8*52;
                reload = 2f*60;
                lifetime = 0.4f*60f;
                width = 0.3f;
                targetInterval = 1f;
                targetSwitchInterval =2f;
                soundPitchMax =soundPitchMin = 1f;
                shootSound = ModSound.ArpShoot;
                status = StatusEffects.blasted;
                shootStatusDuration = 1f;
                colors = new Color[]{Color.valueOf("E8E8E8"),Color.valueOf("fffff0"),Color.valueOf("ffffff")};
                shootEffect = new Effect(60,e->{
                    Draw.z(Layer.effect);
                    Draw.color(Color.white,e.fout());
                    Tmp.v1.trns(e.rotation, 10+e.fin()*20f);
                    Lines.ellipse(Tmp.v1.x + e.x, Tmp.v1.y + e.y , 0.5f*e.fin()+0.1f,8f,16, e.rotation);
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
                int divisions = 13;
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
        }};
        LargeTurret = new Weapon("carrier-mod-dis-Large-Turret"){{
            shoot = new ShootBarrelsContinous(){{
                barrels = new float[]{6,9,0,-6,9,0};
                shots = 8;
                BulletPerShot = 2;
                shotDelay = 12f;
            }};
            layerOffset = 3f;
            reload = 3*60;
            shootCone = 6f;
            rotateSpeed = 1.1f;
            smoothReloadSpeed =1;
            rotate = true;
            targetInterval = 1f;
            targetSwitchInterval =2f;
            shootWarmupSpeed = 0.05f;
            shootSound = Sounds.shootSmite;
            shake = 2;
            inaccuracy = 4f;
            recoil = 2f;
            bullet = new TrailFadeBulletType(){{
                speed = 10;
                damage = 4250;
                accelerateBegin = 0.1f;
			    accelerateEnd = 0.99f;
                velocityBegin = 20f;
			    velocityIncrease = -19.9f;
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
                lifetime = 120f;
                sprite = "circle";
                fragBullets = 7;
                fragSpread = 360;
                fragLifeMin = 1.2f;
                fragLifeMax = 2f;
                fragBullet = new BasicBulletType(4,200f){{
                    splashDamage = 200f;
                    splashDamageRadius = 20f;
                    sprite = "circle";
                    shrinkX=shrinkY = 0;
                    width = height = 7;
                    trailChance = 0.1f;
                    trailInterval =1f;
                    despawnSound = hitSound = Sounds.plasmaboom;
                    trailEffect = Fx.artilleryTrail.wrap(Color.white);
                    hitEffect = despawnEffect = new MultiEffect(
                        NDEffect.ShockWave(60f,Color.white,splashDamageRadius,0,5,0),
                        NDEffect.SolidCircleCollapse(60, Color.white, width),
                        NDEffect.FragmentExplode(120, Color.white,splashDamageRadius*3f , 8, 3 , 9),
                        NDEffect.TriAngleCircleSpreardSpike(50,splashDamageRadius*0.85f,Color.white,4,2.5f,10,5)
                    );
                }};
                hitEffect = new MultiEffect(
                    NDEffect.CircleSpikeBlastOut(Color.white,splashDamageRadius,70,2),
                    NDEffect.TriAngleCircleSpreardSpike(50,splashDamageRadius*0.95f,Color.white,8,4,20,5),
                    NDEffect.SolidCircleCollapse(70, Color.white, width),
                    NDEffect.FragmentExplode(60, Color.white,splashDamageRadius*4f , 13, 3, 13),
                    NDEffect.Star4Wings(70, Color.white, splashDamageRadius*2, 10, 0, true)
                );
            }};
            layerOffset = 1.4f;
        }
    };
    }
    //Subunit, Nó chỉ là part thôi,dùng cho ability
    public static void loadPartUnit(){
        loadWeapon();
        PartDisbenzire = new PartType("Base-turret"){{
            health=10000;
            speed = 0.9f*1.5f;
            accel =1;
            armor =100000;
            ammoType = new PowerAmmoType(Float.POSITIVE_INFINITY);
            weapons.add(copyMove(TransformGun,0,0));
        }};
    }
    //Đây là main nè
    public static void loadUnit(){
        loadWeapon();
        Disbenzire = new CarrierType("Disbenzire"){{
            BuffStatus = StatusMod.DisbenzireBuff;
            ImmuneAll=true;
            description = "A strong Tier 6 Half carrier unit.Has ability to control the airfighter:Surenza";
            health = 67500f;
            drag = 0.01f;
            accel = 0.01f;
            speed = 0.9f;
            armor = 70f;
            trailLength = 12;
            trailScl = 0.5f;
            lowAltitude = true;
            alwaysCreateOutline = true;
            hitSize = 8f*12;
            rotateSpeed = 0.4f;
            aimDst =0;
            details = "Hmm I dont Know Wat I Am Saying";
            ammoType = new ItemAmmoType(Items.phaseFabric , 1000);
            totalRequirements =firstRequirements=cachedRequirements = new ItemStack[]{
                new ItemStack(Items.silicon,2700),
                new ItemStack(ModItem.QuatanzationCrystal,500),
                new ItemStack(Items.surgeAlloy, 720),
                new ItemStack(Items.plastanium,1400),
                new ItemStack(Items.titanium,1200),
                new ItemStack(Items.phaseFabric,500)
            };            
            constructor = ()-> new CarrierEntity(){{
                nameSprite = "Disbenzire";
                TimeTransform = 120*60;
                CountDownTime = 60*60;
                healSpeedWhenInCarrier = 2f;
                postion.addAll(
                    -14.5f,-60f,0f,
                    -14.5f,-40f,0f,
                    -14.5f,-20f,0f,
                    -14.5f, 0f ,0f,
                    -14.5f,20f ,0f,
                    -14.5f,40f, 0f,
                    -14.5f,60f, 0f,
                    -14.5f,80f, 0f
                    );
                droneType = DroneUnit.Surenza;
                spawnX =-14.5f;
                spawnY = 80f;
                SpawnTime = 12*60f;
                buildSpeedIfTransform = 4f;
            }}; 
            outlineColor = Color.valueOf("3c3d42");
            abilities.addAll(new RepairFieldAbility(100f, 4*60, 8*10),new RadarDestruction(){{
                radarRadius =8*50f;
                radarRadiusDamage = 4000;
                skillCooldown = 6*60;
                BlastEffect =new MultiEffect(
                    NDEffect.BlackHole(Color.white,2f,60,45,140f,7),
                    NDEffect.FragmentExplode(1.2f*60,Color.white,80,5,4,34)
                );
            }},
            new AirForce(100, -60, SupporterUnit.Terebusta,Color.white, "Terebusta"),
            new AirForce(-100, -60, SupporterUnit.Terebusta,Color.white,"Terebusta" ),
            new DrawPartOnly("-Disbenzire-wings1"){{
                x=63f;
                y=-52f;
                z= Layer.groundUnit-1;
            }},
            new DrawPartOnly("-Disbenzire-wings2"){{
                x=-63f;
                y=-52f;
                z= Layer.groundUnit-1;
            }},
            new PartSpawn(PartDisbenzire, 56, 10,true),
            new PartSpawn(PartDisbenzire, -56, 10,false),
            new PartSpawn(PartDisbenzire, 80f, -20f,false),
            new PartSpawn(PartDisbenzire, -80f, -20f,false),
            new PartSpawn(PartDisbenzire, -45, 50,false),
            new PartSpawn(PartDisbenzire, 45, 50,false)
            );
            weapons.addAll(
                        copyMove(MainTurret, -16.6f, 60f, false,0f,true),
                        copyMove(MainTurret, -22f, 7f, false,0,false),
                        copyMove(MainTurret, -10f, -31f, false,180,false),
                        copyMove(MainTurret, -15f, -55.5f, false,180,false),
                        new Weapon(){{
                            x=40;
                            y=-45;
                            shootSound = Sounds.release;
                            shootX =1f;
                            xRand=20f;
                            reload = 5*60f;
                            rotate=mirror=predictTarget =false;
                            ignoreRotation= true;
                            baseRotation=270f;
                            shoot.shots = 2;
                            shootCone=360f;
                            display = false;
                            range=8*40f;
                            shoot.shotDelay = 10f;
                            bullet = new BulletType(){{
                                rangeOverride = 8*60f;
                                spawnUnit = LaserUnit.LaserDrone;
                            }};
                        }},new Weapon(){{
                            x=-40;
                            y=-45;
                            shootSound = Sounds.release;
                            shootX =1f;
                            xRand=20f;
                            reload = 5*60f;
                            rotate=mirror=predictTarget =false;
                            ignoreRotation= true;
                            baseRotation=90f;
                            shoot.shots = 2;
                            shootCone=360f;
                            range=8*40f;
                            shoot.shotDelay = 10f;
                            bullet = new BulletType(){{
                                rangeOverride = 8*60f;
                                spawnUnit = LaserUnit.LaserDrone;
                            }};
                        }},
                        new Weapon(){{
                            display = mirror = false;
                            range  = 1f;
                            reload = 1000000f;
                            x=0f;
                            y=0f;
                            bullet = new BulletType(){{
                                speed =100000f;
                                rangeOverride = 150*8f;
                                shootSound = Sounds.none;
                                shootEffect=Fx.none;
                                smokeEffect = Fx.none;
                                trailEffect = Fx.none;
                                damage = 1f;
                            }};
                        }});
        }};
        Dissesana = new CarrierType("Dissesana"){{
            BuffStatus = StatusMod.DissesanaBuff;
            alwaysCreateOutline = true;
            ImmuneAll = true;
            health = 800000;
            armor = 300;
            description  = "??????? <Not Found> ??????? \n Press N to Transform";
            hitSize = Disbenzire.hitSize * 1.5f;
            outlineColor = Color.valueOf("3c3d42");
            rotateSpeed = 0.2f;
            speed = Disbenzire.speed*0.8f;
            accel = 0.008f;
            drag = 0.005f;
            useEngineElevation = false;
            lowAltitude = true;
            aimDst =0;
            ammoType = new ItemAmmoType(Items.surgeAlloy,3000);
            constructor = () -> new CarrierEntity(){{
                nameSprite = "Dissesana";
                droneType = DroneUnit.Nemesis;
                spawnX =-23f;
                spawnY =-115f;
                SpawnTime = 12*60f;
                TimeTransform = 300*60;
                CountDownTime = 90*60;
                healSpeedWhenInCarrier = 3f;
                buildSpeedIfTransform = 4f;
                postion.addAll(
                    -23f,125f,0f,
                    -23f,95f,0f,
                    -23f,65f,0f,
                    -23f,35f,0f,
                    -23f,5f,0f,   23f,15f,0f,
                    -23f,-25f,0f, 23f,-15f,0f,
                    -23f,-55f,0f, 23f,-45f,0f,
                    -23f,-85f,0f, 23f,-75f,0f,
                    -23f,-115f,0f
                );
            }};
            for(int i : Mathf.signs){
                abilities.addAll(
                    new PartSpawn(PartDisbenzire, 75*i, -70,true),
                    new PartSpawn(PartDisbenzire, 105*i, -90,true),
                    new DrawPartOnly("-Disbenzire-wings"+(i == 1 ?1:2)){{
                        x=90f*i;
                        y=-130f;
                        z= Layer.groundUnit-1;
                    }}
                );
            }
            weapons.addAll(
                MoveModifed(MainTurret, -4.6f*8f, 6.5f*8f,s ->{
                s.shoot = new ShootBarrelsContinous(){{
                    barrels = new float[]{5.02f,20,0,-5.02f,20,0}; 
                    BulletPerShot = 2;
                    shots = 6;
                    shotDelay = 15f;
                }};
                s.mirror = false;
                s.baseRotation = 180;
                s.display = false;
                s.layerOffset = 2f;
                s.reload = 3f*60;
            }),
            new Weapon(){{
                display = mirror = false;
                range  = 1f;
                reload = 1000000f;
                x=0f;
                y=0f;
                bullet = new BulletType(){{
                    speed =100000f;
                    rangeOverride = 301*8f;
                    shootSound = Sounds.none;
                    shootEffect=Fx.none;
                    smokeEffect = Fx.none;
                    trailEffect = Fx.none;
                    damage = 1f;
                }};
            }},
            copyMove(RailBeam,-3.4f*8 , -16*8),
            copyMove(LargeTurret, -4.02f*8, 15.2f*8, false, 0, true),
            copyMove(LargeTurret, -4f*8, 10.2f*8, false, 0, false),  
            MoveModifed(DroneUnit.ManchineLaser, -5.3f*8f, 7.87f*8f, m->{
                m.reload = 0.12f*60;
                m.mirror = false;
                m.controllable = false;
                m.autoTarget = true;
                m.rotateSpeed = 2f;
                m.bullet = new LaserBulletType(150){{
                    length = 45*8f;
                    width = 1.2f;
                    splashDamage = 32;
                    lifetime = 8f;
                    shootEffect = Fx.hitLaser;
                    hitColor = Color.white;
                    splashDamageRadius = 20;
                    colors = new Color[]{Color.valueOf("ffffff"),Color.valueOf("fffff0")};
                    sideAngle=sideLength=sideWidth=2;
                    hitEffect = Fx.hitLaser;
                }};
            }),
            MoveModifed(DroneUnit.ManchineLaser, -4.44f*8f, 17.25f*8f, m->{
                m.reload = 0.12f*60;
                m.mirror = false;
                m.controllable = false;
                m.autoTarget = true;
                m.display = false;
                m.rotateSpeed = 2f;
                m.bullet = new LaserBulletType(150){{
                    length = 45*8f;
                    width = 1.2f;
                    splashDamage = 32;
                    lifetime = 8f;
                    shootEffect = Fx.hitLaser;
                    hitColor = Color.white;
                    splashDamageRadius = 20;
                    colors = new Color[]{Color.valueOf("ffffff"),Color.valueOf("fffff0")};
                    sideAngle=sideLength=sideWidth=2;
                    hitEffect = Fx.hitLaser;
                }};
            }));
            abilities.addAll(new OrbritWeapon(){{
                color = Color.white;
                skillCooldown = 30*60;
                firstDamage = 2000000;
                shockWaveIncrease = 30*8;
                beamRadius = 14*8f;
                duration = 15*60;
                damage = 300000;
                timeDelay = 2*60f;
                Velocity = 3f;
                shake = 10;
                damageInterval = 5;
                BoomEffect = new MultiEffect(
                    NDEffect.ShockWave(7*60f,Color.white,beamRadius+shockWaveIncrease,4,17,6)
                );
            }});
        }};
    }
    public static Weapon copyMove(Weapon weapon, float x, float y,boolean mirror,float OffSet,boolean display){
		Weapon n = weapon.copy();
		n.x = x;
		n.y = y;
        n.mirror = mirror;
        n.baseRotation = OffSet;
        n.display = display;
		return n;
	}
    public static Weapon copyMove(Weapon w,float x,float y){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        return n;
    }
    public static Weapon MoveModifed(Weapon w, float x,float y,Cons<Weapon> mod){
        Weapon n = w.copy();
        n.x=x;
        n.y=y;
        mod.get(n);
        return n;
    }
    //Thank to Yuria
    public static Seq<StatusEffect> statuses;
	
    public static void immunise(UnitType type){
		if(statuses == null){
			statuses = Vars.content.statusEffects().copy();
			statuses.retainAll(s -> {
				return s.disarm || s.damage > 0 || s.healthMultiplier * s.reloadMultiplier * s.buildSpeedMultiplier * s.speedMultiplier < 1;
			});
			statuses.remove(StatusEffects.overclock);
			statuses.remove(StatusEffects.overdrive);
			statuses.add(StatusEffects.wet);
			statuses.add(StatusEffects.unmoving);
		}
		
		type.immunities.addAll(statuses);
	}
}
