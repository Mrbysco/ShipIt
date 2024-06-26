package com.mrbysco.buriedwrecks.datagen;

import com.mrbysco.buriedwrecks.BuriedWrecks;
import com.mrbysco.buriedwrecks.registry.ModStructureSets;
import com.mrbysco.buriedwrecks.registry.ModStructures;
import com.mrbysco.buriedwrecks.util.BuriedBiomeTags;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class BuriedDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = CompletableFuture.supplyAsync(BuriedDatagen::getProvider);
		ExistingFileHelper helper = event.getExistingFileHelper();


		generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
				packOutput, CompletableFuture.supplyAsync(BuriedDatagen::getPatchedRegistries), Set.of(BuriedWrecks.MOD_ID)));

		generator.addProvider(event.includeServer(), new BuriedStructureFeatureTagProvider(packOutput, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new BuriedBiomeTagProvider(packOutput, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new StructureUpdater("structure/buried_shipwreck",
				BuriedWrecks.MOD_ID, helper, packOutput));
	}

	private static RegistrySetBuilder.PatchedRegistries getPatchedRegistries() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.STRUCTURE, ModStructures::bootstrap);
		registryBuilder.add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

		// We need the BIOME registry to be present, so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, $ -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(p_311524_ -> p_311524_.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}

	private static HolderLookup.Provider getProvider() {
		return getPatchedRegistries().full();
	}

	public static class BuriedStructureFeatureTagProvider extends TagsProvider<Structure> {
		public BuriedStructureFeatureTagProvider(PackOutput generator, CompletableFuture<HolderLookup.Provider> completableFuture,
		                                         @Nullable ExistingFileHelper existingFileHelper) {
			super(generator, Registries.STRUCTURE, completableFuture, BuriedWrecks.MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(BuriedWrecks.HAS_BURIED_WRECK)
					.add(ModStructures.BURIED_SHIPWRECK);
		}
	}

	public static class BuriedBiomeTagProvider extends BiomeTagsProvider {
		public BuriedBiomeTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
		                              @Nullable ExistingFileHelper existingFileHelper) {
			super(packOutput, completableFuture, BuriedWrecks.MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.tag(BuriedBiomeTags.HAS_BURIED_SHIPWRECK)
					.addTag(BiomeTags.IS_OVERWORLD);
		}
	}
}
