package carrier.Main.Content.Ability;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Effect.NDEffect;
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
    public float x;
    public float y;
    public Color color;
    public String nameSpirte;
    
    public PartSpawn(UnitType ut,float x,float y, boolean display){
        this.ut=ut;
        this.x=x;
        this.y=y;
        this.display = display;
        color = Color.white;
    }
    public PartSpawn(UnitType ut,float x,float y, boolean display,Color color){
        this.ut=ut;
        this.x=x;
        this.y=y;
        this.display = display;
        this.color = color;
    }
    @Override
    public void update(Unit u){
        //Kiểm tra có phải là unit có thể biến đổi và nó là 1 phần part ?
        if(u instanceof TransformEntity t && ut instanceof PartType){
            transformType = true;
            isTransform = t.TransformNow;
        }
        else if (u instanceof TransformFlying f && ut instanceof PartType){
            transformType = true;
            isTransform = f.TransformNow;
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
                Effect spawnPartEffect = NDEffect.TransformAppear(color,new Effect(),4*60f,nameSpirte,u).rotWithParent(true).followParent(true);
                if(runEffect){
                    spawnPartEffect.at(unitpart,true);
                    runEffect=false;
                    for(var w : unitpart.mounts()){
                        Effect ex = NDEffect.TransformWeaponsAppear(color,new Effect(),4*60f,w,unitpart).rotWithParent(true).followParent(true);
                        ex.at(unitpart,true);
                    }
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
