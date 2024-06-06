package carrier.Main.Content.Type_and_Entity.Sentry;

import mindustry.type.UnitType;

public class SentryType extends UnitType {
    public SentryType(String name) {
        super(name);
        speed = 0;
        targetable = false;
        drawMinimap = false;
        drawCell = false;
    }
}
