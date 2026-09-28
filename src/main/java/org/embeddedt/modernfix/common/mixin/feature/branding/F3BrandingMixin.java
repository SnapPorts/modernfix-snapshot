package org.embeddedt.modernfix.common.mixin.feature.branding;

import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.ModernFixClient;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugScreenEntries.class)
@ClientOnlyMixin
public class F3BrandingMixin {
    private static final Identifier MODERNFIX_VERSION = Identifier.fromNamespaceAndPath(ModernFix.MODID, "version");

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerModernFixDebugEntry(CallbackInfo ci) {
        DebugScreenEntries.register(MODERNFIX_VERSION, new DebugScreenEntry() {
            @Override
            public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
                if (ModernFixClient.INSTANCE != null && ModernFixClient.INSTANCE.brandingString != null) {
                    displayer.addToGroup(MODERNFIX_VERSION, ModernFixClient.INSTANCE.brandingString);
                }
            }
        });
    }
}
