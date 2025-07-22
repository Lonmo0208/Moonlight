package net.mehvahdjukaar.moonlight.core;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.world.poi.PoiStateSet;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

public class PoiTypeHelper {

    private static Field matchingStatesField;
    private static Field backingSetField;

    static {
        try {
            matchingStatesField = PoiType.class.getDeclaredField("matchingStates");
            matchingStatesField.setAccessible(true);

            backingSetField = PoiStateSet.class.getDeclaredField("backingSet");
            backingSetField.setAccessible(true);
        } catch (Exception e) {
            // 初始化失败处理
        }
    }

    public static Set<BlockState> getMutableStates(PoiType poiType) {
        try {
            if (matchingStatesField == null) {
                matchingStatesField = PoiType.class.getDeclaredField("matchingStates");
                matchingStatesField.setAccessible(true);
            }

            Object states = matchingStatesField.get(poiType);

            if (states instanceof PoiStateSet) {
                if (backingSetField == null) {
                    backingSetField = PoiStateSet.class.getDeclaredField("backingSet");
                    backingSetField.setAccessible(true);
                }
                return (Set<BlockState>) backingSetField.get(states);
            }
            return (Set<BlockState>) states;
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }

    public static boolean addStateToPoi(PoiType poiType, BlockState state) {
        Set<BlockState> states = getMutableStates(poiType);
        return states != null && states.add(state);
    }

    public static boolean removeStateFromPoi(PoiType poiType, BlockState state) {
        Set<BlockState> states = getMutableStates(poiType);
        return states != null && states.remove(state);
    }
}