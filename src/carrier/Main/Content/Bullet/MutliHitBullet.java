package carrier.Main.Content.Bullet;

import carrier.Main.CarrierVars;
import mindustry.entities.Units;
import mindustry.gen.Bullet;

public class MutliHitBullet extends DataBulletType {
    public float MutliHitInterval;
    public MutliHitBullet(){
        killShooter=absorbable=hittable=false;
        speed = 0;
        sprite ="";
        fragOnHit = false;
        collidesAir = collidesGround = collideFloor = collideTerrain = collides = false;
        removeAfterPierce = false;
    }
    @Override
    public void update(Bullet b){
        super.update(b);
        if(b.timer(0, MutliHitInterval)){
            CarrierVars.completeDamage(b.team, b.x, b.y, splashDamageRadius, damage/MutliHitInterval);
            hitEffect.at(b);
            if(readDataUnit){
                var unit = Units.closestEnemy(b.team,b.x,b.y, 6,u->!u.dead);
                if(unit != null){
                    b.set(unit);
                }
            }
        }
    }
}
