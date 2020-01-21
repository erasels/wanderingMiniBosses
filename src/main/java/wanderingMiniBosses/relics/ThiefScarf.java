package wanderingMiniBosses.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import wanderingMiniBosses.WanderingminibossesMod;
import wanderingMiniBosses.util.TextureLoader;

import java.util.ArrayList;

import static wanderingMiniBosses.WanderingminibossesMod.makeRelicOutlinePath;
import static wanderingMiniBosses.WanderingminibossesMod.makeRelicPath;

public class ThiefScarf extends CustomRelic {

    public static final String ID = WanderingminibossesMod.makeID("ThiefScarf");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("ThiefScarf.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("ThiefScarf.png"));

    public ThiefScarf() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public static boolean bruh = false;
    public static boolean NO_MORE_FUN = false;

    public static AbstractRelic funkyDoe1 = null;
    public static AbstractRelic lowkeyBruh = null;

    public static void wjhatefhefjeujf() {
        if (bruh) {
            funkyDoe1.instantObtain();
            lowkeyBruh.instantObtain();
            bruh = false;
        }
        if (NO_MORE_FUN) {
            AbstractDungeon.player.loseRelic(funkyDoe1.relicId);
            AbstractDungeon.player.loseRelic(lowkeyBruh.relicId);
            NO_MORE_FUN = false;
        }
    }

    @Override
    public void atPreBattle() {
        this.flash();
        getRelic();
    }

    public static void getRelic() {
        ArrayList<AbstractRelic> bruhList = new ArrayList<>();
        for (String s : AbstractDungeon.commonRelicPool) {
            AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
            try {
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    bruhList.add(r);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (String s : AbstractDungeon.uncommonRelicPool) {
            AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
            try {
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    bruhList.add(r);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (String s : AbstractDungeon.rareRelicPool) {
            AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
            try {
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    bruhList.add(r);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (String s : AbstractDungeon.shopRelicPool) {
            AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
            try {
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    bruhList.add(r);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        for (String s : AbstractDungeon.bossRelicPool) {
            AbstractRelic r = RelicLibrary.getRelic(s).makeCopy();
            try {
                if (r.getClass().getMethod("onEquip").getDeclaringClass() == AbstractRelic.class && r.getClass().getMethod("onUnequip").getDeclaringClass() == AbstractRelic.class) {
                    bruhList.add(r);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        funkyDoe1 = bruhList.remove(AbstractDungeon.cardRandomRng.random(bruhList.size() - 1));
        lowkeyBruh = bruhList.remove(AbstractDungeon.cardRandomRng.random(bruhList.size() - 1));
        bruh = true;
    }

    @Override
    public void onVictory() {
        NO_MORE_FUN = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
