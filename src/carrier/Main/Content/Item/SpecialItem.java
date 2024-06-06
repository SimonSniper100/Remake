package carrier.Main.Content.Item;

import arc.graphics.Color;
import carrier.Main.Content.Special.ModStat;
import mindustry.type.Item;

public class SpecialItem extends Item {
    public float armorScalingPoint;
    public boolean damageBounsForUnit,damageBounsForBlock;
    public float damageBouns;
    public SpecialItem(String name){
        super(name,new Color(Color.black));
    }
    public SpecialItem(String name, Color color) {
        super(name, color);
    }
    @Override
    public void setStats(){
        super.setStats();
        if(armorScalingPoint >0)stats.add(ModStat.armorBonus, armorScalingPoint);
        if(damageBouns>0){
            stats.add(ModStat.DamageBonusForUnit, damageBounsForUnit);
            stats.add(ModStat.DamageBonusForBlock, damageBounsForBlock);
            stats.addPercent(ModStat.damageBouns, damageBouns/100f);
        }
    }
}
