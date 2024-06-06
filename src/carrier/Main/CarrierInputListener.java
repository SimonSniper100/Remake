package carrier.Main;

import java.lang.reflect.Field;

import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import carrier.Main.SomeThing.ModKeyBinds;
import static arc.Core.keybinds;

public class CarrierInputListener {
    @SuppressWarnings("unchecked")
    public static void registerModBinding(){
        try {
            Field definitionsField = KeyBinds.class.getDeclaredField("definitions");
			Field defaultCacheField = KeyBinds.class.getDeclaredField("defaultCache");
            definitionsField.setAccessible(true);
			defaultCacheField.setAccessible(true);
            KeyBinds.KeyBind[] definitionsKey = ModKeyBinds.values();
            KeyBinds.KeyBind[] definitions = (KeyBinds.KeyBind[])definitionsField.get(keybinds);
            Seq<KeyBinds.KeyBind> definitionSeq = new Seq<>(false, definitions.length + definitionsKey.length, KeyBinds.KeyBind.class);  
            definitionSeq.addAll(definitionsKey).addAll(definitions);
			definitionsField.set(keybinds, definitionSeq.toArray()); 
            ObjectMap<KeyBinds.KeyBind, ObjectMap<InputDevice.DeviceType, KeyBinds.Axis>> defaultCache = (ObjectMap<KeyBinds.KeyBind, ObjectMap<InputDevice.DeviceType, KeyBinds.Axis>>)defaultCacheField.get(keybinds);
            for(KeyBinds.KeyBind def : definitionsKey){
				defaultCache.put(def, new ObjectMap<>());
				for(InputDevice.DeviceType type : InputDevice.DeviceType.values()){
					defaultCache.get(def).put(type,
							def.defaultValue(type) instanceof KeyBinds.Axis ?
									(KeyBinds.Axis)def.defaultValue(type) : new KeyBinds.Axis((KeyCode)def.defaultValue(type)));
				}
			}
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
