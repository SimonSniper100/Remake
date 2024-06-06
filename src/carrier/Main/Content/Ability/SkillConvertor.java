package carrier.Main.Content.Ability;

import arc.scene.ui.layout.Table;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class SkillConvertor extends Ability{
    public Skill skill;
    public SkillConvertor(Skill s){
        this.skill = s;
    }
    @Override
    public  void init(UnitType t){
        skill.init(t);
    }
    @Override
    public void update(Unit u){
        skill.update(u);
    }
    @Override
    public void draw(Unit u){
        skill.draw(u);
    }
    @Override
    public String localized(){
        return skill.localized();
    }
    @Override
    public void addStats(Table t){
        skill.addStats(t);
    }
    @Override
    public void displayBars(Unit unit, Table bars){
        skill.displayBars(unit, bars);
    }
}
