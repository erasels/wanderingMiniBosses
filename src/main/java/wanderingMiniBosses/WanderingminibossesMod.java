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
import wanderingMiniBosses.patches.MaybeSpawnDudePatch;
import wanderingMiniBosses.relics.Blackblade;
import wanderingMiniBosses.relics.CarrionFlame;
import wanderingMiniBosses.relics.Inkheart;
import wanderingMiniBosses.util.TextureLoader;
import wanderingMiniBosses.util.WanderingBossHelper;

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
        PostBattleSubscriber {
    public static final Logger logger = LogManager.getLogger(WanderingminibossesMod.class.getName());
    private static String modID;

    public static Properties wanderingMiniBossesDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

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
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
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

        wanderingMiniBossesDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("wanderingMiniBossesMod", "wanderingMiniBossesConfig", wanderingMiniBossesDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        receiveEditPotions();
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("wanderingMiniBossesMod", "wanderingMiniBossesConfig", wanderingMiniBossesDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        BaseMod.addSaveField("WBMonsterID", new CustomSavable<String>() {
            @Override
            public String onSave() {
                return WanderingBossHelper.getMonster() != null?WanderingBossHelper.getMonster().id:"null";
            }

            @Override
            public void onLoad(String i) {
                WanderingBossHelper.setMonster(WanderingBossHelper.getMonsterFromID(i));
            }
        });

        BaseMod.addSaveField("WBMonsterHP", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return WanderingBossHelper.getMonster() != null?WanderingBossHelper.getMonster().currentHealth:-1;
            }

            @Override
            public void onLoad(Integer i) {
                if(WanderingBossHelper.getMonster() != null) {
                    WanderingBossHelper.getMonster().currentHealth = i;
                }
            }
        });

        WanderingBossHelper.populateMonsterMap();
    }

    @Override
    public void receiveStartGame() {
        if(!CardCrawlGame.loadingSave) {
            WanderingBossHelper.setMonster(WanderingBossHelper.getRandomMonster());
        }
    }
    
    public void receiveEditPotions() {

    }
    
    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new CarrionFlame(), RelicType.SHARED);
        BaseMod.addRelic(new Blackblade(), RelicType.SHARED);

        BaseMod.addRelic(new Inkheart(), RelicType.SHARED);
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

    private static final float CHANCE = 0.15F;

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        MaybeSpawnDudePatch.noEncounterThisFight();
        if (WanderingBossHelper.isMonsterAlive() && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
            if (Settings.isDebug || AbstractDungeon.monsterRng.randomBoolean(CHANCE)) {
                MaybeSpawnDudePatch.resetTurnCounter();
            }
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        inkedCardsList.clear();
    }
}
