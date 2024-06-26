package com.mrbysco.buriedwrecks.registry;

import com.mrbysco.buriedwrecks.BuriedWrecks;
import com.mrbysco.buriedwrecks.feature.BuriedWreckFeature;
import com.mrbysco.buriedwrecks.util.BuriedBiomeTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class ModStructures {
	public static final ResourceKey<Structure> BURIED_SHIPWRECK = createKey("buried_shipwreck");

	public static void bootstrap(BootstrapContext<Structure> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);

		context.register(BURIED_SHIPWRECK, new BuriedWreckFeature(
				new Structure.StructureSettings(biomeGetter.getOrThrow(BuriedBiomeTags.HAS_BURIED_SHIPWRECK), Map.of(),
						GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.NONE),
				false, -20));

	}

	private static ResourceKey<Structure> createKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, name));
	}
}
