package org.game.Figures;

import org.utils.Pair;

public class EmptySpace implements IFigure, Cloneable{
    @Override
    public Teams getOwner() {
        return Teams.Empty;
    }
    private final String name = "Empty Space";

    @Override
    public String getType() {
        return name;
    }

    @Override
    public boolean checkMoveValidity(Pair<Integer, Integer> cords1, Pair<Integer, Integer> cords2) {
        //lol
        return false;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
