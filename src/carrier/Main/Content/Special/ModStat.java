package carrier.Main.Content.Special;

import mindustry.world.meta.Stat;

public class ModStat extends Stat{

    public ModStat(String name) {
        super(name);
    }
    public static final Stat armorBonus = new Stat("ArmorBonus");
    public static final Stat damageBouns = new Stat("DamageBonus");
    public static final Stat DamageBonusForUnit = new Stat("DamageBonusForUnit");
    public static final Stat DamageBonusForBlock = new Stat("DamageBonusForBlock");
}
