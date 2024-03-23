package carrier.Main.Content.Bullet;

import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.IntSet;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.MathComplex;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;
import mindustry.core.World;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Healthc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import static mindustry.Vars.*;

public class BlackHoleType extends DataBulletType{
    public float suctionRadius,force,BlackHoleDamage,interval,shakeStrenghtFrom,shakeStrenghtTo;
    public float BlackHoleRadius,BlackHoleRadiusRemain,BlackHoleRadiusAffect,nearEndEffectDelay,ExtenalEffectDelay;
    public Effect ExtenalEffect,nearEndEffect;
    public boolean fin,push,BlackHoleTeamColor;
    public Color color;
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
        if(b.vel.len() >0&&speed >=0)b.vel.setLength(0);
        if(b.timer(1,interval)) {
            if (BulletSpawn != null) {
                BulletSpawn.create(b, b.x, b.y, Mathf.random(0, 360));
            }
            float real = suctionRadius + BlackHoleRadius * (fin ? b.fin() : b.fout()) + BlackHoleRadiusRemain;
            completeDamage(b.team, b.x, b.y, real - suctionRadius, b.damage + BlackHoleDamage * b.fin() + BlackHoleRadiusAffect * (float) Math.pow(b.fin() * 2, 2), buildingDamageMultiplier, true, true);
            Units.nearbyEnemies(b.team, b.x - real, b.y - real, real * 2f, real * 2f, unit -> {
                boolean shouldApplyForce = !(unit.type instanceof TransformType || unit.type instanceof PartType)||
                        (unit.type instanceof TransformType t && !t.ImmuneSuction)||
                        (unit.type instanceof PartType pt && !pt.ImmuneSuction);
                if (unit.within(b.x, b.y, real) && unit.team != b.team && shouldApplyForce) {
                    v1.set(b).sub(unit);
                    //Cấm về số 0 vì nó sẽ đi backward
                    float pushf = (1 - (v1.len2() / (real * real)));
                    impulse.set(b).sub(unit).setLength(force * (pushf < 0 ? 0 : pushf));
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
        float bs = Mathf.sin(b.fin()*lifetime*Mathf.PI)*b.fout(Interp.pow2Out);
        Draw.z(Layer.effect);
        Draw.color(!BlackHoleTeamColor ? (color != null ? color :b.team.color):b.team.color);
        Fill.circle(b.x,b.y,BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain+ bs*4f);
        Draw.reset();
        Draw.z(Layer.effect+2f);
        Draw.color(Color.black);
        Fill.circle(b.x,b.y,(BlackHoleRadius*(fin ? b.fin():b.fout())+BlackHoleRadiusRemain+ bs*4f)*0.8f);
        Draw.reset();
        Draw.z(Layer.effect+2f);
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
    public static void completeDamage(Team team, float x, float y, float radius, float damage){
        completeDamage(team, x, y, radius, damage, 1f, true, true);
    }
    public static void completeDamage(Team team, float x, float y, float radius, float damage, float buildDmbMult, boolean air, boolean ground){
        allNearbyEnemies(team, x, y, radius, t -> {
            if(t instanceof Unit u){
                if(u.isFlying() && air || u.isGrounded() && ground){
                    u.damage(damage);
                }
            }else if(t instanceof Building b){
                if(ground){
                    b.damage(team, damage * buildDmbMult);
                }
            }
        });
    }
    public static void allNearbyEnemies(Team team, float x, float y, float radius, Cons<Healthc> cons){
        Units.nearbyEnemies(team, x - radius, y - radius, radius * 2f, radius * 2f, unit -> {
            if(unit.within(x, y, radius + unit.hitSize / 2f) && !unit.dead){
                cons.get(unit);
            }
        });

        trueEachBlock(x, y, radius, build -> {
            if(build.team != team && !build.dead && build.block != null){
                cons.get(build);
            }
        });
    }
    private static final IntSet collidedBlocks = new IntSet();
    public static void trueEachBlock(float wx, float wy, float range, Cons<Building> cons){
        collidedBlocks.clear();
        int tx = World.toTile(wx);
        int ty = World.toTile(wy);

        int tileRange = Mathf.floorPositive(range / tilesize);

        for(int x = tx - tileRange - 2; x <= tx + tileRange + 2; x++){
            for(int y = ty - tileRange - 2; y <= ty + tileRange + 2; y++){
                if(Mathf.within(x * tilesize, y * tilesize, wx, wy, range)){
                    Building other = world.build(x, y);
                    if(other != null && !collidedBlocks.contains(other.pos())){
                        cons.get(other);
                        collidedBlocks.add(other.pos());
                    }
                }
            }
        }
    }
}
