import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    }

    public int[][] insert(int[][] intervals, int[] newInterval) {
        int n = intervals.length;
        List<int[]> result = new ArrayList<>();

  boolean ins=false;
        for(int i=0;i<n;i++) {

            int[] existing = intervals[i];
            //2<4
            if(existing[1]<newInterval[0]) {
                result.add(existing);
            } else if (existing[0]<=newInterval[1]) {
                newInterval[0] = Math.min(existing[0], newInterval[0]);
                newInterval[1] = Math.max(existing[1], newInterval[1]);
            } else {
                if(!ins) {
                    result.add(newInterval);
                    ins=true;
                }
                result.add(existing);
            }
        }

        if (!ins) result.add(newInterval);

        return result.toArray(new int[result.size()][]);
    }



}