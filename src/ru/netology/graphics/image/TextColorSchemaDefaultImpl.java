package ru.netology.graphics.image;

import java.util.NavigableMap;
import java.util.TreeMap;

public class TextColorSchemaDefaultImpl implements TextColorSchema {
    private final static NavigableMap<Integer, Character> colorMap = new TreeMap<>();

    public TextColorSchemaDefaultImpl() {
        colorMap.put(0, '▇');
        colorMap.put(32, '●');
        colorMap.put(64, '◉');
        colorMap.put(96, '◍');
        colorMap.put(128, '◎');
        colorMap.put(160, '○');
        colorMap.put(192, '☉');
        colorMap.put(224, '◌');
        colorMap.put(255, '-');
    }

    @Override
    public char convert(int color) {
        return colorMap.floorEntry(color).getValue();
    }
}
