package carrier.Main.Content.Ability;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import mindustry.entities.Effect;
import mindustry.gen.Unit;

public class DrawPartOnly extends Skill{
    public boolean runEffect;
    public String partName;
    public float x,y,z;
    public Effect spawnPartEffect;
    public Color color;
    public DrawPartOnly(String partNames){
        this.partName = "carrier-mod"+partNames;
        display = false;
        color = Color.white;
    }
    @Override
    public void draw(Unit u){
        if(u instanceof TransformEntity c){
            if(c.TransformNow){
                Draw.z(z);
                Draw.color(color);
                var region = Core.atlas.find(partName);
                Draw.rect(region,u.x+Angles.trnsx(u.rotation,y,x),u.y+Angles.trnsy(u.rotation,y,x),u.rotation-90);
                Draw.reset();
                spawnPartEffect = new Effect(30, e->{
                    Draw.color(color);
                    Draw.alpha(e.foutpow());
                    Draw.mixcol(color,e.finpow());
                    Draw.rect(region,u.x+Angles.trnsx(u.rotation,y,x),u.y+Angles.trnsy(u.rotation,y,x),u.rotation-90);
                });
                if(runEffect){
                    spawnPartEffect.at(u.x+Angles.trnsx(u.rotation,y,x),u.y+Angles.trnsy(u.rotation,y,x),u.rotation);
                    runEffect = false;
                }
            }
            else{
                runEffect = true;
            }
        }
    }
    
}
