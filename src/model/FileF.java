package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class FileF {
    private Queue<String> contenu;

    public FileF() {
        this.contenu = new LinkedList<>();
    }

    public void enqueue(String data) {
        contenu.add(data);
    }

    public String dequeue() {
        return contenu.poll();
    }

    public boolean Test_EOF() {
        return contenu.isEmpty();
    }
    public List<String> getContenuCommeListe() {
        return new ArrayList<>(contenu);
    }
}
