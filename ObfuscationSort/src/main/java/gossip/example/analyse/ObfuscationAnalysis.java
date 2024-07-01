package gossip.example.analyse;

import gossip.example.CLIParser;
import gossip.example.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObfuscationAnalysis {

    private static Set<String> wordlist;
    private static final Set<String> whiteList = new HashSet<>(Arrays.asList("os", "io", "ui"));

    public static boolean isWordObfuscated(String word) {
        if (wordlist == null) {
            wordlist = Utils.readLinesToSet(CLIParser.getWordlistPath());
        }
        assert wordlist != null;

        word = word.toLowerCase();
        if (word.length() >= 5) {
            return false;
        }

        if (word.length() <= 2 && !whiteList.contains(word)) {
            return true;
        }
        return !wordlist.contains(word);
    }
}