package carrier.Main.Content.Type_and_Entity.Part;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.UnitEntity;

public class PartEntity extends UnitEntity {
    @Override
    public void wobble(){
        x += Mathf.sin(Time.time + (float)(id() % 10 * 12), 25.0F, 0.001F) * Time.delta * elevation;
        y += Mathf.cos(Time.time + (float)(id() % 10 * 12), 25.0F, 0.001F) * Time.delta * elevation;
    }
}
