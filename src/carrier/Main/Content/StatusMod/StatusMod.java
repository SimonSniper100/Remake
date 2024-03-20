package carrier.Main.Content.StatusMod;

import mindustry.type.StatusEffect;

public class StatusMod {
    public static StatusEffect NakisakaBuff,DisbenzireBuff,DissesanaBuff,Special;
    // Unit Buff,Will Hidden
    public static void loadStatus(){
        NakisakaBuff = new StatusEffect("NakisakaBuff"){{
            show = false;
            hideDetails = true;
            speedMultiplier = 1.1f;
            damageMultiplier = 1.5f;
            healthMultiplier = 3f;
            reloadMultiplier = 1.3f;
        }};
        DisbenzireBuff = new StatusEffect("DisbenzireBuff"){{
            show = false;
            hideDetails = true;
            speedMultiplier = 2f;
            damageMultiplier = 3f;
            healthMultiplier = 3f;
            reloadMultiplier = 2f;
        }};
        DissesanaBuff = new StatusEffect("DissesanaBuff"){{
            show = false;
            hideDetails = true;
            speedMultiplier = 3f;
            reloadMultiplier = 2.1f;
            damageMultiplier = 2.3f;
            healthMultiplier = 5f;
        }};
        Special = new StatusEffect("clear"){{
            show = false;
            hideDetails = true;
            healthMultiplier = 3f;
        }};
    }
}
