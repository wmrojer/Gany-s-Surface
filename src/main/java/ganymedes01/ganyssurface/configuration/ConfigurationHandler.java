package ganymedes01.ganyssurface.configuration;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.integration.Integration;
import ganymedes01.ganyssurface.integration.ModIntegrator;
import ganymedes01.ganyssurface.lib.Reference;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class ConfigurationHandler {

	public static ConfigurationHandler INSTANCE = new ConfigurationHandler();
	public Configuration configFile;
	public String[] usedCategories = { Configuration.CATEGORY_GENERAL, "mod integration" };

	private int configInteger(String name, boolean requireRestart, int def) {
		return configInteger(name, null, requireRestart, def);
	}

	private int configInteger(String name, String tooltip, boolean requireRestart, int def) {
		int config = configFile.get(Configuration.CATEGORY_GENERAL, name, def, tooltip).getInt(def);
		return config > 0 ? config : def;
	}

	private boolean configBoolean(String name, String tooltip, boolean requireRestart, boolean def) {
		return configFile.get(Configuration.CATEGORY_GENERAL, name, def, tooltip).getBoolean(def);
	}

	private boolean configBoolean(String name, boolean requireRestart, boolean def) {
		return configBoolean(name, null, requireRestart, def);
	}

	private boolean configIntegrationBoolean(String modID) {
		return configFile.get("Mod Integration", "Integrate " + modID, true).setRequiresMcRestart(true).getBoolean(true);
	}

	public void init(File file) {
		configFile = new Configuration(file);

		syncConfigs();
	}

	private void syncConfigs() {
		// Mod Integration
		for (Integration integration : ModIntegrator.modIntegrations)
			integration.setShouldIntegrate(configIntegrationBoolean(integration.getModID()));

		// Blocks/Items
		GanysSurface.enableChocolate = configBoolean("activateChocolate", true, true);
		GanysSurface.enablePoop = configBoolean("enablePoop", true, true);
		GanysSurface.enableWoodenArmour = configBoolean("enableWoodenArmour", true, true);
		GanysSurface.enableTea = configBoolean("enableTea", true, true);
		GanysSurface.enableQuiver = configBoolean("enableQuiver", true, true);
		GanysSurface.enablePaintings = configBoolean("Enable paintings", true, true);
		GanysSurface.enableDispenserShears = configBoolean("Enable dispenser action for shears", true, true);
		GanysSurface.enable3DRendering = configBoolean("Enable 3D rendering for vanilla items", "Vanilla items (hoppers, minecarts, etc) render in 3D in your inventory", true, true);
		GanysSurface.enablePlanter = configBoolean("Enable planter", true, true);
		GanysSurface.enableColouredRedstone = configBoolean("Enable coloured redsotne", true, true);
		GanysSurface.enableDislocators = configBoolean("Enable dislocators and block detector", true, true);
		GanysSurface.enableItemDisplay = configBoolean("Enable item display", true, true);
		GanysSurface.enablePineCones = configBoolean("Enable pine cones", true, true);
		GanysSurface.enableCookedEgg = configBoolean("Enable cooked egg", true, true);
		GanysSurface.enablePocketCritters = configBoolean("Enable pocket critters", true, true);
		GanysSurface.enableWorkTables = configBoolean("Enable work tables", true, true);
		GanysSurface.enableLeafWalls = configBoolean("Enable leaf walls", true, true);
		GanysSurface.enableDisguisedTrapdoors = configBoolean("Enable disguised trapdoors", true, true);
		GanysSurface.enableEncasers = configBoolean("Enable encasers", true, true);
		GanysSurface.enableOMC = configBoolean("Enable organic matter compressor", true, true);
		GanysSurface.enableSpawnEggs = configBoolean("Enable spawn eggs", true, true);

		// 1.8 Stuff
		GanysSurface.enable18Stones = configBoolean("Enable 1.8 Stones", true, true);
		GanysSurface.enableIronTrapdoor = configBoolean("Enable Iron Trapdoor", true, true);
		GanysSurface.enableMutton = configBoolean("Enable Mutton", true, true);
		GanysSurface.enableSpongeTexture = configBoolean("Enable new sponge texture", true, true);
		GanysSurface.enablePrismarineStuff = configBoolean("Enable Prismarine stuff", true, true);
		GanysSurface.enableDoors = configBoolean("Enable 1.8 style doors", true, true);
		GanysSurface.enableInvertedDaylightSensor = configBoolean("Enable inverted daylight sensor", true, true);

		// Others
		GanysSurface.shouldDoVersionCheck = configBoolean("shouldDoVersionCheck", true, true);
		GanysSurface.poopRandomBonemeals = configBoolean("poopRandomBonemeals", false, true);
		GanysSurface.enableDynamicSnow = configBoolean("enableDynamicSnow", "Snow layers will get taller when it snows and shorter when it stops snowing", false, true);
		GanysSurface.noDespawnRadius = configInteger("noDespawnRadius", "If there are any players within this radius of an item thrown on the ground that item will not despawn.\n-Set it to 0 to disable this feature.\n-Set it to LESS THAN 0 and items will never despawn regardless of players being nearby.", false, GanysSurface.noDespawnRadius);

		GanysSurface.maxLevelOMCWorks = configInteger("maxLevelOMCWorks", false, 15);
		GanysSurface.inkHarvesterMaxStrike = configInteger("inkHarvesterMaxStrike", false, 5);
		GanysSurface.poopingChance = configInteger("poopingChance", "Larger number means poop is LESS likely to happen", false, 15000);
		GanysSurface.prismarineTempleChance = configInteger("prismarineTempleChance", "Larger number means temples are LESS likely to happen", false, GanysSurface.prismarineTempleChance);

		if (configFile.hasChanged())
			configFile.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (Reference.MOD_ID.equals(eventArgs.modID))
			syncConfigs();
	}
}