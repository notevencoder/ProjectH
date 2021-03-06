package Sprites.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Platformer;
import com.mygdx.game.Screens.PlatScreen;

public class Diamond extends Item {

    private float stateTimer;

    private Animation curAnimation;
    private Animation idleAnimation, hitAnimation;

    public Diamond(PlatScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        SetCategoryFilter(Platformer.ITEM_BIT);

        defineAnimations();
        curAnimation = idleAnimation;

        PlatScreen.updateQueue.addForever(this);
        PlatScreen.drawQueue.add(this, 3);

        setRegion((TextureRegion) idleAnimation.getKeyFrame(0));
        setBounds(bounds.x / Platformer.PPM, bounds.y / Platformer.PPM, bounds.width / Platformer.PPM, bounds.height / Platformer.PPM);
    }

    //Вызывается при взятии
    @Override
    public void onTake() {
        stateTimer = 0;
        curAnimation = hitAnimation;
        screen.getHud().increaseScore();
    }

    @Override
    public void update(float dt) {
        setRegion((TextureRegion) curAnimation.getKeyFrame(stateTimer, true));
        if (curAnimation == hitAnimation && curAnimation.isAnimationFinished(stateTimer) && !destroyed)
            destroy();
        stateTimer += dt;
    }


    private void defineAnimations() {
        stateTimer = 0;

        TextureRegion curRegion;
        Array<TextureRegion> frames = new Array<TextureRegion>();

        curRegion = atlas.findRegion("Big Diamond Idle (18x14)");
        for (int i = 0; i < 8; i++)
            frames.add(new TextureRegion(curRegion, i * 18, 0, 18, 14));
        idleAnimation = new Animation(0.1f, frames);
        frames.clear();

            curRegion = atlas.findRegion("Big Diamond Hit (18x14)");
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(curRegion, i * 18, 0, 18, 14));
            hitAnimation = new Animation(0.1f, frames);
            frames.clear();
    }
}
