package com.joey.mineparkour;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public interface ParkourColor {
    Color RED = new Color(125, 32, 27);
    Color ORANGE = new Color(194, 97, 23);
    Color YELLOW = new Color(181, 154, 42);
    Color LIME = new Color(91, 142, 22);
    Color GREEN = new Color(69, 94, 10);
    Color CYAN = new Color(17, 119, 119);
    Color LIGHT_BLUE = new Color(41, 128, 155);
    Color BLUE = new Color(43, 49, 122);
    Color PURPLE = new Color(99, 35, 132);
    Color MAGENTA = new Color(141, 55, 134);
    Color PINK = new Color(174, 100, 121);
    Color BROWN = new Color(95, 60, 35);
    Color LIGHT_GRAY = new Color(112, 113, 108);
    Color GRAY = new Color(50, 56, 59);
    Color BLACK = new Color(20, 19, 22);

    static List<Color> getValues() {
        List<Color> colorList = new ArrayList<>();

        colorList.add(RED);
        colorList.add(ORANGE);
        colorList.add(YELLOW);
        colorList.add(LIME);
        colorList.add(GREEN);
        colorList.add(CYAN);
        colorList.add(LIGHT_BLUE);
        colorList.add(BLUE);
        colorList.add(PURPLE);
        colorList.add(MAGENTA);
        colorList.add(PINK);
        colorList.add(BROWN);
        colorList.add(LIGHT_GRAY);
        colorList.add(GRAY);
        colorList.add(BLACK);

        return colorList;
    }

    static Color getByName(String name) {
        for (Color i : getValues()) {
            if (i.toString().equalsIgnoreCase(name)) {
                return i;
            }
        }

        return null;
    }
}
