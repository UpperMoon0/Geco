package com.nstut.geco.common.block;

import com.nstut.geco.common.Geco;
import com.nstut.geco.common.wood.WoodType;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class GecoSaplingBlock extends SaplingBlock {
    private final WoodType woodType;
    
    public GecoSaplingBlock(WoodType woodType) {
        super(new net.minecraft.world.level.block.grower.TreeGrower(woodType.getPath(), 
              java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()), 
              BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
        this.woodType = woodType;
    }
    
    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            if (!this.mayPlaceOn(level.getBlockState(pos.below()), level, pos.below())) {
                return; // Do not grow if the block below is not suitable
            }

            // Remove the sapling block
            level.removeBlock(pos, false);

            StructureTemplateManager structureTemplateManager = level.getStructureManager();
            
            // Automatically detect available tree structures and filter out blacklisted ones
            String woodName = woodType.getPath();
            List<String> availableTreeNames = new ArrayList<>();
            
            // Start from model 1 and check each one until we can't find any more
            for (int i = 1; i <= 20; i++) { // Check up to 20 models (reasonable limit)
                if (!woodType.isModelBlacklisted(i)) {
                    String treeName = woodName + "_tree_m" + i;
                    ResourceLocation testLocation = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, treeName);
                    
                    // Check if the structure exists
                    Optional<StructureTemplate> testTemplate = structureTemplateManager.get(testLocation);
                    if (testTemplate.isPresent()) {
                        availableTreeNames.add(treeName);
                    } else {
                        // If we can't find this model and we haven't found any yet, continue checking
                        // If we've found some models but hit a gap, we might want to continue checking
                        // But for simplicity, we'll assume models are sequential starting from 1
                        if (availableTreeNames.isEmpty()) {
                            continue;
                        } else {
                            // We found some models but hit a gap, continue checking a few more
                            // in case there are non-sequential models
                            continue;
                        }
                    }
                }
            }
            
            if (!availableTreeNames.isEmpty()) {
                String selectedTree = availableTreeNames.get(random.nextInt(availableTreeNames.size()));
                ResourceLocation structureLocation = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, selectedTree);

                // Get the structure template (we know it exists from our check above)
                Optional<StructureTemplate> optionalTemplate = structureTemplateManager.get(structureLocation);
                if (optionalTemplate.isPresent()) {
                    StructureTemplate template = optionalTemplate.get();
                    BlockPos structureOrigin = pos.offset(-template.getSize().getX() / 2, 0, -template.getSize().getZ() / 2);

                    StructurePlaceSettings settings = new StructurePlaceSettings();

                    template.placeInWorld(level, structureOrigin, structureOrigin, settings, random, 3);
                }
            }
        }
    }
}
