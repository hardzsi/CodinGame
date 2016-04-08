import java.util.*;
class Player {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int levels = in.nextInt();
        in.nextInt();
        in.next(); in.next();
        int exitPos = in.nextInt();
        in.next(); in.next();
        int lifts = in.nextInt();
        boolean[] blocked = new boolean[levels];
        Integer[][] data = new Integer[lifts][2];
        for (int i = 0; i < lifts; i++) {
            data[i][0] = in.nextInt();
            data[i][1] = in.nextInt();
        }
        Arrays.sort(data, new Comparator<Integer[]>() {
            public int compare(Integer[] arg1, Integer[] arg2) {
                return arg1[0].compareTo(arg2[0]);
            }
        });
        while (true) {
            int cloneLev = in.nextInt(),
                clonePos = in.nextInt();
            String dir = in.next(),
                   move = "WAIT";
            if (cloneLev != -1) {
                int targetPos = (cloneLev < levels - 1) ? data[cloneLev][1] : exitPos;
                if ((targetPos < clonePos & dir.equals("RIGHT") & !blocked[cloneLev]) |
                    (targetPos > clonePos & dir.equals("LEFT")  & !blocked[cloneLev])) {
                    move = "BLOCK";
                    blocked[cloneLev] = true;
                }
            }
            System.out.println(move);
        }
    }
}