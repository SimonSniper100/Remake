package carrier.Main.SomeThing;

import arc.Core;
import arc.KeyBinds;
import arc.KeyBinds.KeybindValue;
import arc.input.KeyCode;
import arc.input.InputDevice.DeviceType;

public enum ModKeyBinds implements KeyBinds.KeyBind{
    Transform(KeyCode.n),
    UnTransform(KeyCode.b),
    Skill1(KeyCode.j),
    Skill2(KeyCode.numpad2),
    Skill3(KeyCode.numpad3),
    Skill4(KeyCode.numpad4),
    Skill5(KeyCode.numpad5),
    Skill6(KeyCode.numpad6);

    private final KeyBinds.KeybindValue defaultValue;
    private final String category;

    ModKeyBinds(KeyBinds.KeybindValue defaultValue,String categoty){
        this.defaultValue=defaultValue;
        this.category = categoty;
    }
    ModKeyBinds(KeyBinds.KeybindValue defaultValue){
		this(defaultValue, "carrier-mod");
	}
    @Override
    public KeybindValue defaultValue(DeviceType type) {
        return defaultValue;
    }
    @Override
	public String category(){
		return category;
	}
    public boolean IsPressTransforming(){
        return Core.input.keyTap(Transform);
    }
}
