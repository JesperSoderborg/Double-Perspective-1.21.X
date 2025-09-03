package net.laisvall.doubleperspective;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoublePerspective implements ModInitializer {
	public static final String MOD_ID = "doubleperspective";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        LOGGER.info("Double Perspective mod initialized!");
	}
}