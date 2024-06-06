package carrier.Main.Content.Part.PartProgess;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Interval;
import arc.util.Time;
import mindustry.entities.Effect;
import mindustry.entities.part.DrawPart;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;

public class SmallBlackCore extends DrawPart{
    public float size;
    private float time,t;
    public Color color = Color.white;
    public Effect exEffect;
    public Vec2 v =  new Vec2();
    public Interval timer = new Interval(10);
    private PartProgress progressWarmup = PartProgress.warmup;
    @Override
    public void draw(PartParams params) {
        float p = progressWarmup.getClamp(params);
        Draw.z(Layer.effect);
        Draw.color(color);
        Lines.stroke(4*p);
        Lines.circle(params.x, params.y, (size+3.8f)*p);
        Fill.circle(params.x, params.y, p*(size/5f));
        float rot = time+=20*Time.delta/60,rot2 = t+=30*Time.delta/60;
        for(int i:Mathf.signs){
            v.trns(rot-90*i, (8+size)*p);
            Drawf.tri(params.x + v.x, params.y+v.y,p*(4+size/2f), p*(15+size), rot-90*i);
        }
        for(int i:Mathf.signs){
            v.trns(-rot2-90*i,(8+size)*p);
            Drawf.tri(params.x + v.x, params.y+v.y,p*(4+size/2f), p*(15+size), -rot2-90*i);
        }
        Drawf.light(new Position() {@Override public float getX() {return params.x;}@Override public float getY() {return params.y;}}, size*p, color, 4);
        if(exEffect != null&&timer(0, 0.5f*30f)&&p>0.9f){
            exEffect.at(params.x,params.y,params.rotation,color);
        }
    }
    @Override
    public void load(String arg0) {
    }
    public boolean timer(int index, float time) {
        return Float.isInfinite(time) ? false : this.timer.get(index, time);
     }
}
