package wanderingMiniBosses.patches.dunno;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import javassist.CtBehavior;
import wanderingMiniBosses.relics.Inkheart;


//DONE:
/*
    card's update method is called appropriately
    card's render method is called appropriately (RenderTopCard.java)
    card's position is updated when which card it is changes, and when RefreshHandLayout is called
    card's hover logic is updated when hover logic is updated


 */

public class UpdateAndTrackTopCard {
    private static final float RENDER_X = 120 * Settings.scale;
    private static final float RENDER_Y = 400 * Settings.scale;

    @SpirePatch(
            clz = CardGroup.class,
            method = SpirePatch.CLASS
    )
    public static class Fields
    {
        public static SpireField<AbstractCard> currentCard = new SpireField<>(()->null);
    }

    @SpirePatch(
            clz = DrawPilePanel.class,
            method = "updatePositions"
    )
    public static class Update
    {
        @SpirePostfixPatch
        public static void doTheUpdateThing(DrawPilePanel __instance)
        {
            if (!AbstractDungeon.isScreenUp && AbstractDungeon.player.hasRelic(Inkheart.ID))
            {
                if (!AbstractDungeon.player.drawPile.isEmpty())
                {
                    AbstractCard top = AbstractDungeon.player.drawPile.getTopCard();
                    AbstractCard last = Fields.currentCard.get(AbstractDungeon.player.drawPile);
                    if (!top.equals(last))
                    {
                        if (last != null && AbstractDungeon.player.drawPile.contains(last))
                        {
                            partialReset(last);
                            last.shrink();
                            AbstractDungeon.getCurrRoom().souls.onToDeck(last, false, true);
                        }
                        Fields.currentCard.set(AbstractDungeon.player.drawPile, top);

                        glowCheck(top);
                        setPosition(top);
                    }
                    AbstractDungeon.player.drawPile.getTopCard().update();
                }
                else
                {
                    Fields.currentCard.set(AbstractDungeon.player.drawPile, null);
                }
            }
            else
            {
                AbstractCard last = Fields.currentCard.get(AbstractDungeon.player.drawPile);
                if (last != null && AbstractDungeon.player.drawPile.contains(last))
                {
                    partialReset(last);
                    last.shrink();
                    AbstractDungeon.getCurrRoom().souls.onToDeck(last, false, true);
                }

                Fields.currentCard.set(AbstractDungeon.player.drawPile, null);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "applyPowers"
    )
    public static class ApplyPowers
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.drawPile);
            if (c != null)
                c.applyPowers();
        }
    }
    @SpirePatch(
            clz = CardGroup.class,
            method = "glowCheck"
    )
    public static class GlowCheck
    {
        @SpirePostfixPatch
        public static void apply(CardGroup __instance)
        {
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.drawPile);
            if (c != null)
            {
                glowCheck(c);
                c.triggerOnGlowCheck();
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "updateHoverLogic"
    )
    public static class UpdateHoverLogic
    {
        @SpirePostfixPatch
        public static void update(CardGroup __instance)
        {
            AbstractCard c = Fields.currentCard.get(AbstractDungeon.player.drawPile);
            if (c != null)
                c.updateHoverLogic();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "refreshHandLayout"
    )
    public static class RefreshLayout
    {
        @SpireInsertPatch(
                locator = HoverLocator.class
        )
        public static void onRefreshLayout(CardGroup __instance)
        {
            AbstractCard top = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.drawPile);
            if (top != null)
                setPosition(top);
        }

        private static class HoverLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hoveredCard");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "canUseAnyCard"
    )
    public static class MaybeYouCan
    {
        @SpirePostfixPatch
        public static boolean maybe(boolean __result, CardGroup __instance)
        {
            return __result || (!AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.drawPile.getTopCard().hasEnoughEnergy());
        }
    }


    public static void glowCheck(AbstractCard c)
    {
        if (c.canUse(AbstractDungeon.player, null) && !AbstractDungeon.isScreenUp) {
            c.beginGlowing();
        } else {
            c.stopGlowing();
        }
    }
    public static void partialReset(AbstractCard c)
    {
        c.block = c.baseBlock;
        c.isBlockModified = false;
        c.damage = c.baseDamage;
        c.isDamageModified = false;
        c.magicNumber = c.baseMagicNumber;
        c.isMagicNumberModified = false;

        c.stopGlowing();
    }
    public static void setPosition(AbstractCard c)
    {
        c.targetDrawScale = 0.5f;
        c.targetAngle = 0;
        c.target_x = RENDER_X;
        c.target_y = RENDER_Y;
    }
}
