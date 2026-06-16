import java.util.HashSet;
import java.util.Set;

public class SortManual {
    public static void main(String[] args) {
        String s = "dakshin 123 okok";
        String s2 ="dakshin okok";
        int n = s.length();
        Set<String> wordsInFirst = new HashSet<>();
        StringBuilder current = new StringBuilder();     // accumulates the current word
        String lower = s.toLowerCase();

        for(int i=0;i<n;i++) {
            char c=lower.charAt(i);
            if(c == ' ' || c == '\t' || c == '\n') {
                  if(!current.isEmpty()) {
                      wordsInFirst.add(current.toString());
                      current.setLength(0);
                  }

            } else {
            current.append(c);
        }
        }
        if (!current.isEmpty()) {
            wordsInFirst.add(current.toString());
        }

        for (String word:wordsInFirst) {
            if(s2.contains(word))
            {
                System.out.println(word);
            }
        }
    }

}
