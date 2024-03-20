package carrier.Main.Content.Special;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import carrier.Main.Content.AI.PartUnitAI;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class ThursterEngine extends UnitType.UnitEngine{
    public float lenght,z;
    public boolean TeamColor;
    private float t;
    public Color color = Color.white;
    public Unit us,sub;
    public ThursterEngine(float x,float y,float radius,float rotation,float lenght,boolean TeamColor){
        super(x, y, radius, rotation);
        this.lenght= lenght;
    }
    @Override
    public void draw(Unit u){
        if(u.controller() instanceof PartUnitAI p){
            us = p.Transformer;
        }
        if(u.vel.len2()>=0.000001f){
            if(t >=360)t=0f;
            sub = us != null && !us.dead &&u.isValid() ? us: u; 
            Draw.blend(Blending.additive);
            float z1 = Draw.z();
            if(z>0)Draw.z();
            float str = Mathf.clamp(sub.vel.len(), 0, sub.speed())*Mathf.log(10,Mathf.sqr(2*sub.speedMultiplier+1));
            float b = str <= 0.9f ? 0: Mathf.sin(t+=Time.delta*0.1f*(Vars.state.isPaused() ? 0:1)*Mathf.random(1f, (float)Math.sqrt(Math.abs(sub.speedMultiplier))));
            Draw.color(TeamColor ? u.team.color:color);
            float 
                dx1= u.x +Angles.trnsx(u.rotation, y,x+radius/2f),
                dx2= u.x +Angles.trnsx(u.rotation, y,x-radius/2f),
                dx3= u.x +Angles.trnsx(u.rotation, y-lenght*(str < 0 ?0:str)+1.5f*b,x+radius/2f),
                dx4= u.x +Angles.trnsx(u.rotation, y-lenght*(str < 0 ?0:str)+1.5f*b,x-radius/2f);
            float 
                dy1= u.y +Angles.trnsy(u.rotation, y,x+radius/2f),
                dy2= u.y +Angles.trnsy(u.rotation, y,x-radius/2f),
                dy3= u.y +Angles.trnsy(u.rotation, y-lenght*(str < 0 ?0:str)+1.5f*b,x+radius/2f),
                dy4= u.y +Angles.trnsy(u.rotation, y-lenght*(str < 0 ?0:str)+1.5f*b,x-radius/2f);
            Fill.quad(dx1, dy1, color.cpy().toFloatBits(), dx2, dy2, color.cpy().toFloatBits(), dx4, dy4, color.cpy().a(0).toFloatBits(), dx3, dy3, color.cpy().a(0).toFloatBits());
            Draw.z(z1);
            Draw.blend();
        }
    }
}
