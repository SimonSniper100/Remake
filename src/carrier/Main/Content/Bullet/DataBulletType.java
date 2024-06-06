package carrier.Main.Content.Bullet;


import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Tmp;
import carrier.Main.Content.Item.SpecialItem;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Fires;
import mindustry.entities.Lightning;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.game.EventType.UnitDamageEvent;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Hitboxc;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.ammo.ItemAmmoType;
import mindustry.world.blocks.defense.turrets.ItemTurret.ItemEntry;
import mindustry.world.blocks.defense.turrets.ItemTurret.ItemTurretBuild;

public class DataBulletType extends BasicBulletType {
    static final UnitDamageEvent bulletDamageEvent = new UnitDamageEvent();
    public boolean readDataUnit = false,readDataBlock = true;
    public float DectectRange;
    public Color ShootColor;
    public float StatusEffectsStackTime;
    public Seq<StatusEffect> statusStackable = new Seq<>(); 
    private static float damageBouns = 1f;
    public DataBulletType(float speed,float damage){
        super(speed, damage);
        readDataUnit = true;
    }
    public DataBulletType(float speed,float damage,String bulletSpirte){
        super(speed, damage,bulletSpirte);
        readDataUnit = true;
    }
    public DataBulletType(){
        super();
        readDataUnit = true;
        
    }
    public static class DataBulletDamage{
        public float damage;
        public DataBulletDamage(float damage){
            this.damage = damage;
        }
    }
    @Override
    public void init(Bullet b){
        super.init(b);
        shootEffect.wrap(ShootColor, b.rotation());
        if(b instanceof DataBullet db){
            if(b.owner instanceof Unit u){
                if(u.type.ammoType instanceof ItemAmmoType iat && iat.item instanceof SpecialItem sp && sp.damageBounsForUnit){
                    //1f = 1%
                    float d= (sp.damageBouns+100f)/100f;
                    db.dataArray.add(new DataBulletDamage(d)); 
                }
            }
            else if(b.owner instanceof ItemTurretBuild bulid && !bulid.ammo.isEmpty()){
                var item = bulid.ammo;
                for(var wut : item){
                    ItemEntry i = (ItemEntry)wut;
                    if(i.item instanceof SpecialItem s&&s.damageBounsForBlock){
                        damageBouns += s.damageBouns/100f;
                    }
                    db.dataArray.add(new DataBulletDamage(damageBouns));
                }
            }
        }
        if(b instanceof DataBullet dt&&!dt.dataArray.isEmpty()){
            for(Object obj : dt.dataArray){
                if(obj instanceof DataBulletDamage dam&&dam.damage>0){
                    b.damage *=dam.damage;
                    break;
                }
                else continue;
            }
        }
    }
    //Copy And Then Modified
    @Override
    public void hit(Bullet b,float x,float y){
        b.hit = true;
        var target = Units.closestEnemy(b.team,b.x,b.y,5,e->!e.dead);
        var target2 = Units.findEnemyTile(b.team, b.x, b.y, 5, block->!block.dead);
        hitSound.at(x, y, hitSoundPitch, hitSoundVolume);
        if(target != null){
            hitEffect.at(x, y, b.rotation(), hitColor,readDataUnit ? target:null);
        }
        else if(target2 != null){
            hitEffect.at(x, y, b.rotation(), hitColor,readDataBlock ? target2:null);
        }
        else {
            hitEffect.at(x, y, b.rotation(), hitColor);
        }
        Effect.shake(hitShake, hitShake, b);

        if(fragOnHit&&b.hit){
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
    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health){
        super.hitEntity(b, entity, health);
        if(entity instanceof Unit unit){
            Tmp.v3.set(unit).sub(b).nor().scl(knockback * 80f);
            if(impact) Tmp.v3.setAngle(b.rotation() + (knockback < 0 ? 180f : 0f));
            unit.impulse(Tmp.v3);
            if(!statusStackable.isEmpty()){
                for(var stat:statusStackable){
                    unit.apply(stat,StatusEffectsStackTime);
                }
            }
            Events.fire(bulletDamageEvent.set(unit, b));
        }
    }
    public class DataBullet extends Bullet{
        public final Seq<Object> dataArray = new Seq<>();
        public WeaponMount weaponOwner;       
    
    }
    @Override
    public void createSplashDamage(Bullet b, float x, float y){
        if(splashDamageRadius > 0 && !b.absorbed){
            Damage.damage(b.team, x, y, splashDamageRadius, splashDamage * b.damageMultiplier(), splashDamagePierce, collidesAir, collidesGround, scaledSplashDamage, b);

            if(status != StatusEffects.none){
                Damage.status(b.team, x, y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
            }
            if(!statusStackable.isEmpty()){
                for(var stat:statusStackable){
                    Damage.status(b.team, x, y, splashDamageRadius, stat, StatusEffectsStackTime, collidesAir, collidesGround);
                }
            }
            if(heals()){
                Vars.indexer.eachBlock(b.team, x, y, splashDamageRadius, Building::damaged, other -> {
                    healEffect.at(other.x, other.y, 0f, healColor, other.block);
                    other.heal(healPercent / 100f * other.maxHealth() + healAmount);
                });
            }

            if(makeFire){
                Vars.indexer.eachBlock(null, x, y, splashDamageRadius, other -> other.team != b.team, other -> Fires.create(other.tile));
            }
        }
    }
}
