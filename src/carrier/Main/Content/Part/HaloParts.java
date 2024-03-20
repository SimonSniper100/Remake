package carrier.Main.Content.Part;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.entities.part.DrawPart;

public class HaloParts extends DrawPart{
    public float circleRadius;
    public Color color;
    public float time,z,y,x;
    public HaloParts(float circleRadius,Color color){
        this.circleRadius=circleRadius;
        this.color = color;
    }
    @Override
    public void draw(PartParams params) {
        float wp = PartProgress.warmup.get(params);
        Draw.color(color);
        Draw.z(z);
        float
            xp = params.x + Angles.trnsx(params.rotation, y,x),
            yp = params.y + Angles.trnsy(params.rotation, y,x);
        Lines.stroke(wp*0.9f);
        Lines.setCirclePrecision(0.4f);
        Lines.circleVertices(0.1f);
        Lines.circle(xp, yp, circleRadius*Mathf.PI/3);
        time+=Time.delta*0.1f;
        for(int i:Mathf.signs){
            Lines.square(xp, yp,circleRadius,time*15*i);
        }
        
    }

    @Override
    public void load(String name) {
    }
}
