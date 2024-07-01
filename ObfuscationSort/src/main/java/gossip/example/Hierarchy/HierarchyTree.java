package gossip.example.Hierarchy;

import java.util.ArrayList;
import java.util.List;
import gossip.example.analyse.ObfuscationAnalysis;

public class HierarchyTree {
    public HierarchyNode root;

    public HierarchyTree() {
        root = new HierarchyNode();
    }

    public void addNode(String packageName) {
        if (packageName.isEmpty()) {
            return;
        }

        String[] names = packageName.split("\\.");
        List<String> newPack = new ArrayList<>();
        int k = 0;
        for (String name : names) {
            if (!ObfuscationAnalysis.isWordObfuscated(name)) {
                k++;
                newPack.add(name);
            } else {
                break;
            }
            if(k==4) {
                break;
            }
        }

        if (newPack.isEmpty()) {
            return;
        } else {
            HierarchyNode i = root;
            for (String name : newPack) {
                if (i.hasChild(name)) {
                    i = i.getChild(name);
                } else {
                    i = i.addChild(name);
                }
            }
        }

    }
}
