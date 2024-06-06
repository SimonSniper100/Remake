package carrier.Main.Content.Weapons;

import mindustry.entities.units.WeaponMount;
import mindustry.gen.Unit;

public class SecondaryWeapon extends ConfigWeapons{
    public SecondaryWeapon(String name){
        super(name);
    }
    public SecondaryWeapon(){
        super("");
    }
    @Override
    public void update(Unit u , WeaponMount m){
        super.update(u, m);
        m.shoot = m.target != null;    
    }
}
