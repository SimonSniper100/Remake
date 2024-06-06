package carrier.Main.Content.unit;

import static mindustry.Vars.tilesize;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import carrier.Main.CarrierVars;
import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Ability.CrossHairSkill;
import carrier.Main.Content.Ability.PartSpawn;
import carrier.Main.Content.Bullet.AccelBulletType;
import carrier.Main.Content.Bullet.AdaptedLaserBulletType;
import carrier.Main.Content.Bullet.BlackHoleType;
import carrier.Main.Content.Bullet.DataBulletType;
import carrier.Main.Content.Bullet.MutliHitBullet;
import carrier.Main.Content.Bullet.TrailFadeBulletType;
import carrier.Main.Content.Bullet.Shoot.ShootBarrelsContinous;
import carrier.Main.Content.Effect.NDDraw;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.Special.PropellerEngine;
import carrier.Main.Content.Special.SeqMutliEffect;
import carrier.Main.Content.Special.ThursterEngine;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;
import carrier.Main.Content.Weapons.ConfigWeapons;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.Sized;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.ExplosionBulletType;
import mindustry.entities.bullet.PointBulletType;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Bullet;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
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
    public static Weapon RailGunBlackHole,SecondaryGun,NakisakaRailGun,NakisakaSmallgun,NakisakaRocket;
    public static void loadWeapon(){
        NakisakaRocket = new Weapon("carrier-mod-N-Rocket"){{
            reload = 4*60;
            velocityRnd = 0.07f;
            layerOffset = 1.2f;
            shootY = 24;
            rotate = true;
            mirror = false;
            rotateSpeed = 1.1f;
            shoot = new ShootBarrelsContinous(){{
                shots = 24;
                shotDelay = 7f;
                BulletPerShot = 3;
                barrels = new float[]{10,0,0,0,0,0,-10,0,0};
            }};
            bullet = new AccelBulletType(0,750){
                @Override
					public void updateHoming(Bullet b){
						if(b.time >= this.homingDelay){
							if(b.owner instanceof Unit u){
								if(u.isPlayer()){
									Player p = u.getPlayer();
									b.vel.setAngle(Angles.moveToward(b.rotation(), b.angleTo(p.mouseX, p.mouseY), homingPower * Time.delta * 60.0f));
								}else super.updateHoming(b);
							}else super.updateHoming(b);
						}
					}
                {
                sprite = "missile-large";
                width = 8f;
				height = 30f;
                accelerateBegin = 0.1f;
				accelerateEnd = 0.7f;
				velocityBegin = 7f;
				velocityIncrease = 19f;
                backColor = hitColor = lightColor = lightningColor = CarrierVars.thurmixRed;
                trailColor = Color.gray.cpy();
                frontColor = CarrierVars.thurmixRedLight;
                homingPower = 0.08f;
				homingDelay = 8f;
                lifetime = 120f;
                lightning = 1;
				lightningLengthRand = 12;
				lightningLength = 3;
				lightningDamage = 300;
                trailWidth = 2f;
				trailLength = 12;
                splashDamage = 300;
                rangeOverride =250*tilesize;    
                splashDamageRadius = 120;
                despawnEffect=hitEffect= new MultiEffect(
                    NDEffect.Star4Wings(1f*60f, CarrierVars.thurmixRedLight, splashDamageRadius*1.2f, 40*1.5f, 0, true),
                    NDEffect.ShockWave(2f*60f, CarrierVars.thurmixRedLight, splashDamageRadius, 3, 10, 0)
                );
            }};
            shootCone = 360;
			recoil = 0.5f;
            recoilTime = 3.5f*60f;
            parts.add(new RegionPart("-base"){{
                moveY = -0.5f;
                progress = PartProgress.recoil;
            }});
        }};
        NakisakaSmallgun = new Weapon("carrier-mod-smallGun"){
            
            {
            reload = 15f;
            recoilPow =2;
            recoilTime = reload*0.5f;
            recoil = 1f;
            shake = 3f;
            heatColor = CarrierVars.thurmixRed;
            cooldownTime=reload*0.8f;
            rotate = true;
            rotateSpeed = 1f;
            shootSound = ModSound.synchro;
            display = false;
            shootY = 6f;
            bullet = new AdaptedLaserBulletType(){
                @Override
                public void init(Bullet b){
                    super.init(b);
                    shootEffect.wrap(hitColor, b.rotation());
                }
                {
                length = 100*8f;
                knockback = 2f;
                width=1f;
                damage = 550f;
                lifetime = 0.2f*60;
                hitColor = CarrierVars.thurmixRed;
                width = 5f;
                colors = new Color[]{CarrierVars.thurmixRed,Color.white.cpy()};
                pierceDamageFactor = 0.2f;
                pierce = true;
                sideAngle =1;
                sideLength = 2f;
                sideWidth = 1;
                pierceCap = 3;
            }};
        }};
        NakisakaRailGun = new Weapon("carrier-mod-N-RailGun"){
            
            {
            parts.addAll(
                new RegionPart("-Barrel"){{
                    progress = PartProgress.recoil;
                    moveY = -8f;
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
            shootCone = 0.5f;
            reload = recoilTime = 4*60f;
            heatColor = Color.white.cpy();
            bullet = new DataBulletType(28f,0){
                {
                hitShake = 15;
                lifetime = 72f;
                sprite = "circle";
                readDataUnit = true;
                splashDamage = 10000f;
                width =height= 4f;
                absorbable = false;
                shrinkX = shrinkY = 0;
                trailLength = 100000;
                trailWidth = 8f;
                trailColor = CarrierVars.thurmixRed;
                fragBullets =1;
                despawnEffect = NDEffect.FragmentExplode(1f*60,CarrierVars.thurmixRed, 200, 7, 3, 200).startDelay(0.1f*60f);
                fragBullet = new MutliHitBullet(){{
                    lifetime = 15f;
                    absorbable = false;
                    hitShake = 5;
                    damage = 100;
                    splashDamage = 33f;
                    hitSound = Sounds.plasmaboom;
                    MutliHitInterval =0.9f;
                    hitSoundVolume = 1.5f;
                    fragBullets =2;
                    lightning = 10;
                    lightningCone = 361f;
                    lightningDamage = 250f;
                    lightningLength = 2;
                    lightningLengthRand = 0;
                    fragBullet = new DataBulletType(0,20000f){{
                        hitSound = Sounds.boom;
                        hitSoundVolume = 1.2f;
                        killShooter = false;
                        splashDamage = 12000;
                        splashDamageRadius = 10;
                        hitShake= 50f;
                        absorbable = false;
                        hitEffect = new MultiEffect(
                            NDEffect.SolidCircleCollapse(1.5f*60f, CarrierVars.thurmixRed, 8f).followParent(false),
                            NDEffect.BlackHoleData(CarrierVars.thurmixRed, 2f, 1f*60f, 45, 250f, 6,true).followParent(false),
                            NDEffect.FragmentExplode(1f*60,CarrierVars.thurmixRed, 150, 14, 7, 140).followParent(false),
                            NDEffect.CircleSpikeBlastOut(CarrierVars.thurmixRed,splashDamageRadius,1.5f*60f,4).followParent(false)
                        );
                        fragBullet = new MutliHitBullet(){{
                            lifetime = 0.8f*60f;
                            damage = 400;
                            hitShake = 7;
                            absorbable = false;
                            lightningCone = 361f;
                            lightningDamage = 250f;
                            lightningLength = 2;
                            lightningLengthRand = 0;
                            MutliHitInterval =2f;
                        }};
                    }};
                }};
                
            }};
        }};
        SecondaryGun = new Weapon("carrier-mod-SecondaryGun"){{
            reload = 60*3f;
            recoil =1.5f;
            recoilTime =reload*0.8f;
            heatColor = Color.white;
            rotateSpeed =1f;
            mirror = false;
            rotate = true;
            inaccuracy = 6f;
            shootY = 6.7f;
            layerOffset = 1f;
            shootSound = Sounds.shootSmite;
            shoot.shots = 3;
            shoot.shotDelay = 13f;
            parts.addAll(new RegionPart("-base"){{
                moveY = -1.8f;
                layerOffset =-0.5f;
                progress = PartProgress.recoil;
            }});
            bullet= new TrailFadeBulletType(){
                {
                speed = 24f;
                lifetime = 85;
                lifeTimeEffect = lifetime*1.4f;
                pierceArmor = pierceBuilding = true;
                sprite="circle";
                frontColor =hitColor= CarrierVars.thurmixRed;
                shrinkX=shrinkY=0;
                pierceCap = 10;
                scaledSplashDamage= true;
                width=height = 12f;
                shootEffect =new MultiEffect(
                    NDEffect.shootLine(33, 28),
                    Fx.shootSmokeTitan
                ); 
                hitColor = CarrierVars.thurmixRed;
                splashDamageRadius = 30;
                splashDamage = 1000f;
                scaledSplashDamage = true;
                trailLength = 10;
                trailWidth = 3f;
                homingRange = 600f;
                absorbable = false;
                ShootColor = CarrierVars.thurmixRedLight;
                hittable = false;
                trailColor = CarrierVars.thurmixRed;
                homingPower =20f;
                homingDelay = lifetime*0.40f;
                tracerRandX = width/2;
                tracers =4;
                tracerUpdateSpacing = 1f;
                tracerStroke = 2f;
                tracerSpacing = 2f;
                damage = 2000f;
                hitEffect =new MultiEffect(
                    NDEffect.ShockWave(1*60f, CarrierVars.thurmixRed, splashDamageRadius, 2f, 4, 4)
                );
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
                buildingDamageMultiplier = 0.01f;
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
                    force =1.5f;
                    suctionRadius = 700f;
                    interval =3f;
                    fin = true;
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
                    buildingDamageMultiplier = 0.01f;
                    despawnSound =hitSound= ModSound.hugeBlasts;
                    fragBullets=1;
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
                        splashDamagePierce = true;
                        lifetime = 0.5f; 
                        killShooter =false;
                        splashDamage = 150000f;
                        splashDamageRadius = 400f;
                        despawnShake = 200f;
                        soundPitchMax = soundPitchMin = 1f;
                        hitSoundVolume = 40f;
                        for(int i = 0;i<60;i+=10){
                            add.add(NDEffect.TriAngleCircleSpreardSpike(6*60f, 900, CarrierVars.thurmixRed, 6,30, 300+24*Mathf.random(-1f, 1f), 600,true,10000).followParent(false).startDelay(i).layer(Layer.effect-1.1f));
                        }
                        add.addAll(
                            NDEffect.CircleSpikeBlastOut(CarrierVars.thurmixRed, 800, 60*6, 4,80).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.BlackHoleCollapse(1.9f*60f, CarrierVars.thurmixRed, 200f, !fin).followParent(false),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).startDelay(15f).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.ShockWave(60*6, CarrierVars.thurmixRed, 420*8f, 60, 30f, 3f).startDelay(30f).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.FragmentExplode(3f*60,CarrierVars.thurmixRed, 1200, 24*Mathf.random(0.5f, 1f), 10, 1300).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.FragmentExplode(3f*60,CarrierVars.thurmixRed, 1200, 32*Mathf.random(0.5f, 1f), 10, 1300).followParent(false).layer(Layer.effect-1.1f),
                            NDEffect.SpikeCircle2(1.8f*60f, CarrierVars.thurmixRed, 900, 14, 600f, 130f).followParent(false).layer(Layer.effect-1.1f));
                        despawnEffect = new SeqMutliEffect(add);
                        fragBullets = 1;
                        buildingDamageMultiplier = 0.01f;
                        fragBullet = new ExplosionBulletType(splashDamage*0.5f, splashDamageRadius*2f){
                            {
                                removeAfterPierce = false;
                                killShooter = false;
                                buildingDamageMultiplier = 0.01f;
                            }};
                        }
                    };
                }};
            }};
        }};
    }
    public static void loadParts(){
        loadWeapon();
        //Railgun
        BaseWeapons = new PartType("PartNakisaka"){{
            flying =true;
            health = 1000000f;
            armor = 100000f;
            speed = 1;
            accel =drag =0.07f;
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
            flying = true;
            envDisabled = Env.none;
            omniMovement = false;
            speed = 0.4f;
            fallSpeed = 0.005f;
            totalRequirements =firstRequirements=cachedRequirements=new ItemStack[]{
                new ItemStack(ModItem.Diamond, 10000),
                new ItemStack(ModItem.QuatanzationCrystal, 12000)
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
            armor = 300f;
            faceTarget = false ;
            rotateSpeed = 0.23f;
            BuffStatus = StatusMod.NakisakaBuff;
            targetFlags = new BlockFlag[]{BlockFlag.turret, BlockFlag.core};
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
                invFrameSecond=0;
            }};
            for(int i=-16;i<=16;i++){
                engines.addAll(new ThursterEngine(48+i, -180, 1, 0, 120, false){{
                    z = Layer.flyingUnitLow-5;
                    color = Pal.accent;
                }});
                engines.addAll(new ThursterEngine(-(48+i), -180, 1, 0, 120, false){{
                    z = Layer.flyingUnitLow-5;
                    color = Pal.accent;
                }});
            }
            skills.add(new CrossHairSkill(){
                @Override
                public void drawWeaponCrosshair(Unit u){
                super.drawWeaponCrosshair(u);
                Draw.reset();
                Draw.color(CarrierVars.thurmixRed);
                Draw.z(Layer.effect);
                final Vec2 v1 = new Vec2(),v2 = new Vec2(),v=new Vec2(),v3=new Vec2(),v4=new Vec2(),v5=new Vec2();
                float s = progressDelayIndex(0,60,0.5f,0);
                float a = 120*Interp.pow3.apply(s);
                float rot = CrossHairRotation(u);
                float ss = targetFoundFadeOut(0,0.3f,u,13.5f*tilesize);
                float w= Interp.pow2Out.apply(shootFadeIn(0.4f,u,0));
                v2.trns(rot, Mathf.clamp(rngFin())*u.range());
                for(int i = 0;i<3;i++){
                    v.trns(rot+120*i+a, 13.5f*tilesize);
                    v1.trns(rot+120*i+60-a, 4*tilesize);
                    v3.trns(rot+120*i+60-a, 4.6f*tilesize);
                    NDDraw.DrawSquareBracketsKeyShape(new float[]{
                        u.x+v2.x+v.x,u.y+v2.y+v.y,  
                        rot-180+120*i+a,0,5.5f*w,12,50,30});
                    NDDraw.DrawSquareBracketsKeyShape(new float[]{
                        u.x+v2.x+v1.x,u.y+v2.y+v1.y,  
                        rot-180+120*i+60-a,0,2f*w,12f/5f,10,30});
                    Lines.line(u.x+v2.x+v1.x,u.y+v2.y+v1.y,u.x+v2.x+v3.x,u.y+v2.y+v3.y);
                }
                Fill.circle(u.x+v2.x,u.y+v2.y,5*w);
                if(target != null && target instanceof Sized size){
                    float tx = target.getX(),ty=target.getY();
                    Lines.circle(tx, ty, size.hitSize()/8f);
                    for(int i=0;i<4;i++){
                        float f= progressDelayIndex(1,90,0.5f,0);
                        v4.trns(f*90+90*i,ss*13f+size.hitSize());
                        v5.trns(f*90+90*i,ss*13f+size.hitSize()+15);
                        Lines.line(tx+v4.x,ty+v4.y,tx+v5.x,ty+v5.y);
                    }
                    }
                }
            });
            for(int i:Mathf.signs){
                weapons.addAll(CarrierVars.WeaponChainAdd(SecondaryGun,new float[]{58f*i,80f,69f*i,-76f},i!=1));
                weapons.addAll(
                    CarrierVars.copyMove(NakisakaRailGun, 60f*i, -130f,i!=1),
                    CarrierVars.copyMove(NakisakaRocket, 55*i,-7f, i!=1));
                skills.addAll(
                    new PartSpawn(BaseWeapons, 150f*i, -80,i!=1, CarrierVars.thurmixRed){{
                        SpawnCount = 2;
                    }}
                );
                engines.add(new PropellerEngine(){{
                    x=48*i;
                    y=-186f;
                    z= Layer.flyingUnitLow-4f;
                    zCycle = 2f;
                    upperZColor = Color.valueOf("989AA4").cpy();
                    downZColor = Color.valueOf("363636").cpy();
                    propeller = 11;
                    length = 12;
                    stroke = 3.5f;
                    width = 14;
                    angle = 14*i;
                    rotateSpeed = 0.7f*i;
                    strokeOffset = 0.75f;
                }});
            }
            weapons.addAll(CarrierVars.WeaponChainAdd(NakisakaSmallgun,
                    new float[]{-33f, 185f, 37f, 145f, -37f, 117f, -30f, 38f, 62f, -29.5f},true));
            weapons.addAll(
                CarrierVars.MoveModifed(NakisakaSmallgun, 70.5f, 64, m->{
                    m.baseRotation = 180f;
                    }),
                    new ConfigWeapons(){
                        {
                        display = mirror = false;
                        range  = 1f;
                        reload = 1000000f;
                        x=y=0f;
                        rotate = true;
                        bullet = new BulletType(){{
                            speed =100000f;
                            rangeOverride = 251*8f;
                            shootSound = Sounds.none;
                            shootEffect=Fx.none;
                            smokeEffect = Fx.none;
                            trailEffect = Fx.none;
                            damage = 1f;
                        }};
                    }}
                );
            }
        };
    }
}