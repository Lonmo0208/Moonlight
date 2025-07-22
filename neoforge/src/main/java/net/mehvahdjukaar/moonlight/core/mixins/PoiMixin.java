package net.mehvahdjukaar.moonlight.core.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.world.poi.PoiStateSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = PoiType.class, priority = 2000)
public abstract class PoiMixin {

    // 处理旧版 NeoForge (使用 Set.copyOf)
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;copyOf(Ljava/util/Collection;)Ljava/util/Set;"
            ),
            require = 0
    )
    private Set<BlockState> moonlight$handleOldNeoForge(
            Set<BlockState> original,
            @Local(argsOnly = true) Collection<BlockState> states
    ) {
        return new HashSet<>(states);
    }

    // 处理新版 NeoForge (使用 PoiStateSet)
    @ModifyVariable(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/common/world/poi/PoiStateSet;<init>(Ljava/util/Set;)V",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0, // 修改第一个方法参数
            require = 0
    )
    private Set<BlockState> moonlight$modifySetForNewNeoForge(Set<BlockState> states) {
        return new HashSet<>(states);
    }
}