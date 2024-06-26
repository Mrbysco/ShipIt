package com.mrbysco.buriedwrecks.structure;

import com.mrbysco.buriedwrecks.BuriedWrecks;
import com.mrbysco.buriedwrecks.registry.ModStructurePieceTypes;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;

public class BuriedShipwreckPieces {
	static final BlockPos PIVOT = new BlockPos(4, 0, 15);
	private static final ResourceLocation[] STRUCTURE_LOCATION_BEACHED = new ResourceLocation[]{
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/with_mast"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_full"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_fronthalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_backhalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_full"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_fronthalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_backhalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/with_mast_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_full_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_fronthalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_backhalf_degraded")};
	private static final ResourceLocation[] STRUCTURE_LOCATION_OCEAN = new ResourceLocation[]{
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/with_mast"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_full"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_fronthalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_backhalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_full"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_fronthalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_backhalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_full"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_fronthalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_backhalf"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/with_mast_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_full_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_fronthalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/upsidedown_backhalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_full_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_fronthalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/sideways_backhalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_full_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_fronthalf_degraded"),
			ResourceLocation.fromNamespaceAndPath(BuriedWrecks.MOD_ID, "buried_shipwreck/rightsideup_backhalf_degraded")};
	static final Map<String, ResourceKey<LootTable>> MARKERS_TO_LOOT = Map.of(
			"map_chest", BuiltInLootTables.SHIPWRECK_MAP,
			"treasure_chest", BuiltInLootTables.SHIPWRECK_TREASURE,
			"supply_chest", BuiltInLootTables.SHIPWRECK_SUPPLY);

	public static void addPieces(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieceAccessor, RandomSource randomSource, boolean isBeached) {
		ResourceLocation resourcelocation = Util.getRandom(isBeached ? STRUCTURE_LOCATION_BEACHED : STRUCTURE_LOCATION_OCEAN, randomSource);
		pieceAccessor.addPiece(new BuriedShipwreckPiece(templateManager, resourcelocation, pos, rotation, isBeached));
	}

	public static class BuriedShipwreckPiece extends TemplateStructurePiece {
		private final boolean isBeached;

		public BuriedShipwreckPiece(StructureTemplateManager templateManager, ResourceLocation structureLocation, BlockPos pos, Rotation rotation, boolean beached) {
			super(ModStructurePieceTypes.BURIED_SHIPWRECK_PIECE.get(), 0, templateManager, structureLocation, structureLocation.toString(), makeSettings(rotation), pos);
			this.isBeached = beached;
		}

		public BuriedShipwreckPiece(StructureTemplateManager templateManager, CompoundTag tag) {
			super(ModStructurePieceTypes.BURIED_SHIPWRECK_PIECE.get(), tag, templateManager, (location) -> {
				return makeSettings(Rotation.valueOf(tag.getString("Rot")));
			});
			this.isBeached = tag.getBoolean("isBeached");
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext serializationContext, CompoundTag tag) {
			super.addAdditionalSaveData(serializationContext, tag);
			tag.putBoolean("isBeached", this.isBeached);
			tag.putString("Rot", this.placeSettings.getRotation().name());
		}

		private static StructurePlaceSettings makeSettings(Rotation rotation) {
			return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(BuriedShipwreckPieces.PIVOT)
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
		}

		@Override
		protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
			ResourceKey<LootTable> resourcekey = BuriedShipwreckPieces.MARKERS_TO_LOOT.get(pName);
			if (resourcekey != null) {
				RandomizableContainer.setBlockEntityLootTable(pLevel, pRandom, pPos.below(), resourcekey);
			}
		}

		@Override
		public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator,
		                        RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
			int i = worldGenLevel.getMaxBuildHeight();
			int j = pos.getY();

			Vec3i vec3i = this.template.getSize();
			int i1 = this.isBeached ? i - vec3i.getY() / 2 - random.nextInt(3) : j;
			this.templatePosition = new BlockPos(this.templatePosition.getX(), i1, this.templatePosition.getZ());

			super.postProcess(worldGenLevel, structureManager, chunkGenerator, random, boundingBox, chunkPos, pos);
		}
	}
}