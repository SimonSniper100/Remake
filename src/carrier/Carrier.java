package carrier;

import arc.Core;
import arc.Events;
import arc.util.Log;
import carrier.Main.ModCore;
import carrier.Main.Content.Block.ModBlock;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.Special.EntityRegister;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.unit.CarrierUnit;
import carrier.Main.Content.unit.DroneUnit;
import carrier.Main.Content.unit.FlyingUnit;
import carrier.Main.Content.unit.LaserUnit;
import carrier.Main.Content.unit.SupporterUnit;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;

public class Carrier extends Mod{
    
    public static String name(String add){
        return "carrier-mod" + "-" + add;
    }
    public Carrier(){
        Events.on(ClientLoadEvent.class,e->{
            Log.info("Oke");
        });
    }
    @Override
    public void init(){ 
       Core.app.addListener(new ModCore());
    }
    @Override
    public void loadContent(){
        ModSound.loadSounds();
        ModItem.loadItem();
        StatusMod.loadStatus();
        EntityRegister.load();
        FlyingUnit.loadUnit();
        CarrierUnit.loadPartUnit();
        ModBlock.loadWall();
        ModBlock.loadTurret();
        LaserUnit.loadDrone();
        DroneUnit.loadDrone();
        SupporterUnit.loadSupporter();
        CarrierUnit.loadUnit();
    }
}
