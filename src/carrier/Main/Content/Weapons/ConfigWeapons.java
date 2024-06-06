package carrier.Main.Content.Weapons;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Interval;
import arc.util.Time;
import arc.util.Tmp;
import carrier.Main.Content.AI.RocketMissleConstructor;
import carrier.Main.Content.Bullet.AccelBulletType;
import mindustry.Vars;
import mindustry.ai.types.MissileAI;
import mindustry.audio.SoundLoop; 
import mindustry.entities.Effect;
import mindustry.entities.Mover;
import mindustry.entities.Predict;
import mindustry.entities.Sized;
import mindustry.entities.UnitSorts;
import mindustry.entities.Units;
import mindustry.entities.Units.Sortf;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Bullet;
import mindustry.gen.Entityc;
import mindustry.gen.Sounds;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;

import static carrier.Main.MathComplex.*;

public class ConfigWeapons extends Weapon{
    boolean r = false;
    float p=0,prog,timeDelaySecond=0;
    int t;
    private transient static final Vec2 
        v1 = new Vec2(),
        v2 = new Vec2(),
        v3 = new Vec2(),
        v4 = new Vec2(),
        v5 = new Vec2();
    public Sortf sortf =UnitSorts.strongest;
    public float minAccurate=-1,maxAccurate=-1;
    public boolean ApplyIdleRotation = false,calculateSeperationDependOnRange;
    public float IdleRotation,SoundVolume;
    protected float Times;
    protected Interval timer = new Interval(10);
    public ConfigWeapons(String name){
        super(name);
    }
    public ConfigWeapons(){
        super("");
    }
    @Override
    public void update(Unit unit, WeaponMount mount){
        float
        weaponRotation = unit.rotation - 90 + (rotate ? mount.rotation : baseRotation),
        mountX = unit.x + Angles.trnsx(unit.rotation - 90, x, y),
        mountY = unit.y + Angles.trnsy(unit.rotation - 90, x, y),
        bulletX = mountX + Angles.trnsx(weaponRotation, this.shootX, this.shootY),
        bulletY = mountY + Angles.trnsy(weaponRotation, this.shootX, this.shootY),
        shootAngle = bulletRotation(unit, mount, bulletX, bulletY); 
        boolean can = unit.canShoot();
        float lastReload = mount.reload;
        mount.reload = Math.max(mount.reload - Time.delta * unit.reloadMultiplier, 0);
        mount.recoil = Mathf.approachDelta(mount.recoil, 0, unit.reloadMultiplier / recoilTime);
        if(recoils > 0){
            if(mount.recoils == null) mount.recoils = new float[recoils];
            for(int i = 0; i < recoils; i++){
                mount.recoils[i] = Mathf.approachDelta(mount.recoils[i], 0, unit.reloadMultiplier / recoilTime);
            }
        }
        mount.smoothReload = Mathf.lerpDelta(mount.smoothReload, mount.reload / reload, smoothReloadSpeed);
        mount.charge = mount.charging && shoot.firstShotDelay > 0 ? Mathf.approachDelta(mount.charge, 1, 1 / shoot.firstShotDelay) : 0;
        float warmupTarget = (can && mount.shoot) || (continuous && mount.bullet != null) || mount.charging ? 1f : 0f;
        if(linearWarmup)mount.warmup = Mathf.approachDelta(mount.warmup, warmupTarget, shootWarmupSpeed);
        else mount.warmup = Mathf.lerpDelta(mount.warmup, warmupTarget, shootWarmupSpeed);   
        //rotate if applicable
        if(rotate && (mount.rotate || mount.shoot) && can){
            v4.trns(unit.rotation+baseRotation,10);  
            float angleTemp = Angles.angle(mountX,mountY, mountX+v4.x, mountY+v4.y)-unit.rotation;        
            mount.targetRotation = Angles.angle(mountX,mountY, mount.aimX, mount.aimY)-unit.rotation;
            float dst = Angles.angleDist(mount.rotation, baseRotation);
            float dst2 = Angles.angleDist(mount.targetRotation, baseRotation);
            if(InRange(rotationLimit,179.9f,360)){
                v1.set(mountX,mountY).setAngle(mount.rotation+unit.rotation).nor();
                v2.set(mountX,mountY).setAngle(mount.targetRotation+unit.rotation).nor();
                v3.set(mountX,mountY).setAngle(unit.rotation+baseRotation).nor();
                int sign = SignedAngle(v1, v3),targetSign =SignedAngle(v2, v3);
                mount.rotation = Angles.moveToward(mount.rotation,sign==targetSign?mount.targetRotation:angleTemp-targetSign,rotateSpeed*Time.delta);
            }
            else mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, rotateSpeed*Time.delta);
            if(rotationLimit< 360){ 
                if(dst >= rotationLimit/2f){
                    mount.rotation = Angles.moveToward(mount.rotation, baseRotation, dst - rotationLimit/2f);
                    mount.rotate = dst2<=rotationLimit/2f;
                }
            }
        }else if(!rotate){
            mount.rotation = baseRotation;
            mount.targetRotation = unit.angleTo(mount.aimX, mount.aimY);
        }
        
        //find a new target
        if(!controllable && autoTarget){
            if((mount.retarget -= Time.delta) <= 0f){
                mount.target = findTarget(unit, mountX, mountY, bullet.range, bullet.collidesAir, bullet.collidesGround);
                mount.retarget = mount.target == null ? targetInterval : targetSwitchInterval;
            }
            if(mount.target != null && checkTarget(unit, mount.target, mountX, mountY, bullet.range)){
                mount.target = null;
            }
            boolean shoot = false;
            if(mount.target != null){
                Times = 0;
                shoot = mount.target.within(mountX, mountY, bullet.range + Math.abs(shootY) + (mount.target instanceof Sized s ? s.hitSize()/2f : 0f))&&can;
                if(predictTarget){
                    if(bullet instanceof AccelBulletType accel){
                        float distance = new Vec2().set(unit).sub(mount.target.getX(),mount.target.getY()).limit(accel.calculateRange()).len();
                        float calculateVelocity = accel.calculateVelocity(distance,accel.calculateRange());
                        Vec2 to = Predict.intercept(unit, mount.target, calculateVelocity);
                        mount.aimX = to.x;
                        mount.aimY = to.y;
                    }
                    else {
                        Vec2 to = Predict.intercept(unit, mount.target, bullet.speed);
                        mount.aimX = to.x;
                        mount.aimY = to.y;
                    }
                }else{
                    mount.aimX = mount.target.x();
                    mount.aimY = mount.target.y();
                }
            }
            else if(mount.target == null && ApplyIdleRotation&&(ApplyIdleRotation ? Times+=Time.delta:0)>=5f*60f){
                v5.trns(unit.rotation+IdleRotation,10);
                float angle2 = Angles.angle(mountX, mountY,mountX+v5.x,mountY+v5.y)-unit.rotation();
                mount.rotation = Angles.moveToward(mount.rotation, angle2, rotateSpeed*Time.delta);
                mount.rotate = Angles.within(mount.rotation, angle2, 2);
            }
            mount.shoot = mount.rotate = shoot;
        }
        if(alwaysShooting) mount.shoot = true;
        //update continuous state
        if(continuous && mount.bullet != null){
            if(!mount.bullet.isAdded() || mount.bullet.time >= mount.bullet.lifetime || mount.bullet.type != bullet){
                mount.bullet = null;
            }else{
                mount.bullet.rotation(weaponRotation + 90);
                mount.bullet.set(bulletX, bulletY);
                mount.reload = reload;
                mount.recoil = 1f;
                unit.vel.add(Tmp.v1.trns(unit.rotation + 180f, mount.bullet.type.recoil * Time.delta));
                if(shootSound != Sounds.none && !Vars.headless){
                    if(mount.sound == null) mount.sound = new SoundLoop(shootSound, 1f);
                    mount.sound.update(bulletX, bulletY, true);
                }
                if(alwaysContinuous && mount.shoot){
                    mount.bullet.time = mount.bullet.lifetime * mount.bullet.type.optimalLifeFract * mount.warmup;
                    mount.bullet.keepAlive = true;

                    unit.apply(shootStatus, shootStatusDuration);
                }
            }
        }else{
            mount.heat = Math.max(mount.heat - Time.delta * unit.reloadMultiplier / cooldownTime, 0);
            if(mount.sound != null){
                mount.sound.update(bulletX, bulletY, false);
            }
        }
        //flip weapon shoot side for alternating weapons
        boolean wasFlipped = mount.side;
        if(otherSide != -1 && alternate && mount.side == flipSprite && mount.reload <= reload / 2f && lastReload > reload / 2f){
            unit.mounts[otherSide].side = !unit.mounts[otherSide].side;
            mount.side = !mount.side;
        }
        //shoot if applicable
        if(mount.shoot && //must be shooting
        can && //must be able to shoot
        !(bullet.killShooter && mount.totalShots > 0) && //if the bullet kills the shooter, you should only ever be able to shoot once
        (!useAmmo || unit.ammo > 0 || !Vars.state.rules.unitAmmo || unit.team.rules().infiniteAmmo) && //check ammo
        (!alternate || wasFlipped == flipSprite) &&
        mount.warmup >= minWarmup && //must be warmed up
        unit.vel.len() >= minShootVelocity && //check velocity requirements
        (mount.reload <= 0.0001f || (alwaysContinuous && mount.bullet == null)) && //reload has to be 0, or it has to be an always-continuous weapon
        (alwaysShooting || Angles.within(rotate ? mount.rotation : unit.rotation + baseRotation, mount.targetRotation, shootCone)) //has to be within the cone
        ){
            shoot(unit, mount, bulletX, bulletY, shootAngle);
            mount.reload = reload;
            if(useAmmo){
                unit.ammo--;
                if(unit.ammo < 0) unit.ammo = 0;
            }
        }
    }
    @Override
    protected void shoot(Unit unit, WeaponMount mount, float shootX, float shootY, float rotation){
        unit.apply(shootStatus, shootStatusDuration);

        if(shoot.firstShotDelay > 0){
            mount.charging = true;
            chargeSound.at(shootX, shootY, Mathf.random(soundPitchMin, soundPitchMax),SoundVolume);
            bullet.chargeEffect.at(shootX, shootY, rotation, bullet.keepVelocity || parentizeEffects ? unit : null);
        }

        shoot.shoot(mount.barrelCounter, (xOffset, yOffset, angle, delay, mover) -> {
            //this is incremented immediately, as it is used for total bullet creation amount detection
            mount.totalShots ++;

            if(delay > 0f){
                Time.run(delay, () -> bullet(unit, mount, xOffset, yOffset, angle, mover));
            }else{
                bullet(unit, mount, xOffset, yOffset, angle, mover);
            }
        }, () -> mount.barrelCounter++);
    }
    @Override
    protected void bullet(Unit unit, WeaponMount mount, float xOffset, float yOffset, float angleOffset, Mover mover){
        if(!unit.isAdded()) return;
        mount.charging = false;
        float
        xSpread = Mathf.range(xRand),
        weaponRotation = unit.rotation - 90 + (rotate ? mount.rotation : baseRotation),
        mountX = unit.x + Angles.trnsx(unit.rotation - 90, x, y),
        mountY = unit.y + Angles.trnsy(unit.rotation - 90, x, y),
        bulletX = mountX + Angles.trnsx(weaponRotation, this.shootX + xOffset + xSpread, this.shootY + yOffset),
        bulletY = mountY + Angles.trnsy(weaponRotation, this.shootX + xOffset + xSpread, this.shootY + yOffset),
        shootAngle = bulletRotation(unit, mount, bulletX, bulletY) + angleOffset,
        lifeScl = bullet.scaleLife ? Mathf.clamp(Mathf.dst(bulletX, bulletY, mount.aimX, mount.aimY) / Math.abs(bullet.range)) : 1f,
        angle = angleOffset + shootAngle + Mathf.range(inaccuracy + bullet.inaccuracy);
        Entityc shooter = unit.controller() instanceof MissileAI ai ? ai.shooter : unit; //Pass the missile's shooter down to its bullets
        float scl;
        if(calculateSeperationDependOnRange){
            float a = velocityRnd*Mathf.clamp(Mathf.dst(bulletX, bulletY, mount.aimX, mount.aimY) / Math.abs(bullet.range));
            scl = (1f - a) + Mathf.random(a);
        }
        else{
            scl = (1f - velocityRnd) + Mathf.random(velocityRnd);
        }
        mount.bullet = bullet.create(unit, shooter, unit.team, bulletX, bulletY, angle, -1f, scl, lifeScl+0.1f*(1-scl)*(mount.target instanceof Sized s ? s.hitSize()/16f:1.2f), null, mover, mount.aimX, mount.aimY);
        handleBullet(unit, mount, mount.bullet);
        if(!continuous){
            shootSound.at(bulletX, bulletY, Mathf.random(soundPitchMin, soundPitchMax));
        }
        ejectEffect.at(mountX, mountY, angle * Mathf.sign(this.x));
        bullet.shootEffect.at(bulletX, bulletY, angle, bullet.hitColor, unit);
        bullet.smokeEffect.at(bulletX, bulletY, angle, bullet.hitColor, unit);
        unit.vel.add(Tmp.v1.trns(shootAngle + 180f, bullet.recoil));
        Effect.shake(shake, shake, bulletX, bulletY);
        mount.recoil = 1f;
        if(recoils > 0){
            mount.recoils[mount.barrelCounter % recoils] = 1f;
        }
        mount.heat = 1f;
    }
    @Override
    public Teamc findTarget(Unit unit, float x, float y, float range, boolean air, boolean ground){
        return Units.bestTarget(unit.team, x, y, range + Math.abs(shootY), u -> u.checkTarget(air, ground), t -> ground,sortf);
    }
    @Override
    public void handleBullet(Unit u,WeaponMount m,Bullet b){
        if(m.bullet != null && m.bullet.type.spawnUnit instanceof MissileUnitType t&&t.controller instanceof RocketMissleConstructor r&&flipSprite&&mirror){
            r.reversePush = flipSprite;
        }
    }
}
