package gossip.example.Hierarchy;

import java.util.HashMap;

public class HierarchyNode extends Node<String>{
    private final HashMap<String, HierarchyNode> Child;

    public HierarchyNode(String name) {
        super(name);
        Child = new HashMap<>();
    }

    public HierarchyNode() {
        super();
        Child = new HashMap<>();
    }

    public void addChild(String name, HierarchyNode child) {
        Child.put(name, child);
    }

    public HierarchyNode addChild(String name) {
        HierarchyNode a = new HierarchyNode(name);
        a.setFather(this);
        Child.put(name, a);
        return a;
    }

    public HierarchyNode getChild(String name) {
        return Child.get(name);
    }

    public HashMap<String, HierarchyNode> getChild() {
        return Child;
    }

    public boolean hasChild(String name) {
        return Child.containsKey(name);
    }

    public boolean hasChild() {
        return !Child.isEmpty();
    }

    @Override
    public HierarchyNode getFather() {
        return (HierarchyNode) super.getFather();
    }
}
