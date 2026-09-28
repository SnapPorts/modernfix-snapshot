package org.embeddedt.modernfix.common.mixin.perf.compact_imposterprotochunks;

import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ImposterProtoChunk.class)
public abstract class ImposterProtoChunkMixin extends ChunkAccessMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void replaceDuplicateObjects(LevelChunk wrapped, CallbackInfo ci) {
        this.sections = wrapped.getSections();
        this.skyLightSources = wrapped.getSkyLightSources();
    }
}
