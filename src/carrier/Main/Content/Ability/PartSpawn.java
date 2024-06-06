package carrier.Main.Content.Ability;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Interp;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import carrier.Main.Content.AI.PartUnitAI;
import carrier.Main.Content.Effect.NDEffect;
import carrier.Main.Content.Type_and_Entity.Part.PartEntity;
import carrier.Main.Content.Type_and_Entity.Part.PartType;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.entities.Effect;
import mindustry.game.EventType.UnitCreateEvent;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.meta.StatValues;

public class PartSpawn extends PassiveSkill{
    private boolean runEffect,transformType,isTransform,isPartEntity;
    private float prog=0;
    public boolean DrawEletricLine= false;
    public Unit unitpart;
    public UnitType ut;
    public float x,moveX;
    public float y,moveY;
    public float bodyRotation,moveSpeed,rotationOffset,rotateSpeeds;
    public Interp InterpRotation = Interp.linear,InterpMoveX= Interp.linear,InterpMoveY= Interp.linear;
    public Color color;
    public String nameSpirte;
    /** Delay Time Fout MoveX, Delay Time Fout MoveY, Delay Fout TimeRotation, Start X, Start Y, Start Rotation */
    public float[] startArgument = new float[10];
    public Seq<DrawEletricLines> linesEletric = new Seq<>();
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
    public void init(UnitType uts){
        super.init(uts);
        SpawnCount =0;
        if(display){
        }
        
    }
    @Override
    public void update(Unit u){
        //Kiểm tra có phải là unit có thể biến đổi và nó là 1 phần part ?
        isPartEntity = u instanceof PartEntity;
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
        if((transformType && isTransform)||isPartEntity){
            float 
            xp=u.x + Angles.trnsx(u.rotation,y,x),
            yp=u.y + Angles.trnsy(u.rotation,y,x);
            //Nếu null thì spawn con part mới
            if(isRip(unitpart)){
                unitpart = ut.create(u.team);
                unitpart.set(xp,yp);
                unitpart.rotation = u.rotation+rotationOffset;
                unitpart.add(); 
                Events.fire(new UnitCreateEvent(unitpart,null,u));
                Effect spawnPartEffect = NDEffect.TransformAppear(color,new Effect(),4*60f,nameSpirte,u,rotationOffset).rotWithParent(true).followParent(true);
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
            if(!isRip(unitpart) && unitpart.controller() instanceof PartUnitAI ai){
                ai.startArgument = startArgument;
                ai.Transformer = u;
                ai.x = x;
                ai.y = y;
                ai.moveY=moveY;
                ai.moveX=moveX;
                ai.rotationOffset = rotationOffset;
                ai.InterpMoveX = InterpMoveX;
                ai.InterpMoveY = InterpMoveY;
                ai.InterpRotation = InterpRotation;
                ai.bodyRotation = bodyRotation;
                ai.progress = prog = shootFadeIn(moveSpeed, u,0);
                ai.rotProg = shootFadeIn(rotateSpeeds, u,1);
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
        a.add("[#ffd37f]"+Core.bundle.format("info.title")+": ");
        a.row();
        a.add(Core.bundle.format("partspawn.whenTransform"));
        a.row();
        a.add(ut.description);
        a.row();
        a.table(whiteui.tint(Color.valueOf("4c4c4c").cpy()),t->{
            StatValues.weapons(ut, ut.weapons).display(t);
        });
        a.row();
        a.add("[#ffd37f]"+Core.bundle.format("partspawn.SpawnCount")+": "+SpawnCount);
    }
    @Override
    public String localized(){
        return Core.bundle.format("partspawn.unitspawn");
    }
    @Override
    public void draw(Unit u){
        super.draw(u);
        if(!linesEletric.isEmpty()&&DrawEletricLine&&prog>=0.2){
            for(var issue: linesEletric){
                issue.draw(u,unitpart);
            }
        }
    }
}
