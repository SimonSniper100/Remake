package carrier.Main;

import arc.ApplicationListener;
import arc.Core;
import arc.struct.Seq;
import arc.util.Time;
import carrier.Main.UI.Fragment.PictureFragment;
import mindustry.Vars;

public class ModCore implements ApplicationListener {
    Seq<String> img = new Seq<>();
    float timer = 0f;
    PictureFragment pf = new PictureFragment(
        img.addAll("carrier-mod-pic-1","carrier-mod-pic-3","carrier-mod-pic-4")
        ,false); ;
    @Override
	public void update(){
        if(Vars.state.isMenu() || Vars.state.isPaused()){
            if((timer +=Time.delta)> 45f){
                pf.build(Core.scene.root);
                Vars.ui.loadfrag = pf;
                timer = 0f;
            }
        }
    }
}
