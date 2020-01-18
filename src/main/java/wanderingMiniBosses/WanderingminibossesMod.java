package wanderingMiniBosses;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wanderingMiniBosses.cards.*;
import wanderingMiniBosses.characters.Wanderingminibosses;
import wanderingMiniBosses.events.IdentityCrisisEvent;
import wanderingMiniBosses.patches.MaybeSpawnDudePatch;
import wanderingMiniBosses.potions.PlaceholderPotion;
import wanderingMiniBosses.relics.BottledPlaceholderRelic;
import wanderingMiniBosses.relics.DefaultClickableRelic;
import wanderingMiniBosses.relics.PlaceholderRelic;
import wanderingMiniBosses.relics.PlaceholderRelic2;
import wanderingMiniBosses.util.IDCheckDontTouchPls;
import wanderingMiniBosses.util.TextureLoader;
import wanderingMiniBosses.variables.DefaultCustomVariable;
import wanderingMiniBosses.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class WanderingminibossesMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnStartBattleSubscriber,
        PostInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(WanderingminibossesMod.class.getName());
    private static String modID;

    public static Properties wanderingMiniBossesDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

    private static final String MODNAME = "Wandering Minibosses";
    private static final String AUTHOR = "erasels, raz";
    private static final String DESCRIPTION = "TBD";
    
    // =============== INPUT TEXTURE LOCATION =================
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);
    
    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "wanderingMiniBossesResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "wanderingMiniBossesResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "wanderingMiniBossesResources/images/512/bg_power_default_gray.png";
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = "wanderingMiniBossesResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "wanderingMiniBossesResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "wanderingMiniBossesResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "wanderingMiniBossesResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "wanderingMiniBossesResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "wanderingMiniBossesResources/images/1024/card_default_gray_orb.png";

    public static final String BADGE_IMAGE = "wanderingMiniBossesResources/images/Badge.png";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
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
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public WanderingminibossesMod() {
        BaseMod.subscribe(this);
      
        setModID("wanderingMiniBosses");
        
        logger.info("Creating the color " + Wanderingminibosses.Enums.COLOR_GRAY.toString());
        
        BaseMod.addColor(Wanderingminibosses.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

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
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    
    // ================ ADD POTIONS ===================
    
    public void receiveEditPotions() {
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, Wanderingminibosses.Enums.THE_DEFAULT);
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), Wanderingminibosses.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), Wanderingminibosses.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), Wanderingminibosses.Enums.COLOR_GRAY);
        
        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        
        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {

    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
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
        
        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Character-Strings.json");
        
        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/WanderingminibossesMod-Orb-Strings.json");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
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
    
    // ================ /LOAD THE KEYWORDS/ ===================    

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }


    private static final float CHANCE = 0.15F;
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if(Settings.isDebug || AbstractDungeon.monsterRng.randomBoolean(CHANCE)) {
            MaybeSpawnDudePatch.resetTurnCounter();
        } else {
            MaybeSpawnDudePatch.noEncounterThisFight();
        }
    }
}
