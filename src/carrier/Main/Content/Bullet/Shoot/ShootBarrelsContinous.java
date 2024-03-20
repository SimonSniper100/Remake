package carrier.Main.Content.Bullet.Shoot;

import arc.util.Nullable;
import mindustry.entities.pattern.ShootBarrel;

public class ShootBarrelsContinous extends ShootBarrel{
    public int BulletPerShot;
    @Override
    public void shoot(int totalShots, BulletHandler handler, @Nullable Runnable barrelIncrementer){
        for(int i = 0; i < shots; i++){
            int index = ((i + totalShots + barrelOffset) % (barrels.length / 3)) * 3;
            handler.shoot(barrels[index], barrels[index + 1], barrels[index + 2], firstShotDelay + shotDelay *(int)(i/BulletPerShot));
            if(barrelIncrementer != null) barrelIncrementer.run();
        }
    }
}