package carrier.Main.Content.Type_and_Entity.Battleship;

import carrier.Main.Content.Type_and_Entity.Transformer.TransformType;

public class BattleshipType extends TransformType{
    public BattleshipType(String name) {
        super(name);
        ImmuneAll = true;
        constructor = ()->new BattleshipTransformer(){{
        }};
    }
    @Override
    public void drawSoftShadow(float x, float y, float rotation, float alpha){
        
    }
}
