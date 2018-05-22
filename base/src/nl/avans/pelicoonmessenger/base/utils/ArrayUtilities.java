package nl.avans.pelicoonmessenger.base.utils;

import java.util.ArrayList;

public class ArrayUtilities {
    public static boolean isArrayListOfType(ArrayList<?> array, Class<?> type) {
        return isArrayOfType(array.toArray(), type);
    }

    public static boolean isArrayOfType(Object[] array, Class<?> type) {
        return array.length > 0
                && array.getClass().getComponentType().isAssignableFrom(type);
    }
}
