package carrier.Main.Content.Ability;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.entities.Effect;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.meta.StatValues;

public class PartSpawn extends Skill{
    public boolean runEffect,transformType,isTransform;
    public Unit unitpart;
    public UnitType ut;
    public float x,y,progress;
    public Color color;
    
    public PartSpawn(UnitType ut,float x,float y, boolean display){
        this.ut=ut;
        this.x=x;
        this.y=y;
        this.display = display;
        color = Color.white;
    }
    @Override
    public void update(Unit u){
        //Kiểm tra có phải là unit có thể biến đổi và nó là 1 phần part ?
        if(u instanceof TransformEntity t && ut instanceof PartType){
            transformType = true;
            if(t.TransformNow)isTransform = true;
            else isTransform = false;
        }
        else if (u instanceof TransformFlying f && ut instanceof PartType){
            transformType = true;
            if(f.TransformNow)isTransform = true;
            else isTransform = false;
        }
        else{
            transformType = false;
            isTransform = false;
        }
        if(transformType && isTransform){
            float 
            xp=u.x + Angles.trnsx(u.rotation,y,x),
            yp=u.y + Angles.trnsy(u.rotation,y,x);
            //Nếu null thì spawn con part mới
            if(unitpart == null || unitpart.dead || !unitpart.isValid()){
                unitpart = ut.create(u.team);
                unitpart.set(xp,yp);
                unitpart.rotation = u.rotation;
                unitpart.add(); 
                Events.fire(new UnitCreateEvent(unitpart,null,u));
                Effect spawnPartEffect = new Effect(60, e->{
                    Draw.color(color);
                    Draw.alpha(e.foutpow());
                    Draw.mixcol(color,e.finpow());
                    Draw.rect(unitpart.type.fullIcon,e.x,e.y,e.rotation-90);
                });
                if(runEffect){
                    spawnPartEffect.at(u,true);
                    runEffect=false;
                }
            }
            //còn có rồi thì kiểm tra ai
            if(unitpart != null && unitpart.controller() instanceof PartUnitAI ai){
                ai.Transformer = u;
                ai.x = x;
                ai.y = y;
                unitpart.team = u.team;
            }
        }
        else {
            runEffect = true;
        }
    }
    @Override
    public void addStats(Table a){
        if(!display)return;
        var whiteui = (TextureRegionDrawable)Tex.whiteui;
        super.addStats(a);
        a.add("[#ffd37f]Infomation:[white] ");
        a.row();
        a.table(whiteui.tint(Color.valueOf("4c4c4c").cpy()),t->{
            StatValues.weapons(ut, ut.weapons).display(t);
        });
        a.row();
        a.add("[#ffd37f]Spawn Count:"+SpawnCount);
    }
    @Override
    public String localized(){
        return Core.bundle.format("partspawn.unitspawn", ut.localizedName);
    }
}
