package carrier.Main.Content.Bullet;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import carrier.Main.Content.Item.SpecialItem;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.blocks.defense.turrets.ItemTurret;

public class DataBulletType extends BasicBulletType {
    public boolean readDataUnit = false;
    private int ammoTypeMatch;
    private float damageBouns = 1f;
    public DataBulletType(float speed,float damage){
        super(speed, damage);
    }
    public DataBulletType(float speed,float damage,String bulletSpirte){
        super(speed, damage,bulletSpirte);
    }
    public DataBulletType(){
        super();
    }
    @Override
    public void init(Bullet b){
        super.init(b);
        if(b.owner instanceof Unit u){
            if(u.type.ammoType instanceof ItemAmmoType iat && iat.item instanceof SpecialItem sp && sp.damageBounsForUnit ){
                //1f = 1%
                b.damage *= (sp.damageBouns+100f)/100f;
            
            }
        }
        //lười lám nên chc làm average;
        else if(b.owner instanceof Building bulid && bulid.block instanceof ItemTurret t && t.ammoTypes!= null){
            t.ammoTypes.each((item,bt)->{
                if(item instanceof SpecialItem sp&&sp.damageBounsForBlock){
                    ammoTypeMatch++;
                    damageBouns += sp.damageBouns/100f;
                }
            });
            //Chia tỉ lệ lại và tính trung bình
            damageBouns /= (float)ammoTypeMatch;
            b.damage *= damageBouns;
        }
        //đề phòng overlap
        ammoTypeMatch = 0;
        damageBouns =1f;   
    }
    //Copy And Then Modified
    @Override
    public void hit(Bullet b,float x,float y){
        var target = Units.closestEnemy(b.team,b.x,b.y,3,s->!s.dead);
        hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
        hitEffect.at(x, y, b.rotation(), hitColor,readDataUnit ? target:null);
        Effect.shake(hitShake, hitShake, b);

        if(fragOnHit){
            createFrags(b, x, y);
        }
        createPuddles(b, x, y);
        createIncend(b, x, y);
        createUnits(b, x, y);

        if(suppressionRange > 0){
            //bullets are pooled, require separate Vec2 instance
            Damage.applySuppression(b.team, b.x, b.y, suppressionRange, suppressionDuration, 0f, suppressionEffectChance, new Vec2(b.x, b.y));
        }

        createSplashDamage(b, x, y);

        for(int i = 0; i < lightning; i++){
            Lightning.create(b, lightningColor, lightningDamage < 0 ? damage : lightningDamage, b.x, b.y, b.rotation() + Mathf.range(lightningCone/2) + lightningAngle, lightningLength + Mathf.random(lightningLengthRand));
        }
    }
}
