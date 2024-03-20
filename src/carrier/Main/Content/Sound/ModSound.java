package carrier.Main.Content.Sound;

import arc.audio.Sound;
import mindustry.Vars;

public class ModSound {
    public static Sound 
        ArpShoot=new Sound(),
        hugeBlasts = new Sound(),
        synchro = new Sound(),
        blaster = new Sound();
    public static Sound LoadSound(String name){
        return Vars.tree.loadSound(name);
    }
    public static void loadSounds(){
        ArpShoot= LoadSound("ArpShootSound");
        blaster = LoadSound("Blaster");
        hugeBlasts = LoadSound("HugeBlasts");
        synchro = LoadSound("synchro");
    } 
}
