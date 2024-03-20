package carrier.Main.Content.Special;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.entities.Effect;

public class SeqMutliEffect extends Effect{
    Seq<Effect> effects = new Seq<>();

    public SeqMutliEffect(){
    }

    public SeqMutliEffect(Seq<Effect> effects){
        this.effects = effects;
    }

    @Override
    public void create(float x, float y, float rotation, Color color, Object data){
        if(!shouldCreate()) return;

        for(var effect : effects){
            effect.create(x, y, rotation, color, data);
        }
    }
}
