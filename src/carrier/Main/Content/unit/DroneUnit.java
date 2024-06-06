package carrier.Main.Content.unit;

import arc.func.Cons;
import arc.graphics.Color;
import carrier.Main.Content.AI.DockController;
import carrier.Main.Content.Bullet.CriticalBulletType;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Type_and_Entity.Drone.DroneEntity;
import carrier.Main.Content.Type_and_Entity.Drone.DroneType;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Sounds;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public final class DroneUnit {
    public static UnitType Surenza,Nemesis;
    public static Weapon Rocket,ManchineLaser;
    public static void loadWeapons(){
        ManchineLaser = new Weapon("carrier-mod-N-SmallTurret"){{
            parts.add(new RegionPart("-barrel"){{
                moveY = -0.5f;
                progress = PartProgress.reload;
            }});
            reload = 0.075f*60;
            inaccuracy = 0.5f;
            rotate = true;
            recoil =0f;
            shoot.shotDelay = 1;
            rotateSpeed =1.2f;
            shoot = new ShootBarrel(){{
                barrels = new float[]{2.2f,3.4f,0,-2.2f,3.4f,0};
            }};
            shoot.shots = 2;
            shootSound = Sounds.laser;
            bullet = new LaserBulletType(150){{
                length = 51*8f;
                width = 1f;
                splashDamage = 32;
                lifetime = 8f;
                shootEffect = Fx.hitLaser;
                layerOffset = 1f;
                hitColor = Color.white;
                splashDamageRadius = 20;
                shootCone =12f;
                colors = new Color[]{Color.valueOf("ffffff"),Color.valueOf("fffff0")};
                sideAngle=sideLength=sideWidth=2;
                hitEffect = Fx.hitLaser;
            }};
        }};
        Rocket = new Weapon(){{
            x = 6f;
            mirror = true;
            shootY = 0f;
            reload = 1*60;
            shoot.shotDelay = 3f;
            shoot.shots = 5;
            rotationLimit = 2f;
            inaccuracy = 5f;
            shootSound = Sounds.missile;
            bullet = new CriticalBulletType(80f,10f,"carrier-mod-strike",25f,125f){{
                autoTarget =true;
                splashDamage = 12f;
                buildingDamageMultiplier = 0.5f;
                splashDamageRadius = 50f;
                splashDamageRadiusCrit = 12f;
                splashDamagePierce = true;
                homingPower =0.5f;
                homingRange = 2*8;
                hitSound = Sounds.explosion;
                width = 10f;
                height = 14f;
                lifetime = 40f;
                hitEffect = NDEffect.CircleSpikeBlastOut(Color.white,10,90f,2);
                shootEffect = Fx.none;
                smokeEffect = Fx.none;
                status = StatusEffects.freezing;
                statusDuration = 60f;
            }};
        }};
    }
    public static void loadDrone(){
        loadWeapons();
        Surenza = new DroneType("Surenza"){{
            description = "Small unit,spawned by carrier,only control by carrier";
            health = 1700f;
            armor = 5f;
            speed = LaserUnit.LaserDrone.speed*2f;
            accel = 0.05f;
            drag = 0.07f;
            ImmuneAll = true;
            rotateSpeed=3f;
            homingDelay =0.5f;
            flying = true;
            autoFindTarget =true;
            alwaysCreateOutline = true;
            shadowElevation = 0.02f;
            hitSize = UnitTypes.horizon.hitSize*1.1f;;
            useUnitCap = false;
            constructor = ()-> new DroneEntity(){{
                ImmuneChance = 92f;
                ImmuneTime =0.75f*60f;
                speedMultiplierWhenImmune = 2f;
            }};
            controller = u -> new DockController();
            weapons.addAll(Rocket);
        }};
        Nemesis = new DroneType("Nemesis"){{
            hitSize = UnitTypes.quad.hitSize*0.8f;
            controller = u -> new DockController();
            ImmuneAll = true;
            constructor = ()-> new DroneEntity(){{
                ImmuneChance = 80f;
                ImmuneTime = 0.7f*60;
                speedMultiplierWhenImmune = 1.5f;
            }};
            alwaysCreateOutline = true;
            rotateSpeed = 2f;
            autoFindTarget = true;
            shadowElevation = 1;
            outlineColor = Color.valueOf("3c3d42");
            useUnitCap = false;
            health = 15000f;
            accel = 0.05f;
            drag = 0.03f;
            lowAltitude = true;
            speed = 2.7f;
            flying = true;
            setEnginesMirror(new UnitEngine(1.5f*8,-2.2f*5,3,-90));
            engines.addAll(new UnitEngine(0,-2f*5,4,-90));
            armor = 100;
            weapons.addAll(CopyMove(ManchineLaser, 14.2f, -4f*1f,true));
        }};
    }
    public static Weapon copyAndFix(Weapon weapon, Cons<Weapon> modifier){
		Weapon n = weapon.copy();
		modifier.get(n);
		return n;
	}
    public static Weapon CopyOnly(Weapon weapon){
        Weapon n = weapon.copy();
        return n;
    }
    public static Weapon CopyMove(Weapon weapon,Float x ,Float y,boolean display){
        Weapon n = weapon.copy();
        n.x=x;
        n.y=y;
        n.display = display;
        return n;
    }
}
