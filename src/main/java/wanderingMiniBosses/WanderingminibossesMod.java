package wanderingMiniBosses;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wanderingMiniBosses.monsters.banditking.BanditKing;
import wanderingMiniBosses.patches.MaybeSpawnDudePatch;
import wanderingMiniBosses.relics.*;
import wanderingMiniBosses.util.TextureLoader;
import wanderingMiniBosses.util.WanderingBossHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

@SpireInitializer
public class WanderingminibossesMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnStartBattleSubscriber,
        StartGameSubscriber,
        PostInitializeSubscriber,
        PostBattleSubscriber,
        PostUpdateSubscriber {
    public static final Logger logger = LogManager.getLogger(WanderingminibossesMod.class.getName());
    private static String modID;
    private static SpireConfig modConfig = null;

    public static Properties wanderingMiniBossesDefaultSettings = new Properties();

    private static final String MODNAME = "Wandering Minibosses";
    private static final String AUTHOR = "erasels, raz, Darkglade, Vex'd";
    private static final String DESCRIPTION = "Mod The Spire second anniversary mod. Adds wandering mini-bosses encountered multiple times over a run.";

    public static ArrayList<AbstractCard> inkedCardsList = new ArrayList<>();

    // =============== INPUT TEXTURE LOCATION =================
    public static final String BADGE_IMAGE = "wanderingMiniBossesResources/images/Badge.png";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeMonsterPath(String resourcePath) {
        return getModID() + "Resources/images/monsters/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public WanderingminibossesMod() {
        BaseMod.subscribe(this);

        setModID("wanderingMiniBosses");
        
        /*BaseMod.addColor(WanderingminibossesMod.Enums.COLOR_WB, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);*/

    }


    public static void setModID(String ID) {
        modID = ID;
    }

    public static String getModID() {
        return modID;
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        WanderingminibossesMod defaultmod = new WanderingminibossesMod();

        try {
            Properties defaults = new Properties();
            defaults.put("PermanentNemesis", Boolean.toString(false));
            modConfig = new SpireConfig("WanderingMinibosses", "Config", defaults);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean permaNemesis() {
        if (modConfig == null) {
            return false;
        }
        return modConfig.getBool("PermanentNemesis");
    }

    @Override
    public void receiveEditCharacters() {
        receiveEditPotions();
    }



    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(makeID("OptionsMenu"));
        String[] TEXT = UIStrings.TEXT;

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        int xPos = 350, yPos = 700;
        ModLabeledToggleButton PNBtn = new ModLabeledToggleButton(TEXT[0], xPos, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, permaNemesis(), settingsPanel, l -> { },
                button ->
                {
                    if (modConfig != null) {
                        modConfig.setBool("PermanentNemesis", button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(PNBtn); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        BaseMod.addSaveField("WBMonsterID", new CustomSavable<String>() {
            @Override
            public String onSave() {
                return WanderingBossHelper.getMonster() != null ? WanderingBossHelper.getMonster().id : "null";
            }

            @Override
            public void onLoad(String i) {
                WanderingBossHelper.setMonster(WanderingBossHelper.getMonsterFromID(i));
            }
        });

        BaseMod.addSaveField("WBMonsterHP", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return WanderingBossHelper.getMonster() != null ? WanderingBossHelper.getMonster().currentHealth : -1;
            }

            @Override
            public void onLoad(Integer i) {
                if (WanderingBossHelper.getMonster() != null) {
                    WanderingBossHelper.getMonster().currentHealth = i;
                }
            }
        });

        BaseMod.addSaveField("WBMonsterChance", new CustomSavable<Float>() {
            @Override
            public Float onSave() {
                return WanderingBossHelper.getSpawnChance();
            }

            @Override
            public void onLoad(Float i) {
                WanderingBossHelper.setSpawnChance(i);
            }
        });

        BaseMod.addSaveField("WBMonsterSpawn", new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return WanderingBossHelper.nemesisCheck();
            }

            @Override
            public void onLoad(Boolean i) {
                WanderingBossHelper.HAS_NEMESIS = i;
            }
        });

        BaseMod.addSaveField("NinjaRelicList", new CustomSavable<ArrayList<String>>() {
            @Override
            public ArrayList<String> onSave() {
                return BanditKing.relicList;
            }

            @Override
            public void onLoad(ArrayList<String> i) {
                BanditKing.relicList = i;
            }
        });

        BaseMod.addSaveField("NinjaStolenGold", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return BanditKing.myGold;
            }

            @Override
            public void onLoad(Integer i) {
                BanditKing.myGold = i;
            }
        });

        WanderingBossHelper.populateMonsterMap();
    }

    @Override
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            WanderingBossHelper.setMonster(WanderingBossHelper.getRandomMonster());
            WanderingBossHelper.resetSpawnChance();
            BanditKing.myGold = 0;
            BanditKing.relicList.clear();
            WanderingBossHelper.nemesisDetermination();
        }
    }

    public void receiveEditPotions() {

    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new CarrionFlame(), RelicType.SHARED);
        BaseMod.addRelic(new Blackblade(), RelicType.SHARED);
        BaseMod.addRelic(new AbyssPearl(), RelicType.SHARED);
        BaseMod.addRelic(new OtherGremlinHorn(), RelicType.SHARED);
        BaseMod.addRelic(new Inkheart(), RelicType.SHARED);
        BaseMod.addRelic(new ThiefScarf(), RelicType.SHARED);
        BaseMod.addRelic(new MasterThiefsPresence(), RelicType.SHARED);
        BaseMod.addRelic(new LockLocket(), RelicType.SHARED);
    }

    @Override
    public void receiveEditCards() {

    }

    @Override
    public void receiveEditStrings() {
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Relic-Strings.json");

        // BlightStrings
        BaseMod.loadCustomStringsFile(BlightStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Blight-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Potion-Strings.json");

        // MonsterStrings
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Monster-Strings.json");

        //UI Strings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-UI-Strings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/WanderingminibossesMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if(WanderingBossHelper.nemesisCheck()) {
            MaybeSpawnDudePatch.noEncounterThisFight();
            if (WanderingBossHelper.isMonsterAlive() && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
                if (Settings.isDebug || AbstractDungeon.monsterRng.randomBoolean(WanderingBossHelper.getSpawnChance())) {
                    MaybeSpawnDudePatch.resetTurnCounter();
                }
            }
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        inkedCardsList.clear();
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.player != null) {
            if (WanderingBossHelper.getMonster().name.equals(BanditKing.ID) && WanderingBossHelper.getMonster().currentHealth < 1 && AbstractDungeon.player.hasRelic(ThiefScarf.ID))
                ThiefScarf.wjhatefhefjeujf();
        }
    }
}
