import java.util.*;
class Player{
	public static void main(String[] t){
		Scanner s=new Scanner(System.in);
		int levels=s.nextInt();s.nextInt();s.next();s.next();int exitPos=s.nextInt();s.next();s.next();int lifts=s.nextInt();
		boolean[] blocked=new boolean[levels];
		Integer[][] data=new Integer[lifts][2];
		for(int i=0;i<lifts;i++){
			data[i][0]=s.nextInt();data[i][1]=s.nextInt();
		}
		Arrays.sort(data,new Comparator<Integer[]>(){
			public int compare(Integer[] a,Integer[] b){
				return a[0].compareTo(b[0]);
			}
		});
		while(true){
			int cloneLev=s.nextInt(),clonePos=s.nextInt();
			String dir=s.next(),move="WAIT";
			if(cloneLev!=-1){
				int targetPos=(cloneLev<levels-1)?data[cloneLev][1]:exitPos;
				if((targetPos<clonePos&dir.equals("RIGHT")&!blocked[cloneLev])|
					(targetPos>clonePos&dir.equals("LEFT")&!blocked[cloneLev])){
					move="BLOCK";blocked[cloneLev]=true;
				}
			}
			System.out.println(move);
		}
	}
}