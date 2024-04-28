package com.mrbysco.buriedwrecks.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.buriedwrecks.config.BuriedConfig;
import com.mrbysco.buriedwrecks.registry.ModStructureTypes;
import com.mrbysco.buriedwrecks.structure.BuriedShipwreckPieces;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class BuriedWreckFeature extends Structure {
	public static final MapCodec<BuriedWreckFeature> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
		return builder.group(settingsCodec(builder),
				Codec.BOOL.fieldOf("is_beached").forGetter((feature) -> {
					return feature.isBeached;
				}),
				Codec.INT.fieldOf("y_level").forGetter((feature) -> feature.yLevel)
		).apply(builder, BuriedWreckFeature::new);
	});

	public final boolean isBeached;
	public final int yLevel;

	public BuriedWreckFeature(Structure.StructureSettings structureSettings, boolean beached, int yLevel) {
		super(structureSettings);
		this.isBeached = beached;
		this.yLevel = yLevel;
	}

	public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext generationContext) {
		return onTopOfChunkCenter(generationContext, Heightmap.Types.WORLD_SURFACE_WG, (piecesBuilder) -> {
			this.generatePieces(piecesBuilder, generationContext);
		});
	}

	private void generatePieces(StructurePiecesBuilder piecesBuilder, Structure.GenerationContext generationContext) {
		Rotation rotation = Rotation.getRandom(generationContext.random());
		int yLevel = BuriedConfig.COMMON.overwriteYLevel.get() ? BuriedConfig.COMMON.yLevel.get() : this.yLevel;
		BlockPos blockpos = new BlockPos(generationContext.chunkPos().getMinBlockX(), yLevel, generationContext.chunkPos().getMinBlockZ());
		BuriedShipwreckPieces.addPieces(generationContext.structureTemplateManager(), blockpos, rotation, piecesBuilder, generationContext.random(), this.isBeached);
	}

	@Override
	public Decoration step() {
		return Decoration.UNDERGROUND_STRUCTURES;
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.BURIED_SHIPWRECK.get();
	}
}
