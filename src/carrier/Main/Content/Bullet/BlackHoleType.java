package carrier.Main.Content.Bullet;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.CarrierVars;
import carrier.Main.MathComplex;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;

public class BlackHoleType extends DataBulletType{
    public float suctionRadius,force,BlackHoleDamage,interval,shakeStrenghtFrom,shakeStrenghtTo;
    public float BlackHoleRadius,BlackHoleRadiusRemain,BlackHoleRadiusAffect,nearEndEffectDelay,ExtenalEffectDelay;
    public Effect ExtenalEffect,nearEndEffect;
    public boolean fin,push,BlackHoleTeamColor,RemoveVelocity = true;
    public Color color;
    public String[] include ={""};
    public String[] exclude ={""};
    private Vec2 impulse = new Vec2(),v1= new Vec2();
    public BulletType AnotherBullet, BulletSpawn;
       
    public BlackHoleType(float speed,float damage){
        super(speed, damage);
        hittable = absorbable = false;
        collides = collidesAir = collidesGround = collidesTiles = false;
        pierce = true;
    }
    @Override
    public float continuousDamage(){
        return (damage+BlackHoleDamage*0.5f) / interval * 60f; 
    }
    @Override
    public void update(Bullet b){
        if(b.vel.len()>0&&speed>=0&&RemoveVelocity)b.vel.setZero();
        if(b.timer(1,interval)) {
            if (BulletSpawn != null) {
                BulletSpawn.create(b, b.x, b.y, Mathf.random(0, 360));
            }
            float real = suctionRadius + BlackHoleRadius * (fin ? b.fin() : b.fout()) + BlackHoleRadiusRemain;
            int count = Units.count(b.x, b.y, real, unit->!unit.dead||!unit.isNull()||unit.isValid());
            CarrierVars.completeDamage(b.team, b.x, b.y, real - suctionRadius, b.damage + BlackHoleDamage * b.fin() + BlackHoleRadiusAffect * (float) Math.pow(b.fin() * 2, 2), buildingDamageMultiplier, true, true);
            Units.nearbyEnemies(b.team, b.x, b.y , real, unit -> {
                boolean shouldApplyForce = 
                    !(unit.type instanceof TransformType || unit.type instanceof PartType)||
                    (unit.type instanceof TransformType t && !t.ImmuneSuction)||
                    (unit.type instanceof PartType pt && !pt.ImmuneSuction)||
                    (checkUnitName(include, unit.type.name)&&!checkUnitName(exclude, unit.type.name));
                if (unit.team != b.team && shouldApplyForce){
                    v1.set(b).sub(unit);
                    //Cấm về số 0 vì nó sẽ đi backward
                    float pushf = (1 - (v1.len2() / (real * real)))*(real/(Mathf.sqrt(unit.mass())*count));
                    impulse.set(b).sub(unit).setLength(force * (MathComplex.NonNegative(pushf))*(!unit.isFlying()||unit.elevation<=1 ? 1.5f:1f));
                    if (push) impulse.rotate(180f);
                    unit.vel.add(impulse);
                }
            });
        }
        super.update(b);  
    }
    @Override
    public void updateTrailEffects(Bullet b){
        super.updateTrailEffects(b);
        if(ExtenalEffect != null &&b.timer(3, 2f+ExtenalEffectDelay*b.foutpow())&&MathComplex.InRange(b.fin(), 0.06f, 0.9f)){
            ExtenalEffect.lifetime /= (1+1.5f*b.fin()+0.1f);
            ExtenalEffect.at(b.x, b.y, suctionRadius,!BlackHoleTeamColor ? (color != null ? color :b.team.color):b.team.color , b);
        }
        float s = Mathf.lerp(shakeStrenghtFrom, shakeStrenghtTo, b.fin());
        Effect.shake(s,s,b);
        if(b.fin() >= 0.9f&&nearEndEffect!= null&&b.timer(2, nearEndEffectDelay)){
            nearEndEffect.at(b.x, b.y, suctionRadius,!BlackHoleTeamColor ? (color != null ? color :b.team.color):b.team.color , b);
        }

    }

    @Override
    public void draw(Bullet b){
        float bs = Mathf.sin(b.fin()*lifetime*Mathf.PI/3f)*b.fout(Interp.pow3Out);
        Draw.z(Layer.effect);
        Draw.color(!BlackHoleTeamColor ? (color != null ? color :b.team.color):b.team.color);
        Fill.circle(b.x,b.y,BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain+ bs*4f);
        Draw.reset();
        Draw.z(Layer.effect+0.0001f);
        Draw.color(Color.black);
        Fill.circle(b.x,b.y,(BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain+ bs*4f)*0.8f);
        Draw.reset();
        Draw.z(Layer.bullet-0.11f);
        Draw.color(Color.black);
        Fill.circle(b.x,b.y,(BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain + bs*4f)*0.8f);
        Drawf.light(b,(BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain+ bs*4f)*1.2f,Color.white,4);
    }
    @Override
    public void despawned(Bullet b){
        if(despawnEffect != null){
            despawnEffect.at(b.x, b.y, b.rotation(), b.team.color);
        }
        hitSound.at(b);
        despawnSound.at(b);
        if(AnotherBullet != null){
            AnotherBullet.create(b, b.x, b.y, b.rotation());
        }
        Effect.shake(despawnShake, despawnShake, b);
        if(!b.hit && (fragBullet != null || splashDamageRadius > 0f || lightning > 0)){
            hit(b);
        }
    }
    public boolean checkUnitName(String[] names, String targetName) {
        for (String name : names) {
            if (name.equals(targetName)||name.contains(targetName)||name.matches(targetName)||name.contentEquals(targetName)) {
                return true;
            }
        }
        return false;
    }
}
