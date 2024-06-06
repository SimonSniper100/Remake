package carrier.Main.Content.Bullet;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Special.Vec2Seq;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.gen.Hitboxc;

public class TrailFadeBulletType extends AccelBulletType{
	public int tracers = 2;
	public float tracerStroke = 3F;
	public int tracerFadeOffset = 10;
	public int tracerStrokeOffset = 15;
	public float lifeTimeEffect;
	public float tracerSpacing = 8f;
	public float tracerRandX = 6f;
	public Effect OtherEffect;
	public float tracerUpdateSpacing = 0.3f;
	public BulletType Extra;
	public Color color;
	/** Whether add the spawn point of the bullet to the trail seq.*/
	public boolean addBeginPoint = false;
	public boolean hitBlinkTrail = false;
	public boolean despawnBlinkTrail = false;
	
	public TrailFadeBulletType(){
		super();
	}
	
	public TrailFadeBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);
		
		impact = true;
	}
	
	public TrailFadeBulletType(float speed, float damage) {
		super(speed, damage, "");
	}
	
	protected static final Vec2 v1 = new Vec2(), v2 = new Vec2(), v3 = new Vec2();
	protected static final Rand rand = new Rand();
	
	@Override
	public void despawned(Bullet b){
		if(Extra!= null)Extra.create(b, b.x, b.y, b.rotation(),0);
		if(!Vars.headless && (b.data instanceof Vec2Seq[])){
			Vec2Seq[] pointsArr = (Vec2Seq[])b.data();
			for(Vec2Seq points : pointsArr){
				points.add(b.x, b.y);
				if(despawnBlinkTrail || (b.absorbed && hitBlinkTrail)){
					NDEffect.createBoltEffect(hitColor, tracerStroke * 2f, points,lifeTimeEffect);
					Vec2 v = points.firstTmp();
					if(OtherEffect != null){
						OtherEffect.at(b.x,b.y);
					}
					NDEffect.lightningHitSmall(hitColor).at(v.x, v.y, hitColor);
				}else{
					points.add(tracerStroke, tracerFadeOffset);
					NDEffect.lightningFade(lifeTimeEffect).at(b.x, b.y, tracerStrokeOffset, hitColor, points);
				}
			}
			
			b.data = null;
		}
		
		super.despawned(b);
	}
	
	@Override
	public void hitEntity(Bullet b, Hitboxc entity, float health){
		super.hitEntity(b, entity, health);
		hit(b);
	}
	
	@Override
	public void hit(Bullet b){
		super.hit(b);
		if(Extra!= null)Extra.create(b, b.x, b.y, b.rotation());
		if(Vars.headless || !(b.data instanceof Vec2Seq[]))return;
		Vec2Seq[] pointsArr = (Vec2Seq[])b.data();
		for(Vec2Seq points : pointsArr){
			points.add(b.x, b.y);
			if(hitBlinkTrail){
				NDEffect.createBoltEffect(hitColor, tracerStroke * 2f, points,lifeTimeEffect);
				Vec2 v = points.firstTmp();
				NDEffect.lightningHitSmall(hitColor).at(v.x, v.y, hitColor);
				if(OtherEffect != null){
					OtherEffect.at(b.x,b.y);
				}
			}else{
				points.add(tracerStroke, tracerFadeOffset);
				NDEffect.lightningFade(lifeTimeEffect).at(b.x, b.y, tracerStrokeOffset, hitColor, points);
			}
		}
		
		b.data = null;
	}
	@Override
	public void init(Bullet b){
		super.init(b);
		if(Vars.headless)return;
		Vec2Seq[] points = new Vec2Seq[tracers];
		for(int i = 0; i < tracers; i++){
			Vec2Seq p = new Vec2Seq();
			if(addBeginPoint)p.add(b.x, b.y);
			points[i] = p;
		}
		b.data = points;
	}
	
	@Override
	public void update(Bullet b){
		super.update(b);
		if(!Vars.headless && b.timer(2, tracerUpdateSpacing)){
			if(!(b.data instanceof Vec2Seq[]))return;
			Vec2Seq[] points = (Vec2Seq[])b.data();
			for(Vec2Seq seq : points){
				v2.trns(b.rotation(), 0, rand.range(tracerRandX));
				v1.setToRandomDirection(rand).scl(tracerSpacing);
				seq.add(v3.set(b.x, b.y).add(v1).add(v2));
			}
		}
	}
	
	@Override
	public void drawTrail(Bullet b){
		super.drawTrail(b);
		
		if((b.data instanceof Vec2Seq[])){
			Vec2Seq[] pointsArr = (Vec2Seq[])b.data();
			for(Vec2Seq points : pointsArr){
				if(points.size() < 2)return;
				Draw.color(hitColor);
				for(int i = 1; i < points.size(); i++){
//					Draw.alpha(((float)(i + fadeOffset) / points.size));
					Lines.stroke(Mathf.clamp((i + tracerFadeOffset / 2f) / points.size() * (tracerStrokeOffset - (points.size() - i)) / tracerStrokeOffset) * tracerStroke);
					Vec2 from = points.setVec2(i - 1, Tmp.v1);
					Vec2 to = points.setVec2(i, Tmp.v2);
					Lines.line(from.x, from.y, to.x, to.y, false);
					Fill.circle(from.x, from.y, Lines.getStroke() / 2);
				}
				
				Fill.circle(points.peekTmp().x, points.peekTmp().y, tracerStroke);
			}
		}
	}
}
