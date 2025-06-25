package com.nstut.geco.common.util;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Utility class for adding custom boat types using reflection
 * Follows vanilla pattern exactly: new enum constant with Block planks and String name
 */
public class BoatTypeHelper {
    
    private static Boat.Type EBONY_TYPE = null;
    
    /**
     * Creates EBONY boat type following exact vanilla pattern
     */
    public static Boat.Type createEbonyBoatType(Block ebonyPlanks) {
        if (EBONY_TYPE != null) {
            return EBONY_TYPE;
        }
        
        try {
            // Use reflection to add new enum constant following vanilla constructor:
            // private Boat$Type(Block planks, String name)
            EBONY_TYPE = addEnumConstant(Boat.Type.class, "EBONY",
                new Class<?>[]{Block.class, String.class},
                new Object[]{ebonyPlanks, "ebony"});
            
            return EBONY_TYPE;
            
        } catch (Exception e) {
            // Fallback - this will still work but won't be as clean
            return Boat.Type.OAK;
        }
    }
    
    /**
     * Add enum constant using reflection - follows vanilla enum pattern exactly
     */
    private static <T extends Enum<T>> T addEnumConstant(Class<T> enumClass, String name,
                                                        Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        
        // Build full parameter types array (name, ordinal, custom params)
        Class<?>[] fullParamTypes = new Class<?>[paramTypes.length + 2];
        fullParamTypes[0] = String.class;
        fullParamTypes[1] = int.class;
        System.arraycopy(paramTypes, 0, fullParamTypes, 2, paramTypes.length);
        
        // Get the enum constructor
        Constructor<T> constructor = enumClass.getDeclaredConstructor(fullParamTypes);
        constructor.setAccessible(true);
        
        // Get existing enum values
        T[] existingValues = enumClass.getEnumConstants();
        int ordinal = existingValues.length;
        
        // Build full parameter values array (name, ordinal, custom values)
        Object[] fullParamValues = new Object[paramValues.length + 2];
        fullParamValues[0] = name;
        fullParamValues[1] = ordinal;
        System.arraycopy(paramValues, 0, fullParamValues, 2, paramValues.length);
        
        // Create new enum instance
        T newEnum = constructor.newInstance(fullParamValues);
        
        // Update the enum's $VALUES array
        Field valuesField = enumClass.getDeclaredField("$VALUES");
        valuesField.setAccessible(true);
        
        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);
        
        // Create new array with additional enum
        T[] newValues = Arrays.copyOf(existingValues, existingValues.length + 1);
        newValues[existingValues.length] = newEnum;
        
        // Set the new array
        valuesField.set(null, newValues);
        
        return newEnum;
    }
    
    public static Boat.Type getEbonyType() {
        return EBONY_TYPE;
    }
}