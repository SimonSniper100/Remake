package carrier.Main.Content.Item;

import arc.graphics.Color;
import mindustry.type.Item;

public class ModItem {
    public static Item QuatanzationCrystal,Diamond;
    public static void loadItem(){
        QuatanzationCrystal = new Item("Quatanzation-Crystal",Color.valueOf("f3baff")){{
            flammability = 0f;
            hardness=5;
            explosiveness = 0.3f;
            radioactivity = 1.2f;
            cost = 1.2f;
            healthScaling = 0.5f;
        }};
        Diamond = new SpecialItem("Diamonds",Color.white.cpy()){{
            flammability = 0f;
            hardness = 6;
            explosiveness = 0;
            radioactivity = 0;
            cost = 2f;
            armorScalingPoint = 0.2f;
            healthScaling = 2f;
            damageBouns = 12f;
            damageBounsForUnit =damageBounsForBlock= true;
        }};
    }
}