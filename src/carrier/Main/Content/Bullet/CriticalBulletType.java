package carrier.Main.Content.Bullet;

import static mindustry.Vars.*;

import arc.audio.Sound;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Fires;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Teamc;

public class CriticalBulletType extends DataBulletType {
    //Chơi Double Vì Bắt Buộc
    /** Tỉ Lệ Crit Mà Đạn Có Được */
    public double critRate = 10.00;
    /** Tăng Damage Khi Crit */
	public float critDamage= 100.00f;
    public float splashDamageRadiusCrit = 10;
	public boolean CritHit,FragCrit = false,HealCrit = false,SlapedRadiusIncreaseIfCrit = false;
	public boolean SlapedCrit,despawnHitEffects = true,crit;
    // Có Thể Null Effect Được
    public Sound CritSound;
    public @Nullable Effect CritEffect;
    public @Nullable Effect TrailCritEffect = Fx.trailFade;
	protected float timer;
    public static class CriticalBulletData{
        //Static Cho Bullet Để Crit
        public boolean crit,GotDespawn;
        public CriticalBulletData(boolean crit){
            this.crit = crit;
        }
    }

    public CriticalBulletType(){
		super();
	}
    public CriticalBulletType(float damage,float speed,String bulletSprite,float critRate,float critDamage){
        super(speed,damage,bulletSprite);
        this.critRate=critRate;
        this.critDamage = critDamage;
    }
    //Phải Ghi Đè
    @Override
    public float calculateRange(){
        
        return super.calculateRange();
    }
    //Phân Tính Damage
    @Override
    public void init(Bullet b){
        /*
         * Nếu Crit lớn hơn 100 thì lấy 2 số cuối làm random 
         * và hệ số damage tăng thêm số nguyên của ((crit/100)+ 1)
        */
        // Để Tách Riêng Bullet Nếu Ko Là Mọi Bullet Giống Nhau Đều Sẽ Sài Bullet Chung
        if (b.data == null){
            // Random để ghi giá trị crit
            // 1f = 1%
            if(Mathf.chance((critRate - (int)(critRate/100))/100f)){
                //Cập Nhật True Cho Bullet Đó Nếu Roll Thành Công
                b.data = new CriticalBulletData(true);
            }else {
                b.data = new CriticalBulletData(false);
            }
        }
        if (((CriticalBulletData)b.data).crit){
            // Nếu True Trong Data Thì Làm Crit
            b.damage = b.damage * (((critDamage+100f)/100f)+(int)(critRate/100f));
        }
        else {
            b.damage = b.damage *(1+(int)(critRate/100f));
        }
        //Cập Nhật Lại Bullet
        super.init(b);
    }
    // Phần Dưới Là Bullet Bình Thường Nhưng Thêm Crit
    @Override
    public void update(Bullet b){
        crit = ((CriticalBulletData)b.data).crit;
        if(trailChance > 0){
            if(Mathf.chanceDelta(trailChance) && TrailCritEffect != null&&crit){
                TrailCritEffect.at(b.x, b.y, trailParam, trailColor);
            }
            else{
                trailEffect.at(b.x, b.y, trailParam, trailColor);
            }
        }
        if(weaveMag > 0){
            b.vel.rotate(Mathf.sin(b.time + Mathf.PI * weaveScale/2f, weaveScale, weaveMag * (Mathf.randomSeed(b.id, 0, 1) == 1 ? -1 : 1)) * Time.delta);
        }
        if(homingPower > 0.0001f && b.time >= homingDelay){
            Teamc target = Units.closestTarget(b.team, b.x, b.y, homingRange, e -> e.checkTarget(collidesAir, collidesGround) && !b.collided.contains(e.id), t -> collidesGround && !b.collided.contains(t.id));
            if(target != null){
                b.vel.setAngle(Angles.moveToward(b.rotation(), b.angleTo(target), homingPower * Time.delta * 50f));
            }
        }

    }
    @Override
    public void despawned(Bullet b){
        ((CriticalBulletData)b.data).GotDespawn = true;
        super.despawned(b);
    }
    @Override
    public void hit(Bullet b, float x, float y){
        CriticalBulletData data = (CriticalBulletData)b.data;
        boolean isCrit = data.crit;
        Effect.shake(hitShake, hitShake, b);
        float critBonus = isCrit ?(critDamage + 100f)/100f : 1f;
        b.hit = true;
        if(fragOnHit){
            createFrags(b, x, y);
        }
        createPuddles(b, x, y);
        createIncend(b, x, y);

        if(!data.GotDespawn || despawnHitEffects){
            if(isCrit && CritEffect != null){
                CritEffect.at(x, y, b.rotation(), hitColor);
                if(CritSound != null)CritSound.at(x,y,hitSoundPitch,hitSoundVolume);
                else hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
            }
            else {
                hitEffect.at(x, y, b.rotation(), hitColor);
                hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
            }
            
        }
        if(suppressionRange > 0){
            Damage.applySuppression(b.team, b.x, b.y, suppressionRange, suppressionDuration, 0f, suppressionEffectChance, new Vec2(b.x, b.y));
        }
        createSplashDamage(b, x, y);
        for(int i = 0; i < lightning; i++){
            Lightning.create(b, lightningColor, (lightningDamage < 0 ? damage : lightningDamage) * critBonus, b.x, b.y, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
        }
    }
    @Override
    public void createFrags(Bullet b, float x, float y){
        if(fragBullet != null && FragCrit){
            boolean crit = ((CriticalBulletData)b.data).crit;
            for(int i = 0; i < fragBullets; i++){
                float len = Mathf.random(1f, 7f);
                float a = b.rotation() + Mathf.range(fragRandomSpread / 2) + fragAngle + ((i - fragBullets / 2) * fragSpread);
                Bullet f = fragBullet.create(b, x + Angles.trnsx(a, len), y + Angles.trnsy(a, len), a, Mathf.random(fragVelocityMin, fragVelocityMax), Mathf.random(fragLifeMin, fragLifeMax));
                if(f.type instanceof CriticalBulletType) f.data = new CriticalBulletData(crit);
            }
        }
    }
    @Override
    public void createSplashDamage(Bullet b, float x, float y){
        float critBonus = ((CriticalBulletData)b.data).crit ?(critDamage + 100f)/100f : 1f;
        boolean deal = SlapedRadiusIncreaseIfCrit&&((CriticalBulletData)b.data).crit,crit = ((CriticalBulletData)b.data).crit;
        if(splashDamageRadius > 0 && !b.absorbed){
            Damage.damage(b.team, x, y, splashDamageRadius+(deal ? splashDamageRadiusCrit: 0), splashDamage * b.damageMultiplier() * (SlapedCrit&&crit ? critBonus : 1), false, collidesAir, collidesGround, scaledSplashDamage, b);

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius+(deal ? splashDamageRadiusCrit: 0), status, statusDuration, collidesAir, collidesGround);
            }

            if(heals()){
                indexer.eachBlock(b.team, x, y, splashDamageRadius+(deal ? splashDamageRadiusCrit: 0), Building::damaged, other -> {
                    healEffect.at(other.x, other.y, 0f, healColor, other.block);
                    other.heal((healPercent / 100f * other.maxHealth() + healAmount) *(HealCrit&&crit ? critBonus : 1));
                });
            }

            if(makeFire){
                indexer.eachBlock(null, x, y, splashDamageRadius+(deal ? splashDamageRadiusCrit: 0), other -> other.team != b.team, other -> Fires.create(other.tile));
            }
            
        }
    }
    
}
