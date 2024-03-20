package carrier.Main.Content.Special;

import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import carrier.Main.Content.Type_and_Entity.Carrier.CarrierEntity;
import carrier.Main.Content.Type_and_Entity.Drone.DroneEntity;
import carrier.Main.Content.Type_and_Entity.Supporter.SupporterEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformEntity;
import carrier.Main.Content.Type_and_Entity.Transformer.TransformFlying;
import mindustry.Vars;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;

public class EntityRegister {
    public static final ObjectMap<Class<?>, ProvSet> needIdClasses = new ObjectMap<>();
	private static final ObjectMap<Class<?>, Integer> classIdMap = new ObjectMap<>();
    static{
		EntityRegister.put(CarrierEntity.class,()->new CarrierEntity());
		EntityRegister.put(DroneEntity.class,()->new DroneEntity());
		EntityRegister.put(SupporterEntity.class,()->new SupporterEntity());
		EntityRegister.put(TransformEntity.class,()->new TransformEntity());
		EntityRegister.put(TransformFlying.class,()->new TransformFlying());
    }
    
    public static <T extends Entityc> void put(Class<T> c, ProvSet p){
		needIdClasses.put(c, p);
	}
	public static <T extends Entityc> void put(Class<T> c, Prov<T> prov){
		put(c, new ProvSet(prov));
	}
    public static <T extends Entityc> int getID(Class<T> c){return classIdMap.get(c);}
	public static void load(){
		Seq<Class<?>> key = needIdClasses.keys().toSeq().sortComparing(c -> c.toString().hashCode());
		
		for(Class<?> c : key){
			classIdMap.put(c, EntityMapping.register(c.toString(), needIdClasses.get(c).prov));
		}if(Vars.headless){
			classIdMap.each((c, i) -> Log.info(i + "|" + c.getSimpleName()));
		}
	}
    public static class ProvSet{
		public final String name;
		public final Prov<?> prov;
		
		public ProvSet(String name, Prov<?> prov){
			this.name = name;
			this.prov = prov;
		}
		
		public ProvSet(Prov<?> prov){
			this.name = prov.get().getClass().toString();
			this.prov = prov;
		}
	}
}
