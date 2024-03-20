package carrier.Main.Content.Ability;

import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;

public class Skill extends Ability{
    private float results,lstResults;
    public float rotate;
    public String message="";
    public int SpawnCount=1;
    //TODO need check delta function
    public float Diffenals(float after){
        lstResults = results;
        results = after;
        return (after-lstResults)/Time.delta;
    }
    @Override
    public void update(Unit u){
    }
    @Override
    public void addStats(Table t){
        t.add(message);
        t.row();
        
    }
}
