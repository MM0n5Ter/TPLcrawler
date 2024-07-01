package gossip.example;

import soot.SootClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Utils {
    public static boolean validateFile(String filePath) {
        return new File(filePath).isFile();
    }

    public static boolean validateDirectory(String filePath) {
        return new File(filePath).isDirectory();
    }

    public static Set<String> readLinesToSet(String filePath) {
        //System.out.println("readlinestoset");
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.collect(Collectors.toSet());
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static String getDividingLine(int size) {
        return String.join("", Collections.nCopies(size, "--"));
    }

    public static boolean isResourceClass(String className) {
        return className.endsWith(".R");
    }

    public static boolean isResourceClass(SootClass sootClass) {
        String classSignature = sootClass.getName();
        return isResourceClass(classSignature);
    }

    public static boolean isAndroidClass(SootClass sootClass) {
        String classSignature = sootClass.getName();
        return isAndroidClass(classSignature);
    }

    public static boolean isAndroidClass(String className) {
        List<String> androidPrefixPkgNames = Arrays.asList("android.", "com.google.android.", "com.android.", "androidx.",
                "kotlin.", "kotlinx.", "java.", "javax.", "sun.", "com.sun.", "jdk.", "j$.",
                "org.omg.", "org.xml.", "org.w3c.dom");
        return androidPrefixPkgNames.stream().map(className::startsWith).reduce(false, (res, curr) -> res || curr);
    }


    public static String getRawType(String type) {
        return type.split("\\$")[0];
    }

    public static String getRawTypeWithoutArray(String type) {
        return type.replace("[]", "").split("\\$")[0];
    }


    public static String getEnclosingClass(String className) {
        return className.split("\\$")[0];
    }

    public static boolean isAndroidType(String type) {
        // "kotlin.", "com.google.android.",
        List<String> androidPrefixNames = Arrays.asList("android.", "androidx.", "java.", "javax.");
        for (String s: androidPrefixNames) {
            if (type.startsWith(s)) {
                return true;
            }
        }

        List<String> basicTypes = Arrays.asList("byte", "short", "int", "long", "float", "double", "boolean", "char", "void");
        type = getRawType(type);
        for (String s: basicTypes) {
            if (s.equals(type)) {
                return true;
            }
        }

        return false;
    }

    public static String getNormalizedType(String className) {
        if (isAndroidType(className)) {
            return getRawType(className);
        } else {
            return "X";
        }
    }

    public static String getNormalizedClassName(String className) {
        if (isAndroidClass(className)) {
            return className;
        } else {
            return "X";
        }
    }

    public static String dexClassType2Name(String classType ) {
        classType = classType.replace('/', '.');
        return classType.substring(1, classType.length()-1);
    }

}