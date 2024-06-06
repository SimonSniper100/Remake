package carrier.Main.Content.Ability;

import arc.graphics.Color;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class WingPartSpawn extends PartSpawn{
    public float alpha;
    public WingPartSpawn(UnitType ut, float x, float y, boolean display, Color color) {
        super(ut, x, y, display, color);
    }
    @Override
    public void draw(Unit u){
        super.draw(u);
    }
}
