package carrier.Main.Content.Effect;

import arc.Core;
import arc.func.Floatc2;
import arc.graphics.Color;
import arc.graphics.Texture;
import arc.graphics.g2d.Batch;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Interp;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Mat3D;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.IntMap;
import arc.util.Nullable;
import arc.util.Tmp;
import carrier.Main.MathComplex;
import carrier.Main.Content.Special.CacheBatch3D;
import carrier.Main.Content.Special.Vec2Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Unit;
import mindustry.graphics.*;
import java.util.Arrays;

public class NDEffect {
    public static final float WIDTH = 2.5f;
    public static float trueHitChance = 1;
    public static final float RANGE_RAND = 5f;
    public static final float ROT_DST = Vars.tilesize * 0.6f;
    private static final FloatSeq floatSeq = new FloatSeq();
    static Vec3 v = new Vec3();
    static Vec2 v1 = new Vec2();
    static Vec2 v2 = new Vec2();
    static FloatSeq tf = new FloatSeq(4 * 2);
    public static Rand r = new Rand(),rand= new Rand();
    static boolean drawing3D = false;
    static Batch last;
    private static final Vec2
        tmp1 = new Vec2(),
        tmp2 = new Vec2(),
        tmp3 = new Vec2(),
        vec21 = new Vec2();
    static Mat3D mat3 = new Mat3D();
    public static float[] verts = new float[4 * 6];
    static CacheBatch3D batch3D = new CacheBatch3D();
    private static final Rand rand2 = new Rand();
    public static void draw3DBegin(){
        if(drawing3D) return;
        drawing3D = true;
        last = Core.batch;
        batch3D.begin();
        Core.batch = batch3D;
    }
    public static void draw3DEnd(float x, float y, float rx, float ry, float rz, Runnable pre){
        draw3DEnd(x, y, rx, ry, rz, -0.1f, 0.1f, pre);
    }
    public static Effect chainLightningFade = new Effect(220f, 500f, e -> {
        if(!(e.data instanceof Position)) return;
        Position p = e.data();
        final float lightningAlign = 0.5f;
        float tx = p.getX(), ty = p.getY(), dst = Mathf.dst(e.x, e.y, tx, ty);
        Tmp.v1.set(p).sub(e.x, e.y).nor();
        
        e.lifetime = dst * 0.3f;
        float normx = Tmp.v1.x, normy = Tmp.v1.y;
        float range = e.rotation;
        int links = Mathf.ceil(dst / range);
        float spacing = dst / links;
        
        Lines.stroke(2.5f * Mathf.curve(e.fout(), 0, 0.7f));
        Draw.color(e.color ,Color.white, e.fout() * 0.6f);
        
        Lines.beginLine();
    
        Fill.circle(e.x, e.y, Lines.getStroke() / 2);
        Lines.linePoint(e.x, e.y);
        
        rand.setSeed(e.id);
    
        float fin = Mathf.curve(e.fin(), 0, lightningAlign);
        int i;
        float nx = e.x, ny = e.y;
        for(i = 0; i < (int)(links * fin); i++){
            if(i == links - 1){
                nx = tx;
                ny = ty;
            }else{
                float len = (i + 1) * spacing;
                Tmp.v1.setToRandomDirection(rand).scl(range/2f);
                nx = e.x + normx * len + Tmp.v1.x;
                ny = e.y + normy * len + Tmp.v1.y;
            }
            
            Lines.linePoint(nx, ny);
        }

        if(i < links){
            float f = Mathf.clamp(fin * links % 1);
            float len = (i + 1) * spacing;
            Tmp.v1.setToRandomDirection(rand).scl(range/2f);
            Tmp.v2.set(nx, ny);
            if(i == links - 1)Tmp.v2.lerp(tx, ty, f);
            else Tmp.v2.lerp(e.x + (normx * len + Tmp.v1.x), e.y + (normy * len + Tmp.v1.y), f);
            
            Lines.linePoint(Tmp.v2.x, Tmp.v2.y);
            Fill.circle(Tmp.v2.x, Tmp.v2.y, Lines.getStroke() / 2);
        }
        Lines.endLine();
    }).followParent(false);
    public static void draw3DEnd(float x, float y, float rx, float ry, float rz, float zRangeMin, float zRangeMax, Runnable pre){
        if(!drawing3D) return;
        drawing3D = false;
        mat3.setFromEulerAngles(rx, ry, rz);

        Core.batch = last;
        FloatSeq fsq = batch3D.data;
        int length = fsq.size;
        float[] data = fsq.items;

        if(pre != null) pre.run();
        float srcz = Draw.z();

        float mcol = Color.clearFloatBits;

        for(int i = 0; i < length; i += 24){
            int tix = i / 24;
            float avz = 0f;

            for(int j = 0; j < 4; j++){
                int dix = j * 6 + i;
                int six = j * 6;

                float sx = data[dix];
                float sy = data[dix + 1];
                float sz = data[dix + 2];

                float u1 = data[dix + 3];
                float v1 = data[dix + 4];
                float col = data[dix + 5];

                v.set(sx, sy, sz);
                mul();

                float pz = 700f / (700f - v.z);

                float ix = v.x * pz + x, iy = v.y * pz + y;

                avz += v.z;

                verts[six] = ix;
                verts[six + 1] = iy;
                verts[six + 2] = col;
                verts[six + 3] = u1;
                verts[six + 4] = v1;
                verts[six + 5] = mcol;
            }

            Texture tex = tix < batch3D.textureSeq.size ? batch3D.textureSeq.get(tix) : Core.atlas.white().texture;

            Draw.z(srcz + (avz >= 0 ? zRangeMax : zRangeMin));
            Draw.vert(tex, verts, 0, 24);
        }
    }
    static void mul(){
        final float[] mat = mat3.val;
        v.set(v.x * mat[Mat3D.M00] + v.y * mat[Mat3D.M10] + v.z * mat[Mat3D.M20] + mat[Mat3D.M30], v.x * mat[Mat3D.M01] + v.y * mat[Mat3D.M11] + v.z * mat[Mat3D.M21] + mat[Mat3D.M31], v.x * mat[Mat3D.M02] + v.y * mat[Mat3D.M12] + v.z * mat[Mat3D.M22] + mat[Mat3D.M32]);
    }
    public static Effect BigBlast(Color color,float lifeTime,float RadiusMutli){
        return new Effect(lifeTime,200000,e->{
            Lines.stroke(2f,color);
            float limit = 2*(Math.min(e.fslope(), 0.5f));
            r.setSeed(e.id);
            Rand r2 =new Rand();
            r2.setSeed(e.id);
            float p = limit >= 1f?Mathf.sin(2*Mathf.PI*e.fin()*30f*(lifeTime/60)+r2.random(-Mathf.PI/2,-Mathf.PI/2)):0;
            for(int i=0;i<= r.random(6, 10);i++){
                float t = r.random(0, 2*Mathf.PI);
                Drawf.tri(e.x, e.y, RadiusMutli*(limit*13+p*5), RadiusMutli*(limit*160+p*5+3f*r2.random(-12,12)),t*Mathf.radiansToDegrees);
            }
        });
    }
    public static Effect BigBlast(Color color,float lifeTime,Unit u){
        return BigBlast(color, lifeTime, u.hitSize*0.5f);
    }
    public static Effect CircleSpikeBlastOut(Color color,float radius,float lifeTime,float stroke){
        return CircleSpikeBlastOut(color, radius, lifeTime, stroke, 0);
    }
    public static Effect CircleSpikeBlastOut(Color color,float radius,float lifeTime,float stroke,float OffSetRadius){
        return new Effect(lifeTime,200000,e->{
            float foutLimit =2*(Math.min(e.fout(), 0.5f));
            Lines.stroke(stroke*1.2f*foutLimit,color);
            Drawf.light(e.x, e.y, radius*e.finpow()+OffSetRadius,color.cpy(),e.fout(Interp.pow3Out)*4f);
            Lines.stroke(stroke*foutLimit,color);
            Lines.circleVertices(0.01f);
            Rand r2 =new Rand();
            Lines.circle(e.x, e.y, radius*e.finpow()+OffSetRadius);
            r.setSeed(e.id);
            r2.setSeed(e.id);
            for(int i=0;i<=r.random(6, 18);i++){
                float t = r.random(0, 2*Mathf.PI);
                Drawf.tri(
                    MathComplex.dx(e.x,radius*e.finpow()+OffSetRadius,t*Mathf.radiansToDegrees),
                    MathComplex.dy(e.y,radius*e.finpow()+OffSetRadius,t*Mathf.radiansToDegrees),
                    foutLimit*15,
                    -40*radius/180,
                    t*Mathf.radiansToDegrees);
            }
        });
    }
    public static Effect BlackHoleData(Color color,float BlackHoleSize,float lifeTime,float rotate,float height,float width){
        return BlackHoleData(color,BlackHoleSize,lifeTime,rotate,height,width,false);
    }
    //Wait For It
    public static Effect BlackHoleData(Color color,float BlackHoleSize,float lifeTime,float rotate,float height,float width,boolean EffectSpawnWhenHit){
        return new Effect(lifeTime,200000,e->{
            float size = 0;
            Rand r2 = new Rand(e.id);
            if(e.data != null && e.data instanceof Unit u){
                size = Mathf.sqrt(u.type.hitSize*r2.random(0.7f, 1f)*BlackHoleSize*1.3f)/6f;
            }
            else if(e.data instanceof Building b){
                size = Mathf.sqrt(b.block.size*r2.random(0.7f, 1f));
            }
            else if(EffectSpawnWhenHit)return;
            float xr = r2.random(-20f*size,20f*size),yr = r2.random(-20f*size,20f*size);
            float limit = 2*(Math.min(Interp.pow2Out.apply(e.fslope()), 0.5f));
            float p = limit >= 1f?Mathf.sin(2*Mathf.PI*e.fin()*8f*(lifeTime/60)):0;
            for(int i =0;i<2;i++){
                Draw.color(color,Color.white,e.fout()*0.3f);
                Draw.z(Layer.effect);
                tri(e.x+xr, e.y+yr,(BlackHoleSize+size)*(width*limit+p*0.7f),(height*limit+p*0.7f)*(BlackHoleSize+size), 180*i+rotate);
                Draw.color(Color.black);
                Draw.z(Layer.effect+0.0001f);
                tri(e.x+xr, e.y+yr, (BlackHoleSize+size)*(width*limit*0.66f+p*0.7f),(height*limit*0.66f+p*0.7f)*(BlackHoleSize+size), 180*i+rotate);
                Draw.z(Layer.bullet-0.11f);
                tri(e.x+xr, e.y+yr, (BlackHoleSize+size)*(width*limit*0.66f+p*0.7f),(height*limit*0.66f+p*0.7f)*(BlackHoleSize+size), 180*i+rotate);
            }
        }).layer(Layer.effect - 1f);
    }
    //Idk use this for
    public static Effect ParticleEffectHit(Color color,float size,float life){
        return new ParticleEffect(){{
            lifetime = life;
            particles = 7;
            lightColor = color;
            interp = Interp.circleOut;
            sizeFrom =1f;
            sizeTo=size;
        }};
    }
    public static void drawShockWave(float x, float y, float rx, float ry, float rz, float size, float width, int iter, float zRange){
        float off = (360f / iter);
        //float scl = size + width;
        float zz = Draw.z();

        for(int i = 0; i < iter; i++){
            float angle1 = off * i;
            float angle2 = off * (i + 1);
            float z = 0f;

            tf.clear();
            for(int j = 0; j < 4; j++){
                float w = j == 0 || j == 3 ? width : -width;
                float a = j <= 1 ? angle1 : angle2;

                v2.trns(a, size + w);
                v.set(v2.x, v2.y, 0f).rotate(Vec3.X, rx).rotate(Vec3.Y, ry).rotate(Vec3.Z, rz);
                float sz = 700f / (700f - v.z);
                v.x *= sz;
                v.y *= sz;
                //v.add(x, y, 0f);

                z += v.z;
                tf.add(v.x + x, v.y + y);
            }
            //float tz = Mathf.clamp((z / 4f) / sizeDepth) * zRange + zz;
            float tz = (z < 0f ? -zRange : zRange) + zz;
            Draw.z(tz);
            Fill.polyBegin();
            for(int j = 0; j < 4; j++){
                float vx = tf.items[j * 2];
                float vy = tf.items[j * 2 + 1];
                Fill.polyPoint(vx, vy);
            }
            Fill.polyEnd();
        }
        Draw.z(zz);
    }
    public static Effect HitShockWaveEffect(float x, float y, float rx, float ry, float rz, float size, float width, int iter, float zRange,float lifeTime,Color color){
        return 
        new Effect(60,e->{
            Draw.color(color,e.fout());
            Tmp.v1.trns(e.rotation, e.fin()*20f);
            drawShockWave(e.x+ x, e.y + y, rx, ry, rz-e.rotation-90, size*e.fin(Interp.pow2Out), width*e.fin(Interp.pow2Out), iter, zRange);
            Draw.reset();
        });
    }
    public static Effect ChargeEffect(float lifetime,float circleRadius,Color color){
        return new Effect(4*60,e->{
            Draw.color(color);
            Fill.circle(e.x,e.y,e.fin()*circleRadius);
            Draw.reset();
            Draw.color(Color.black);
            Fill.circle(e.x,e.y,e.fin()*circleRadius*0.7f);
            Draw.reset();
            Draw.color(Color.black);
            Fill.circle(e.x,e.y,e.fin()*circleRadius*0.7f);
        }){{
            followParent =rotWithParent = true;
            followParent(true).rotWithParent(true);
        }};
    }
    public static void DrawProgressCircle(float z,float rad,float thick,float progress,Position source,Color color,float angle){
        Draw.color(color);
        float p = Mathf.clamp(progress),x=source.getX(),y= source.getY();
        Lines.stroke(thick);
        Draw.z(z);
        
        int sides = Lines.circleVertices(rad);
        
        float space = 360.0F / (float)sides;
        float len = 2 * rad * Mathf.sinDeg(space / 2);
        float hstep = Lines.getStroke() / 2.0F / Mathf.cosDeg(space / 2.0F);
        float r1 = rad - hstep;
        float r2 = rad + hstep;
        
        int i;
        
        for(i = 0; i < sides * p - 1; ++i){
            float a = space * (float)i + angle;
            float cos = Mathf.cosDeg(a);
            float sin = Mathf.sinDeg(a);
            float cos2 = Mathf.cosDeg(a + space);
            float sin2 = Mathf.sinDeg(a + space);
            Fill.quad(x + r1 * cos, y + r1 * sin, x + r1 * cos2, y + r1 * sin2, x + r2 * cos2, y + r2 * sin2, x + r2 * cos, y + r2 * sin);
        }
    
        float a = space * i + angle;
        float cos = Mathf.cosDeg(a);
        float sin = Mathf.sinDeg(a);
        float cos2 = Mathf.cosDeg(a + space);
        float sin2 = Mathf.sinDeg(a + space);
        float f = sides * p - i;
        vec21.trns(a, 0, len * (f - 1));
        Fill.quad(x + r1 * cos, y + r1 * sin, x + r1 * cos2 + vec21.x, y + r1 * sin2 + vec21.y, x + r2 * cos2 + vec21.x, y + r2 * sin2 + vec21.y, x + r2 * cos, y + r2 * sin);
    }
    public static int hash(String m, Color c){
		return Arrays.hashCode(new int[]{m.hashCode(), c.hashCode()});
	}
    public static final IntMap<Effect> same = new IntMap<>();
    public static Effect get(String m, Color c, Effect effect){
		int hash = hash(m, c);
		Effect or = same.get(hash);
		if(or == null)same.put(hash, effect);
		return or == null ? effect : or;
	}
    public static  Effect posLightning(float lifetime){
        return new Effect(lifetime, 1200.0f, e -> {
            if(!(e.data instanceof Vec2Seq)) return;
            Vec2Seq lines = e.data();
            
            Draw.color(e.color, Color.white, e.fout() * 0.6f);
            
            Lines.stroke(e.rotation * e.fout());
            
            Fill.circle(lines.firstTmp().x, lines.firstTmp().y, Lines.getStroke() / 2f);
            
            for(int i = 0; i < lines.size() - 1; i++){
                Vec2 cur = lines.setVec2(i, Tmp.v1);
                Vec2 next = lines.setVec2(i + 1, Tmp.v2);
                
                Lines.line(cur.x, cur.y, next.x, next.y, false);
                Fill.circle(next.x, next.y, Lines.getStroke() / 2f);
            }
        }).layer(Layer.effect - 0.001f);
    }
    public static void createBoltEffect(Color color, float width, Vec2Seq vets,float lifeTime) {
	    vets.each(((x, y) -> {
			if(Mathf.chance(0.0855))lightningSpark().at(x, y, rand2.random(2f + width, 4f + width), color);
		}));
		posLightning(lifeTime).at((vets.firstTmp().x + vets.peekTmp().x) / 2f, (vets.firstTmp().y + vets.peekTmp().y) / 2f, width, color, vets);
	}
    
    public static Effect lightningFade(float lifetime){
         return new Effect(posLightning(lifetime).lifetime, 1200.0f, e -> {
            if(!(e.data instanceof Vec2Seq)) return;
            Vec2Seq points = e.data();
            e.lifetime = points.size() < 2 ? 0 : 1000;
            int strokeOffset = (int)e.rotation;
            if(points.size() > strokeOffset + 1 && strokeOffset > 0 && points.size() > 2){
                points.removeRange(0, points.size() - strokeOffset - 1);
            }
            if(!Vars.state.isPaused() && points.any()){
                points.remove(0);
            }
            if(points.size() < 2)return;
            Vec2 data = points.peekTmp();
            float stroke = data.x;
            float fadeOffset = data.y;
            
            Draw.color(e.color);
            for(int i = 1; i < points.size() - 1; i++){
                Lines.stroke(Mathf.clamp((i + fadeOffset / 2f) / points.size() * (strokeOffset - (points.size() - i)) / strokeOffset) * stroke);
                Vec2 from = points.setVec2(i - 1, Tmp.v1);
                Vec2 to = points.setVec2(i, Tmp.v2);
                Lines.line(from.x, from.y, to.x, to.y, false);
                Fill.circle(from.x, from.y, Lines.getStroke() / 2);
            }
            
            Vec2 last = points.tmpVec2(points.size() - 2);
            Fill.circle(last.x, last.y, Lines.getStroke() / 2);
        }).layer(Layer.effect - 0.001f);
    }
    public static Effect lightningSpark(){
        return new Effect(Fx.chainLightning.lifetime, e -> {
			Draw.color(Color.white, e.color, e.fin() + 0.25f);
			Lines.stroke(0.65f + e.fout());
			Angles.randLenVectors(e.id, 3, e.fin() * e.rotation + 6f, (x, y) -> Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fout() * 4 + 2f));
			Fill.circle(e.x, e.y, 2.5f * e.fout());
		});
    }
    public static Effect lightningHitSmall(Color color){
		return get("lightningHitSmall", color, new Effect(20, e -> {
			Draw.color(color, Color.white, e.fout() * 0.7f);
			Angles.randLenVectors(e.id, 5, 18 * e.fin(), (x, y) -> {
				Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 6 + 2);
				Drawf.light(e.x + x, e.y + y, e.fin() * 12 * e.fout(0.25f), color, 0.7f);
			});
		}));
	}
    public static Effect SolidCircleCollapse(float lifeTime,Color color,float radius){
        return new Effect(lifeTime,200000,e->{
            Draw.color(color.cpy());
            Fill.circle(e.x, e.y, radius*e.fout());
        });
    }
    public static Effect TriAngleCircleSpreardSpike(float lifeTime,float radius,Color color,int SpikeCreate,float spikeWithin,float spikeLenght,float randLenght){
        return  TriAngleCircleSpreardSpike(lifeTime,radius,color,SpikeCreate,spikeWithin,spikeLenght,randLenght,false,0f);
    }
    public static Effect TriAngleCircleSpreardSpike(float lifeTime,float radius,Color color,int SpikeCreate,float spikeWithin,float spikeLenght,float randLenght,boolean ApplyDamage,float damage){
        return new Effect(lifeTime,200000,e->{
            Draw.color(color.cpy());
            float foutLimit =e.fin(Interp.pow3Out)*1.1f*2*(Math.min(e.fout(), 0.5f));
            Rand rand1 = new Rand(e.id);
            for(int i = 0;i<SpikeCreate;i++) {
                float rot = 360 * rand1.random(0f, 1f);
                Tmp.v1.trns(rot, (radius + rand1.random(-randLenght, randLenght)) * e.finpow());
                for (int j : Mathf.signs) {
                    Drawf.tri(e.x + Tmp.v1.x, e.y + Tmp.v1.y, spikeWithin * foutLimit, spikeLenght * foutLimit, rot + 90 * j);
                }
                if (ApplyDamage && e.data instanceof Bullet b){
                    Damage.damage(b.team(),e.x + Tmp.v1.x , e.y + Tmp.v1.y,(spikeLenght+spikeWithin) * foutLimit/2f,damage*foutLimit/60f);
                }
            }
        });
    }
    public static Effect TriAngleSpike(float lifeTime,Color color,float spikeWithin,float spikeLenght){
        return new Effect(lifeTime,200000,e->{
            float foutLimit =2*(Math.min(e.fout(), 0.5f));
            Draw.color(color.cpy());
            for(int j:Mathf.signs){
                Drawf.tri(e.x, e.y, spikeWithin*foutLimit, spikeLenght*foutLimit, e.rotation+90*j);
            }
        });
    }
    public static Effect ShockWave(float lifeTime,Color color,float radius,float Offset,float strokeFrom,float strokeTo){
        return new Effect(lifeTime,200000,e->{
            Draw.color(color.cpy().a(e.foutpow()));
            Drawf.light(e.x, e.y, radius*e.finpow(),color.cpy(),e.fout(Interp.pow3Out)*4f);
            Lines.stroke(Mathf.lerp(strokeFrom, strokeTo, e.finpow()));
            Lines.circleVertices(0.01f);
            Lines.circle(e.x, e.y, Mathf.lerp(Offset,radius,e.fin(Interp.pow2Out)));
        });
    }
    public static Effect Absorbed(float lifeTime,Color color,float radius,float Offset,float strokeFrom,float strokeTo){
        return new Effect(lifeTime,200000,e->{
            Draw.color(color.cpy().a(e.finpow()));
            Lines.stroke(Math.abs(strokeFrom*e.fout() + strokeTo));
            Lines.circleVertices(0.01f);
            Lines.circle(e.x, e.y, radius*e.fout() + Offset);
        });
    }
    public static Effect FragmentExplode(float lifeTime,Color color,float radius,float lenght,float stroke,int fragment){
        return new Effect(lifeTime,200000,e->{
            float add = 0;
            if(e.data instanceof Unit u){
                add = u.type.hitSize/8;
            }
            Rand rand = new Rand(e.id);
            Draw.color(color);
            Lines.stroke(e.fout()*stroke*1.1f);
            
            for(int i=0;i< fragment;i++){
                float s = rand.random(0.1f,1f);
                float rot = 360f*rand.random(0f, 1f);
                Tmp.v2.trns(rot, e.fin()*(radius+add+e.fout()*lenght)*s*1.1f);
                Tmp.v3.trns(rot, e.fin()*(radius+add-e.fout()*lenght)*s*1.1f);
                Lines.line(e.x+Tmp.v2.x,e.y+Tmp.v2.y,e.x+Tmp.v3.x,e.y+Tmp.v3.y);
            }
        });
    }
    //Lol lazy create some name
    public static Effect FragmentVaccum(float lifeTime,Color color,float radius,float lenght,float stroke,int fragment){
        return new Effect(lifeTime,200000,e->{
            Rand rand = new Rand(e.id);
            Draw.color(color.cpy().a(Interp.pow2Out.apply(e.fslope())));
            Lines.stroke(e.fout()*stroke);
            for(int i=0;i< fragment;i++){
                float s = rand.random(0.1f,1f);
                float rot = 360f*rand.random(0f, 1f);
                Tmp.v2.trns(rot, e.fout()*(radius+e.fout()*lenght)*s);
                Tmp.v3.trns(rot, e.fout()*(radius-e.fout()*lenght)*s);
                Lines.line(e.x+Tmp.v2.x,e.y+Tmp.v2.y,e.x+Tmp.v3.x,e.y+Tmp.v3.y);
            }
        });
    }
    public static Effect Star4Wings(float lifeTime,Color color,float height,float width,float angleOffSet,boolean FadeIn){
        return new Effect(lifeTime,200000,e->{
            float p = FadeIn ? e.fin():e.fout();
            Draw.color(color);
            for(int i =0;i<4;i++){
                Drawf.tri(e.x, e.y, width*e.fout(Interp.pow2Out), height*p, e.rotation + i*90+angleOffSet);
            } 
        });
    }
    public static Effect StarNWings(float lifeTime,Color color,float height,float width,float angleOffSet,boolean FadeIn,int Wing,float randLenght){
        return new Effect(lifeTime,200000,e->{
            Rand r = new Rand(e.id);
            float p = FadeIn ? e.fin():e.fout();
            Draw.color(color);
            for(int i =0;i<Wing;i++){
                float rd = r.random(0f, 1f)*randLenght;
                Drawf.tri(e.x, e.y, (width+rd/100)*e.fout(Interp.pow2Out), (height+rd)*p, (i-2)*360/Wing);
            } 
        });
    }
    public static Effect makeSwirlEffect(Color color, float eLifetime, int length, float maxWidth, float minRot, float maxRot, float minDst, float maxDst, boolean lerp){
        return new Effect(eLifetime, 400f, e -> {
            if(e.time < 1f) return;

            float lifetime = e.lifetime - length;
            float dst;
            if(minDst < 0 || maxDst < 0){
                dst = Math.abs(e.rotation);;
            }else{
                dst = Mathf.randomSeed(e.id, minDst, maxDst);
            }
            if(lerp){
                Draw.color(color, e.color, Mathf.clamp(e.time / lifetime));
            }else{
                Draw.color(color);
            }

            int points = (int)Math.min(e.time, length);
            float width = Mathf.clamp(e.time / (e.lifetime - length)) * maxWidth;
            float size = width / points;
            float baseRot = Mathf.randomSeed(e.id + 1, 360f), addRot = Mathf.randomSeed(e.id + 2, minRot, maxRot) * Mathf.sign(e.rotation);

            float fout, lastAng = 0f;
            for(int i = 0; i < points; i++){
                fout = 1f - Mathf.clamp((e.time - points + i) / lifetime);
                v1.trns(baseRot + addRot * Mathf.sqrt(fout), Mathf.maxZero(dst * fout));
                fout = 1f - Mathf.clamp((e.time - points + i + 1) / lifetime);
                v2.trns(baseRot + addRot * Mathf.sqrt(fout), Mathf.maxZero(dst * fout));

                float a2 = -v1.angleTo(v2) * Mathf.degRad;
                float a1 = i == 0 ? a2 : lastAng;

                float
                    cx = Mathf.sin(a1) * i * size,
                    cy = Mathf.cos(a1) * i * size,
                    nx = Mathf.sin(a2) * (i + 1) * size,
                    ny = Mathf.cos(a2) * (i + 1) * size;

                Fill.quad(
                    e.x + v1.x - cx, e.y + v1.y - cy,
                    e.x + v1.x + cx, e.y + v1.y + cy,
                    e.x + v2.x + nx, e.y + v2.y + ny,
                    e.x + v2.x - nx, e.y + v2.y - ny
                );

                lastAng = a2;
            }
            Draw.rect("hcircle", e.x + v2.x, e.y + v2.y, width * 2f, width * 2f, -Mathf.radDeg * lastAng);
        });
    }

    public static Effect makeSwirlEffect(float eLifetime, int length, float maxWidth, float minRot, float maxRot, boolean lerp){
        return makeSwirlEffect(Color.black, eLifetime, length, maxWidth, minRot, maxRot, -1, -1, lerp);
    }
    public static Effect BlackHoleCollapse(float lifeTime,Color color,float BlackHoleRadius,boolean fin){
        return new Effect(lifeTime,200000f,b->{
            Draw.z(Layer.effect);
            Draw.color(color.cpy().a(fin ?b.fout(Interp.pow3Out):1));
            Fill.circle(b.x,b.y,BlackHoleRadius*(fin ? b.fin(Interp.pow3Out):b.fout(Interp.pow3Out)));
            Draw.reset();
            Draw.z(Layer.effect+0.0001f);
            Draw.color(Color.black.cpy().a(1));
            Fill.circle(b.x,b.y,(BlackHoleRadius*(fin ? b.fin(Interp.pow3Out):b.fout(Interp.pow3Out)))*0.8f);
            Draw.reset();
            Draw.z(Layer.bullet - 0.11f);
            Draw.color(Color.black.cpy().a(1));
            Fill.circle(b.x,b.y,(BlackHoleRadius*(fin ? b.fin(Interp.pow3Out):b.fout(Interp.pow3Out)))*0.8f);
            Drawf.light(b.x,b.y,BlackHoleRadius*(fin ? b.fin(Interp.pow3Out):b.fout(Interp.pow3Out))*1.1f,Color.white,4);
        });
    }
    public static Effect SpikeCircle2(float lifeTime,Color color,float radius,int SpikeCreate,float randLenght,float width){
        return new Effect(lifeTime,200000f,e->{
            Rand rand = new Rand(e.id);
            Draw.color(color.cpy());
            float rd = 360*rand.random(0f, 1f);
            for(int i = 1;i<=SpikeCreate;i++){
                float rmu = rand.random(-0.5f, 0.5f);
                float r = radius+randLenght*e.fout(Interp.pow2Out)*rmu,
                    rot=rd+((float) ((i - 2) * 360) /SpikeCreate);
                Tmp.v1.trns(rot,r*0.3f);
                Tmp.v2.trns(rot,r);
                Drawf.tri(e.x+Tmp.v1.x, e.y+Tmp.v1.y,(width+randLenght/100*rmu)*e.fout(Interp.pow2Out), -r*0.3f, rot);
                Drawf.tri(e.x+Tmp.v1.x, e.y+Tmp.v1.y,(width+randLenght/100*rmu)*e.fout(Interp.pow2Out), r*0.7f, rot);
                Drawf.light(e.x, e.y, e.x+Tmp.v2.x, e.y+Tmp.v2.y);
            }
        });
    }
    public static Effect TransformAppear(Color color, @Nullable Effect eff, float lifeTime, String spriteName,@Nullable Unit un,float offsetRotation){
        return new MultiEffect(eff,
            new Effect(lifeTime,200000f,e->{
                if(un != null)e.data = un;
                if(e.data instanceof Unit u){
                    e.rotation = u.rotation+offsetRotation;
                    Draw.mixcol(color,color, e.fout());
                    Draw.alpha(e.fout());
                    Lines.stroke(1f);
                    Draw.rect(Core.atlas.find("carrier-mod-"+spriteName+"-white"),e.x,e.y,e.rotation-90);
                }
        }));
    };
    public static Effect TransformWeaponsAppear(Color color, @Nullable Effect eff, float lifeTime,WeaponMount w ,Unit u){
        return new MultiEffect(eff,
        new Effect(lifeTime,200000f,e->{
            e.rotation = u.rotation;
            float
                rotation = u.rotation - 90,
                realRecoil = Mathf.pow(w.recoil, w.weapon.recoilPow) *  w.weapon.recoil,
                weaponRotation  = rotation + (w.weapon.rotate ? w.rotation : w.weapon.baseRotation),
                wx = u.x + Angles.trnsx(rotation, w.weapon.x, w.weapon.y)+ Angles.trnsx(weaponRotation, 0, -realRecoil),
                wy = u.y + Angles.trnsy(rotation, w.weapon.x, w.weapon.y)+ Angles.trnsy(weaponRotation, 0, -realRecoil);
                Draw.mixcol(color,color, e.fout());
                Draw.alpha(e.fout());
                Lines.stroke(1f);
                Draw.rect(Core.atlas.find(w.weapon.name+"-white"),wx,wy,weaponRotation);
        }));
    };
    public static Effect shootLine(float size, float angleRange){
		int num = Mathf.clamp((int)size / 6, 6, 20);
		float thick = Mathf.clamp(0.75f, 2f, size / 22f);
		
		return new Effect(37f, e -> {
            if(e.data instanceof Bullet b)e.rotation = b.rotation();
            else if (e.data instanceof WeaponMount m)e.rotation = m.rotation;
			Draw.color(e.color, Color.white.cpy(), e.fout() * 0.7f);
			rand.setSeed(e.id);
			randLenVectors(e.id, num, 4 + (size * 1.2f) * e.fin(), size * 0.15f * e.fin(), e.rotation, angleRange, (x, y) -> {
				Lines.stroke(thick * e.fout(0.32f));
				Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), (e.fslope() + e.fin()) * 0.5f * (size * rand.random(0.15f, 0.5f) + rand.random(2f)) + rand.random(2f));
				Drawf.light(e.x + x, e.y + y, e.fslope() * (size * 0.5f + 14f) + 3, e.color, 0.7f);
			});
		});
	}
    public static void randLenVectors(long seed, int amount, float length, float minLength, float angle, float range, Floatc2 cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            vec21.trns(angle + rand.range(range), minLength  + rand.random(length));
            cons.get(vec21.x, vec21.y);
        }
    }
    public static void randLenVectors(long seed, float fin, int amount, float minLength, float length, Angles.ParticleConsumer cons){
        rand.setSeed(seed);
        for(int i = 0; i < amount; i++){
            float l = rand.nextFloat();
            vec21.trns(rand.random(360f), length * l * fin + minLength);
            cons.accept(vec21.x, vec21.y, fin * l, (1f - fin) * l);
        }
    }
    public static void tri(float x, float y, float width, float length, float angle){
        float wx = Angles.trnsx(angle + 90, width), wy = Angles.trnsy(angle + 90, width);
        Fill.tri(x + wx, y + wy, x - wx, y - wy, Angles.trnsx(angle, length) + x, Angles.trnsy(angle, length) + y);
    }
    public static void createEffect(Position from, Position to, Color color, int lightningNum, float width,float lifeTime){
		if(Vars.headless)return;
		
		if(lightningNum < 1){
			Fx.chainLightning.at(from.getX(), from.getY(), 0, color, new Vec2().set(to));
		}else{
			float dst = from.dst(to);
			
			for(int i = 0; i < lightningNum; i++){
				float len = Mathf.random(1f, 7f);
				float randRange = len * RANGE_RAND;
				
				floatSeq.clear();
				FloatSeq randomArray = floatSeq;
				for(int num = 0; num < dst / (ROT_DST * len) + 1; num++){
					randomArray.add(Mathf.range(randRange) / (num * 0.025f + 1));
				}
				createBoltEffect(color, width, computeVectors(randomArray, from, to),lifeTime);
			}
		}
	}
    private static Vec2Seq computeVectors(FloatSeq randomVec, Position from, Position to){
		int param = randomVec.size;
		float angle = from.angleTo(to);
		
		Vec2Seq lines = new Vec2Seq(param);
		tmp1.trns(angle, from.dst(to) / (param - 1));
		
		lines.add(from);
		for (int i = 1; i < param - 2; i ++)lines.add(tmp3.trns(angle - 90, randomVec.get(i)).add(tmp1, i).add(from.getX(), from.getY()));
		lines.add(to);
		
		return lines;
	}
    public static void createEffect(Position from, float length, float angle, Color color, int lightningNum, float width,float lifeTime){
		if(Vars.headless)return;
		createEffect(from, tmp2.trns(angle, length).add(from), color, lightningNum, width,lifeTime);
	}
    public static Effect ChargeEffectStar4Wing(float lifeTime,Color color,boolean FadeIn,float width,float height,float angleOffSet,float angleForm,float angleTo,float startDelay){
        return new Effect(lifeTime,200000,e->{
            float pcut = MathComplex.ProgressFadeOutDelay(e.fslope(),0.3f);
            Draw.color(color);
            for(int i =0;i<4;i++){
                Drawf.tri(e.x, e.y, width*pcut, height, e.rotation + i*90+angleOffSet+Mathf.lerp(angleForm,angleTo,e.fin(Interp.pow3Out)));
            } 
        }).startDelay(startDelay);
    }
}
