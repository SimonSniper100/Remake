package carrier.Main.Content.Ability;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.util.Interval;
import carrier.Main.Content.Effect.NDEffect;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class DrawEletricLines {
    public float x1,x2,y1,y2;
    public Interval timec = new Interval(10);
    public Color color=Color.white;
    public DrawEletricLines(float x1 , float y1,float x2,float y2,Color color){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        this.color = color;
    }
    public void draw(Unit u,Unit us){
        Draw.color(color);
        Draw.z(Layer.flyingUnitLow-1);
        float
        px1 = u.x+Angles.trnsx(u.rotation,y1,x1),
        py1 = u.y+Angles.trnsy(u.rotation,y1,x1);
        Position p1 = new Position() {
            @Override
            public float getX() {
                return px1;
            }
            @Override
            public float getY() {
                return py1;
            }
        };
        if(!isRip(us)){
            Fill.circle(px1, py1, 4);
            float 
            px2 = us.x+Angles.trnsx(u.rotation,y2,x2),
            py2 = us.y+Angles.trnsy(u.rotation,y2,x2);
            Position p2 = new Position() {
                @Override
                public float getX() {
                    return px2;
                }
                @Override
                public float getY() {
                    return py2;
                }
            };
            Fill.circle(px1, py1, 4);
            if(timer(1,20+Mathf.random(-5, 5))){
                NDEffect.createEffect(p1,p2,color,3,NDEffect.WIDTH,35+Mathf.random(-5, 5));
            }
        }
    }
    public boolean timer(int index, float time) {
        return !Float.isInfinite(time) && timec.get(index, time);
    }
    public boolean isRip(Unit u){
        return u==null||u.dead()||!u.isValid()||u.isNull();
    }
}
