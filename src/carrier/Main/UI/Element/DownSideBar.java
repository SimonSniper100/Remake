package carrier.Main.UI.Element;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.scene.Element;
import mindustry.graphics.Pal;

public class DownSideBar extends Element{
    public float a =1f;
    @Override
    public void draw(){
        Draw.color(Pal.accent.a(a));
        Fill.quad(x, y, x, y+height, x+width, y, x+width, y+height*1.3f);
        Draw.color(Color.black.a(a));
        Fill.quad(x, y, x, y+height*0.8f, x+width, y, x+width, y+height);
        Draw.reset();
    }
    @Override
    public void act(float d){
        super.act(d);
        
    }
}
