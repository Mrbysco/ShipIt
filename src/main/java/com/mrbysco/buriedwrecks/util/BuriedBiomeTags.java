package com.mrbysco.buriedwrecks.util;

import com.mrbysco.buriedwrecks.BuriedWrecks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BuriedBiomeTags {
	public static final TagKey<Biome> HAS_BURIED_SHIPWRECK = create("has_structure/buried_shipwreck");

	private static TagKey<Biome> create(String id) {
		return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, id));
	}
}
