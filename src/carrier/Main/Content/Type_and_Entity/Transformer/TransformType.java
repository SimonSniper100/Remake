package carrier.Main.Content.Type_and_Entity.Transformer;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import carrier.Main.CarrierVars;
import carrier.Main.Content.Ability.Skill;
import carrier.Main.Content.Item.SpecialItem;
import carrier.Main.Content.Type_and_Entity.Other.CopyUnitType;
import carrier.Main.Content.unit.CarrierUnit;
import mindustry.Vars;
import mindustry.ai.types.LogicAI;
import mindustry.content.Blocks;
import mindustry.entities.abilities.Ability;
import mindustry.entities.part.DrawPart;
import mindustry.gen.Iconc;
import mindustry.gen.Payloadc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;
import mindustry.ui.Bar;

public class TransformType extends CopyUnitType{
    public Seq<Skill> skills = new Seq<>();
    public float maxRotation,rotateMove,velocity;
    public StatusEffect BuffStatus;
    public boolean ImmuneAll,ImmuneMutliBulletAim;
    public Seq<StatusEffect> constantEffect = new Seq<>();
    public Seq<DrawPart> TransformPart = new Seq<>(DrawPart.class);
    public boolean ImmuneSuction,scaleArmorWithSpecialItem = true;
    public TransformType(String name){
        super(name);
    }
    @Override
    public void update(Unit u){
        super.update(u);
        float scale=1f;
        if(firstRequirements != null){
            for(var items:firstRequirements){
                if(items.item instanceof SpecialItem s&&scaleArmorWithSpecialItem){
                    scale +=s.armorScalingPoint*items.amount;
                }
            }
        }
        if(constantEffect != null&&constantEffect.size >0){
            for(var eff:constantEffect){
                u.apply(eff,2);
            }
        }
        u.armor(armor*scale);
        
        if(u instanceof TransformEntity e&&e.TransformNow){
            if(BuffStatus != null){
                u.apply(BuffStatus,2);
            }
        }
        else if(u instanceof TransformFlying f&&f.TransformNow){
            if(BuffStatus != null){
                u.apply(BuffStatus,2);
            }
        }
    }
    @Override
    public void init(){
        super.init();
        if(ImmuneAll){
            CarrierUnit.immunise(this);
        }
        if(skills.any()){
            for(var issue:skills){
                issue.init(this);
            }
        }
    }
    @Override
    public void load(){
        super.load();
        for(var Tparts:TransformPart){
            Tparts.load(name);
        }
    }
    @Override
    public void draw(Unit u){
        super.draw(u);
        boolean isPayload = !u.isAdded();
        if(!isPayload&&CarrierVars.isTransformType(u)){
            for(Skill s : u instanceof TransformEntity t ? t.skills : u instanceof TransformFlying f ?f.skills: new Skill[]{}){
                Draw.reset();
                s.draw(u);
            }
        }
    }
    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out){
        super.getRegionsToOutline(out);
        for(var Tparts:TransformPart){
            Tparts.getOutlines(out);
        }
    }
    @Override
    public void setStats(){
        super.setStats();
        if(skills.any()){
            stats.add(CarrierVars.SkillStat,CarrierVars.SkillsVaule(skills));
        }
    }
    @Override
    public void display(Unit unit, Table table){
        table.table(t -> {
            t.left();
            t.add(new Image(uiIcon)).size(Vars.iconMed).scaling(Scaling.fit);
            t.labelWrap(unit.isPlayer() ? unit.getPlayer().coloredName() + "\n[lightgray]" + localizedName : localizedName).left().width(190f).padLeft(5);
        }).growX().left();
        table.row();

        table.table(bars -> {
            bars.defaults().growX().height(20f).pad(4);

            //TODO overlay shields
            bars.add(new Bar("stat.health", Pal.health, unit::healthf).blink(Color.white));
            bars.row();

            if(Vars.state.rules.unitAmmo){
                bars.add(new Bar(ammoType.icon() + " " + Core.bundle.get("stat.ammo"), ammoType.barColor(), () -> unit.ammo / ammoCapacity));
                bars.row();
            }

            for(Ability ability : unit.abilities){
                ability.displayBars(unit, bars);
            }
            for(Skill skill : unit instanceof TransformEntity t ? t.skills : unit instanceof TransformFlying f ?f.skills: new Skill[]{}){
                skill.displayBars(unit, bars);
            }
            if(payloadCapacity > 0 && unit instanceof Payloadc payload){
                bars.add(new Bar("stat.payloadcapacity", Pal.items, () -> payload.payloadUsed() / unit.type().payloadCapacity));
                bars.row();

                var count = new float[]{-1};
                bars.table().update(t -> {
                    if(count[0] != payload.payloadUsed()){
                        payload.contentInfo(t, 8 * 2, 270);
                        count[0] = payload.payloadUsed();
                    }
                }).growX().left().height(0f).pad(0f);
            }
        }).growX();

        if(unit.controller() instanceof LogicAI ai){
            table.row();
            table.add(Blocks.microProcessor.emoji() + " " + Core.bundle.get("units.processorcontrol")).growX().wrap().left();
            if(ai.controller != null && (Core.settings.getBool("mouseposition") || Core.settings.getBool("position"))){
                table.row();
                table.add("[lightgray](" + ai.controller.tileX() + ", " + ai.controller.tileY() + ")").growX().wrap().left();
            }
            table.row();
            table.label(() -> Iconc.settings + " " + (long)unit.flag + "").color(Color.lightGray).growX().wrap().left();
            if(Vars.net.active() && ai.controller != null && ai.controller.lastAccessed != null){
                table.row();
                table.add(Core.bundle.format("lastaccessed", ai.controller.lastAccessed)).growX().wrap().left();
            }
        }else if(Vars.net.active() && unit.lastCommanded != null){
            table.row();
            table.add(Core.bundle.format("lastcommanded", unit.lastCommanded)).growX().wrap().left();
        }

        table.row();
    }
}
