package carrier.Main.Content.Type_and_Entity.Carrier;

import arc.struct.Seq;
import carrier.Annotations.CallSuper;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class CarrierType extends TransformType{
    public Seq<Float> postion = new Seq<>();
    public int unitsSpawned = 4;
    public UnitType UnitSpawn;
    public float SpawnTime,DefendsTime=7*60;
    public float spawnX=0f,spawnY=0f;
    public CarrierType(String name) {
        super(name);
        constructor=CarrierEntity::new;
        omniMovement = false;
    }
    @CallSuper
    @Override
    public void init(){
        super.init();
        Unit example = constructor.get();
        if(example instanceof CarrierEntity c){
            c.postion=postion;
            c.SpawnTime=SpawnTime;
            c.DefendsTime=DefendsTime;
            c.unitsSpawned=unitsSpawned;
            c.droneType=UnitSpawn;
        }
    }
}