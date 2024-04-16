package model;

import java.util.LinkedList;

public class Pile<T> {
    // Utilise une LinkedList pour stocker les éléments de la pile en mémoire
    private LinkedList<T> elements;

    public Pile() {
        this.elements = new LinkedList<>();
    }

    // Écrit un nouvel élément en fin de pile
    public void push(T element) {
        this.elements.addLast(element);
    }

    // Lit et retire le dernier élément de la pile
    public T pop() {
        if (!this.elements.isEmpty()) {
            return this.elements.removeLast();
        }
        return null; // Retourne null si la pile est vide
    }

    // Vérifie si la pile est vide
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
}
