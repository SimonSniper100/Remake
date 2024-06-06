package carrier.Main.Content.Bullet;

import arc.graphics.Color;
import arc.math.Angles;
import carrier.Main.Content.Effect.NDEffect;
import mindustry.entities.Effect;
import mindustry.entities.bullet.ContinuousLaserBulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;

public class AdaptedLaserBeamBulletType extends ContinuousLaserBulletType {
    public int boltNum = 2;
	public float liWidth = NDEffect.WIDTH - 1f;
    public boolean bulletFolow = true;
    public Color useColor = Color.white;
    public AdaptedLaserBeamBulletType(float damageInterval,float damage){
        this.damageInterval =damageInterval;
        this.damage = damage;
    }
    public AdaptedLaserBeamBulletType(){

    }
    @Override
    public void init(Bullet b){
        super.init(b);
    }
    @Override
    public void update(Bullet b){
        if(!continuous) return;

        if(b.timer(1, damageInterval)){
            applyDamage(b);
            NDEffect.createEffect(b, currentLength(b)*0.95f, b.rotation(), useColor, boltNum, liWidth,damageInterval*2f);
        }
        if(shake > 0){
            Effect.shake(shake, shake, b);
        }
        updateBulletInterval(b);
    }
    protected float bulletRotation(Unit unit, WeaponMount mount, float bulletX, float bulletY){
        return mount.weapon.rotate ? unit.rotation + mount.rotation : Angles.angle(bulletX, bulletY, mount.aimX, mount.aimY) + (unit.rotation - unit.angleTo(mount.aimX, mount.aimY)) + mount.weapon.baseRotation;
    }
}
