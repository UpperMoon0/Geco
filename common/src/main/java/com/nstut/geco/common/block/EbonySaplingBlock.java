package com.nstut.geco.common.block;

import com.nstut.geco.common.Geco;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

public class EbonySaplingBlock extends SaplingBlock {
    public EbonySaplingBlock() {
        super(new net.minecraft.world.level.block.grower.TreeGrower("ebony", java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()), BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }
    
        @Override
        public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
            if (state.getValue(STAGE) == 0) {
                level.setBlock(pos, state.cycle(STAGE), 4);
            } else {
                // Remove the sapling block
                level.removeBlock(pos, false);
    
                StructureTemplateManager structureTemplateManager = level.getStructureManager();
                String[] treeNames = {"ebony_tree_m1", "ebony_tree_m2", "ebony_tree_m3", "ebony_tree_m4", "ebony_tree_m5"};
                String selectedTree = treeNames[random.nextInt(treeNames.length)];
                ResourceLocation structureLocation = ResourceLocation.fromNamespaceAndPath(Geco.MOD_ID, selectedTree);
    
                // Randomly select one of the 5 ebony tree structures
                Optional<StructureTemplate> optionalTemplate = structureTemplateManager.get(structureLocation);
    
                if (optionalTemplate.isPresent()) {
                    StructureTemplate template = optionalTemplate.get();
                    BlockPos structureOrigin = pos.offset(-template.getSize().getX() / 2, 0, -template.getSize().getZ() / 2); 
    
                    StructurePlaceSettings settings = new StructurePlaceSettings()
                            .setRotation(Rotation.getRandom(random))
                            .setMirror(Mirror.values()[random.nextInt(Mirror.values().length)])
                            .setRandom(random);
    
                    template.placeInWorld(level, structureOrigin, structureOrigin, settings, random, 3); 
                }
            }
        }
}
