package carrier.Main.UI.Fragment;

import arc.Core;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas.AtlasRegion;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import carrier.Main.UI.Element.DownSideBar;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.ui.fragments.LoadingFragment;

public class PictureFragment extends LoadingFragment {
    public boolean mobile;
    private Table table,tempTable,t2,t3;
    private TextButton button ;
    private Bar bar;
    public AtlasRegion image;
    private Label nameLabel ;
    private float progValue,d;
    public Seq<String> img=new Seq<>();
    public PictureFragment(Seq<String> img){
        this.img = img;
        this.mobile= false;
    }
    public PictureFragment(Seq<String> img,boolean mobile){
        this.img = img;
        this.mobile = mobile;
    }
    @Override
    public void build(Group patens){
        patens.fill(t->{
            t.clear();
            t.rect((x, y, w, h)->{
                Draw.alpha(t.color.a);
                Styles.black8.draw(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
            });
            t.visible = false;
            t.touchable = Touchable.enabled;
            t.fill(e->{
                e.clear();
                e.setZIndex(150);
                e.bottom();
                e.add(new DownSideBar()).growX().height(180);
                e.toFront();
                e.fill(s->{
                    s.clear();
                    s.toFront();
                    s.bottom().right().moveBy(-60,30);
                    nameLabel = s.add("@loading:"+progValue +"%").style(Styles.techLabel).visible(true).get();
                    text("@loading:"+progValue +"%");
                    bar = s.add(new Bar()).visible(false).get();
                    button = s.button("@cancel", ()->{}).get();
                    s.row();
                    t3=s;
                });
                t2=e;
            });
            table = t;
            tempTable = table;
        });
    }
    public void text(String text){
        nameLabel.setText(text);
        CharSequence realText = nameLabel.getText();
        for(int i = 0; i < realText.length(); i++){
            if(Fonts.tech.getData().getGlyph(realText.charAt(i)) == null){
                nameLabel.setStyle(Styles.defaultLabel);
                return;
            }
        }
        nameLabel.setStyle(Styles.techLabel);
    }
    @Override
    public void setButton(Runnable listener){
        button.visible = true;
        button.getListeners().remove(button.getListeners().size - 1);
        button.clicked(listener);
    }
    @Override
    public void setText(String text){
        text(text);
        nameLabel.setColor(Pal.accent);
    }
    @Override
    public void show(){
        show("@loading");

    }
    @Override
    public void hide(){
        d = Actions.fadeOut(1f).getAlpha();
        table.touchable=tempTable.touchable=t3.touchable =t2.touchable = Touchable.disabled;
        table.clearActions();
        table.toFront();
        table.actions(Actions.fadeOut(1), Actions.visible(false));
        tempTable.clearActions();
        tempTable.actions(Actions.fadeOut(1), Actions.visible(false));
        t2.clearActions();
        t2.toFront();
        t2.actions(Actions.fadeOut(1), Actions.visible(false));
        t3.clearActions();
        t3.toFront();
        t3.actions(Actions.fadeOut(1), Actions.visible(false));
    }
    @Override
    public void show(String text){
        button.visible = false;
        nameLabel.setColor(Color.white);
        bar.visible = false;
        table.clearActions();
        table.touchable = Touchable.enabled;
        text(text);
        table.actions(Actions.fadeIn(1f), Actions.visible(false));
        table.visible = true;
        table.color.a = 1f;
        table.toFront();
        
        tempTable.clear();
        tempTable = table;
        tempTable.setZIndex(100);
        image = Core.atlas.find(!mobile ? img.random():img.random()+"-mobile");
        tempTable.image(image).align(Align.center).grow();
        
        t2.clearActions();
        t2.touchable = Touchable.enabled;
        text(text);
        t2.actions(Actions.fadeIn(1f), Actions.visible(false));
        t2.visible = true;
        t2.color.a = 1f;
        t2.toFront();
        
        t3.clearActions();
        t3.touchable = Touchable.enabled;
        text(text);
        t3.actions(Actions.fadeIn(1f), Actions.visible(false));
        t3.visible = true;
        t3.color.a = 1f;
        t3.toFront();
    }

    @Override
    public void toFront(){
        table.toFront();
    }
    @Override
    public void setProgress(Floatp progress){
        bar.reset(0f);
        bar.visible = true;
        bar.set(() -> ((int)(progress.get() * 100) + "%"), progress, Pal.accent);
    }
    @Override
    public void snapProgress(){
        bar.snap();
    }
    @Override
    public void setProgress(float progress){
        progValue = progress;
        if(!bar.visible){
            setProgress(() -> progValue);
        }
    }
}