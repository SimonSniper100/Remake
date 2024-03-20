package carrier.Main.Content.Type_and_Entity.Transformer;

import arc.struct.Seq;
import carrier.Main.Content.Item.SpecialItem;
import carrier.Main.Content.Type_and_Entity.Other.CopyUnitType;
import carrier.Main.Content.unit.CarrierUnit;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

public class TransformType extends CopyUnitType{
    public float maxRotation,rotateMove,velocity;
    public StatusEffect BuffStatus;
    public int mainWeaponIdx = -1;
    public boolean ImmuneAll;
    public Seq<StatusEffect> constantEffect = new Seq<>();
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
        
    }
    @Override
    public void load(){
        super.load();
    }
}
