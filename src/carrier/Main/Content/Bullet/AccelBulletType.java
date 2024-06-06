package carrier.Main.Content.Bullet;

import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.FloatSeq;
import mindustry.gen.Bullet;

public class AccelBulletType extends DataBulletType{
	public float velocityBegin = -1;
	public float velocityIncrease = 0;
	public float accelerateBegin = 0.1f;
	public float accelerateEnd = 0.6f;
	
	public Interp accelInterp = Interp.linear;
	
	public void disableAccel(){
		accelerateBegin = speed;
		velocityBegin = speed;
		velocityIncrease = 0;
	}
	
	public AccelBulletType(){
		super();
	}
	
	public AccelBulletType(float velocityBegin, float velocityIncrease, Interp accelInterp, float damage, String bulletSprite){
		super(damage,1,bulletSprite);
		this.velocityBegin = velocityBegin;
		this.velocityIncrease = velocityIncrease;
		this.accelInterp = accelInterp;
	}
	
	public AccelBulletType(float speed, float damage, String bulletSprite) {
		super(damage,1,bulletSprite);
	}
	
	public AccelBulletType(float speed, float damage) {
		this(speed, damage, "bullet");
	}
	
	public AccelBulletType(float damage, String bulletSprite){
		super(damage,1,bulletSprite);
	}
	
	@Override
	public float calculateRange(){
		if(velocityBegin < 0)velocityBegin = speed;
		
		boolean computeRange = rangeOverride < 0;
		float cal = 0;
		
		FloatSeq speeds = new FloatSeq();
		for(float i = 0; i <= 1; i += 0.04f){
			float s = velocityBegin + accelInterp.apply(Mathf.curve(i, accelerateBegin, accelerateEnd));
			speeds.add(s);
			if(computeRange)cal += s * lifetime * 0.04f;
		}
		speed = speeds.sum() / speeds.size;
		
		if(computeRange)cal += 1;
		
		return rangeOverride >0 ? rangeOverride : cal;
	}
	
	@Override	
	public void init(){
		super.init();
	}
	
	@Override
	public void update(Bullet b){
		if(accelerateBegin < 1)b.vel.setLength((velocityBegin + accelInterp.apply(Mathf.curve(b.fin(), accelerateBegin, accelerateEnd)) * velocityIncrease) * (drag != 0 ? (1 * Mathf.pow(b.drag, b.fin() * b.lifetime() / 6)) : 1));
		super.update(b);
	}
	public float calculateVelocity(float distances,float maxRange){
        //Calulate Alpha first, they should be aim Corret
        float a = Mathf.clamp(distances/maxRange);
        float velocity = velocityBegin + accelInterp.apply(Mathf.curve(a, accelerateBegin, accelerateEnd))*velocityIncrease;
        return velocity;
    }
}
