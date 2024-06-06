package carrier.Main.Content.Block.Type;

import arc.Core;
import arc.math.Mathf;
import arc.struct.ObjectSet;
import carrier.Main.CarrierVars;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.ItemTurret;

public class PlanetaryWeaponTurret extends ItemTurret {
    public int maxPlace,realSize;
    public int CantPlaceRange;
    public PlanetaryWeaponTurret(String name) {
        super(name);
        maxPlace = 2;
        CantPlaceRange = 80;
        update = true;
        size = 56;
    }
    public static void clear(){
		for(ObjectSet<PlanetaryWeaponTurretBulid> set : CarrierVars.placedPlanetaryWeapons){
			set.clear();
		}
	}
    public int maxPlaceNum(Team team){
        return (team == Vars.state.rules.waveTeam && !Vars.state.rules.pvp) || team.rules().cheat ? Integer.MAX_VALUE : Mathf.clamp(Vars.world.width() * Vars.world.height() / 10000, 1, maxPlace);
    }
    public void drawPlace(int x, int y, int rotation, boolean valid) {
		if(maxPlaceNum(Vars.player.team()) <= CarrierVars.placedPlanetaryWeapons[Vars.player.team().id].size){
			drawPlaceText("Maximum Placement Weapons Quantity Reached", x, y, false);
		}
	}
    @Override
	public boolean isAccessible(){
		return true;
	}
    @Override
	public boolean canPlaceOn(Tile tile, Team team, int rotation){
        return super.canPlaceOn(tile, team, rotation)&& CarrierVars.placedPlanetaryWeapons[team.id].size < maxPlaceNum(team);
    }
    public class PlanetaryWeaponTurretBulid extends ItemTurretBuild {
        @Override
		public void onRemoved(){
			super.onRemoved();
			CarrierVars.placedPlanetaryWeapons[team.id].remove(this);
		}
        @Override
		public void created(){
            super.created();
			Core.app.post(() -> {
				CarrierVars.placedPlanetaryWeapons[team.id].add(this);
			});
        }
        //Maybe To Big
        @Override
		public boolean canPickup(){
			return false;
		}
    }
}
