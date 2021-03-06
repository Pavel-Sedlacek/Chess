package org.game.Figures;

import org.utils.Pair;

public class Pawn implements IFigure,Cloneable {
    private final Teams owner;
    private final String name = "Pawn";

    public Pawn(Teams owner) {
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
        //normalni movovani
        if (owner.equals(Teams.Black)) {
            //normalni pohyb
            if (cords1.first() == cords2.first() && cords1.second() + 1 == cords2.second()) {
                return true;
            //pohyb o 2 pole
            } else if (cords1.second() == 1 && cords1.first() == cords2.first() && cords1.second() + 2 == cords2.second()) {
                return true;
            //vyhazovani
            } else if (Math.abs(cords1.first() - cords2.first()) == 1 && cords1.second() + 1 == cords2.second()) {
                return true;
            }
        } else {

            if (cords1.first() == cords2.first() && cords1.second() - 1 == cords2.second()) {
                return true;
            } else if (cords1.second() == 6 && cords1.first() == cords2.first() && cords1.second() - 2 == cords2.second()) {
                return true;
            } else if (Math.abs(cords1.first() - cords2.first()) == 1 && cords1.second() - 1 == cords2.second()) {
                return true;
            }

        }
        return false;
    }
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

}

