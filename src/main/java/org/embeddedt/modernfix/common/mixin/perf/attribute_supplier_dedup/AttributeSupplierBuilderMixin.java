package org.embeddedt.modernfix.common.mixin.perf.attribute_supplier_dedup;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.embeddedt.modernfix.entity.AttributeInstanceTemplates;
import org.embeddedt.modernfix.neoforge.init.ModernFixForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(AttributeSupplier.Builder.class)
public class AttributeSupplierBuilderMixin {
    @ModifyArg(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;<init>(Ljava/util/Map;)V"))
    private Map<Holder<Attribute>, AttributeInstance> deduplicateInstances(Map<Holder<Attribute>, AttributeInstance> map) {
        if (ModernFixForge.registryEventsFired || map.isEmpty()) {
            return map;
        }
        ImmutableMap.Builder<Holder<Attribute>, AttributeInstance> builder = ImmutableMap.builderWithExpectedSize(map.size());
        for (Map.Entry<Holder<Attribute>, AttributeInstance> entry : map.entrySet()) {
            builder.put(entry.getKey(), AttributeInstanceTemplates.intern(entry.getValue()));
        }
        return builder.buildKeepingLast();
    }
}
