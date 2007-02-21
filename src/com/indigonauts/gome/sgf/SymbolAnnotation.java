/*
 * (c) 2006 Indigonauts
 */
package com.indigonauts.gome.sgf;

import com.indigonauts.gome.common.Point;

public class SymbolAnnotation extends Annotation {
    public static final byte CIRCLE = 1;

    public static final byte CROSS = 2;

    public static final byte SQUARE = 3;

    public static final byte TRIANGLE = 4;

    private int type;

    SymbolAnnotation(Point p, int type) {
        super(p);
        this.type = type;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
}