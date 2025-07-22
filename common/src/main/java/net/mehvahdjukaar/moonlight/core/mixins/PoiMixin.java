package net.mehvahdjukaar.moonlight.core.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.world.poi.PoiStateSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = PoiType.class, priority = 2000)
public abstract class PoiMixin {

    @ModifyExpressionValue(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;copyOf(Ljava/util/Collection;)Ljava/util/Set;"
        )
    )
    private Set<BlockState> moonlight$wrapWithMutableSet(
        Set<BlockState> original,
        @Local(argsOnly = true) Collection<BlockState> states
    ) {
        HashSet<BlockState> mutableSet = new HashSet<>(states);
        
        if (original instanceof PoiStateSet) {
            return original;
        }
        
        return new PoiStateSet(mutableSet) {
            public Set<BlockState> getBackingSet() {
                return this.backingSet;
            }
        };
    }
}
