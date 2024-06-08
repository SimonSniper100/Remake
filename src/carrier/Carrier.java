package carrier;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import carrier.Main.CarrierInputListener;
import carrier.Main.Content.Block.ModBlock;
import carrier.Main.Content.Item.ModItem;
import carrier.Main.Content.Sound.ModSound;
import carrier.Main.Content.Special.EntityRegister;
import carrier.Main.Content.StatusMod.StatusMod;
import carrier.Main.Content.unit.BattleshipUnit;
import carrier.Main.Content.unit.CarrierUnit;
import carrier.Main.Content.unit.DroneUnit;
import carrier.Main.Content.unit.FlyingUnit;
import carrier.Main.Content.unit.LaserUnit;
import carrier.Main.Content.unit.SupporterUnit;
import carrier.Main.UI.Fragment.PictureFragment;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;

public class Carrier extends Mod{
    boolean pictureApply = false;
    public static String name(String add){
        return "carrier-mod" + "-" + add;
    }
    Seq<String> img = new Seq<>();
    public PictureFragment pf = new PictureFragment(
        img.addAll("carrier-mod-pic-1","carrier-mod-pic-3","carrier-mod-pic-4")
        ,false); ;
    //Maybe Use Later
    public Carrier(){
        CarrierInputListener.registerModBinding();
        Events.on(ClientLoadEvent.class,e->{
            Log.info("Oke");
        });
    }
    @Override
    public void init(){
        // Use For Debug
        if(pictureApply){
            pf.build(Core.scene.root);
            Vars.ui.loadfrag = pf;
        }
    }
    @Override
    public void loadContent(){
        /**
         * Many huh ?
         * Im just make that to easy manager
        */
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
        BattleshipUnit.loadUnit();
    }
}
