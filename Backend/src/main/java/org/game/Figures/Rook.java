package org.game.Figures;

import org.utils.Pair;

public class Rook implements IFigure, IHradable, Cloneable {
    private final Teams owner;
    private boolean hradovaniAble = true;
    private final String name = "Rook";

    public Rook(Teams owner){
        this.owner = owner;
    }

    @Override
    public Teams getOwner() {
        return owner;
    }

    @Override
    public String getType() {
        return name;
    }

    @Override
    public boolean checkMoveValidity(Pair<Integer, Integer> cords1, Pair<Integer, Integer> cords2) {
        return cords1.second() == cords2.second() || cords1.first() == cords2.first();
    }
    @Override
    public void setHradovniAble(boolean value){
        hradovaniAble = value;
    }
    @Override
    public boolean isHradAble(){
        return hradovaniAble;
    }
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
