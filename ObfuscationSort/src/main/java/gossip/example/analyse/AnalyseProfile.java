package gossip.example.analyse;

import gossip.example.Hierarchy.HierarchyNode;
import gossip.example.Utils;
import gossip.example.Hierarchy.HierarchyTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.util.Chain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyseProfile {
    private final HierarchyTree packages;
    private final Logger logger = LoggerFactory.getLogger(AnalyseProfile.class);

    public AnalyseProfile() {
        this.packages = new HierarchyTree();
    }

    public void AppProfile(String apkPath, ProcessManifest manifest) {
        logger.info("Profiling: " + apkPath);
        PackManager.v().runPacks();
        Chain<SootClass> sootClasses = Scene.v().getApplicationClasses();
        logger.info("Checking package names");
        build(sootClasses);
    }

    private void build(Chain<SootClass> sootClasses) {
        for (SootClass clz : sootClasses) {

            if(clz.isPhantomClass()
                || Utils.isAndroidClass(clz)
                || Utils.isResourceClass(clz)
            ) {
                continue;
            }

            packages.addNode(clz.getPackageName());

        }
    }

    public HierarchyTree getPackages() {
        return packages;
    }

    private List<String> packs;
    public List<String> getResult() {
        this.packs = new ArrayList<>();
        for (Map.Entry<String, HierarchyNode> entry:packages.root.getChild().entrySet()) {
            subPack(entry.getKey(), entry.getValue());
        }
        return packs;
    }

    private void subPack(String st, HierarchyNode k) {
        if(!k.hasChild()){
            this.packs.add(st);
            return;
        } else {
            for (Map.Entry<String, HierarchyNode> entry:k.getChild().entrySet()) {
                subPack(st+"."+entry.getKey(), entry.getValue());
            }
        }
    }
}
