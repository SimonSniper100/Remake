package carrier.Main.Content.Special;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import carrier.Main.MathComplex;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.type.UnitType.UnitEngine;

public class PropellerEngine extends UnitEngine{
    private float time;
    public float x,y,z,zCycle,rotateSpeed,stroke,length,width,angle,strokeOffset=1;
    public int propeller;
    public Color downZColor,upperZColor;
    @Override
    public void draw(Unit u){
        for(int i = 0;i<propeller;i++){
            float cycle = Sincycle(i,u.vel.len(),propeller),scycle = Sincycle(i,u.vel.len(),propeller,angle),
            cosCycle = Coscycle(i,u.vel.len(),propeller);
            Lines.stroke(strokeOffset+stroke*Math.abs(cosCycle));
            Draw.color(downZColor,upperZColor,0.5f+0.5f*cosCycle);
            Draw.z((z+zCycle*cosCycle)*u.elevation);
            float
                xp=u.x+Angles.trnsx(u.rotation,y,x + width*cycle),
                yp=u.y+Angles.trnsy(u.rotation,y,x +width*cycle),
                xp1=u.x+Angles.trnsx(u.rotation,y-length,x +width*scycle*Mathf.cosDeg(angle)),
                yp1=u.y+Angles.trnsy(u.rotation,y-length,x +width*scycle*Mathf.cosDeg(angle));
            Lines.line(xp, yp, xp1, yp1);
        }
    }
    protected float Coscycle(int startAngle,float speed,int Max){
        return Coscycle(startAngle,speed,Max,0);
    }
    protected float Sincycle(int startAngle,float speed,int Max){
        return Sincycle(startAngle, speed, Max,0);
    }
    protected float Sincycle(int startAngle,float speed,int Max,float AnglesOffset){
        return Mathf.sin(((time += Time.delta*speed*(Vars.state.isPaused()?0:1)*rotateSpeed)+MathComplex.PolygoneAngle(startAngle,Max)+AnglesOffset)*Mathf.degreesToRadians);
    }
    protected float Coscycle(int startAngle,float speed,int Max,float AnglesOffset){
        return Mathf.cos(((time += Time.delta*speed*(Vars.state.isPaused()?0:1)*rotateSpeed)+MathComplex.PolygoneAngle(startAngle,Max)+AnglesOffset)*Mathf.degreesToRadians);
    }
}
