package com.nstut.geco.common.worldgen;

import com.nstut.geco.common.registry.ModWoodTypes;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class TemplateTreeFeature extends Feature<NoneFeatureConfiguration> {
    public TemplateTreeFeature() { super(NoneFeatureConfiguration.CODEC); }
    @Override public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return TemplateTreePlacement.place(context.level(), context.origin(), context.random(), ModWoodTypes.EBONY, null);
    }
}
