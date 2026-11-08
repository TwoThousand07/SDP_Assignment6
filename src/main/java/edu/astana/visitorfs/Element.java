package edu.astana.visitorfs;

public interface Element {
    String getName();

    void accept(Visitor visitor);
}
