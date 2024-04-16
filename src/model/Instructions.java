package model;
import java.util.ArrayList;

public class Instructions {
    private ArrayList<Instruction> commandeListe;

    public Instructions() {
        commandeListe = new ArrayList<>();
    }

    public void add(Instruction e) {
        commandeListe.add(e);
    }

    public ArrayList<Instruction> getCommandeListe() {
        return commandeListe;
    }

    public void clear() {
        commandeListe.clear();
    }

}
