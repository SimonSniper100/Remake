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
    @Override
    public boolean serialize(){
        return false;
    }
    @Override
    public void update(){
        //ko có lí do gì để nó chết cả 
        //cho dù có chết thì nó củng spawn cái thứ 2
        super.update();
        if(health <= 10){
            dead =false;
            health = maxHealth;
        }
    }
}
