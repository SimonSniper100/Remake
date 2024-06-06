package carrier.Main.Content.Ability;

import arc.scene.ui.layout.Table;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class AbilityConvertor extends Skill{
    public Ability ability;
    @Override
    public  void init(UnitType t){
        ability.init(t);
    }
    @Override
    public void update(Unit u){
        ability.update(u);
    }
    @Override
    public void draw(Unit u){
        ability.draw(u);
    }
    @Override
    public String localized(){
        return ability.localized();
    }
    @Override
    public void addStats(Table t){
        ability.addStats(t);
    }
    @Override
    public void displayBars(Unit unit, Table bars){
        ability.displayBars(unit, bars);
    }
}
