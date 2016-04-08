// F:levels | X:exitPos | E:lifts | b:blocked | d:data | L:cloneLev | P:clonePos | m:move | T:targetPos | g:direction
import java.util.*;
class Player{
	public static void main(String[] A){
		Scanner s=new Scanner(System.in);
		int F=s.nextInt();s.next();s.next();s.next();int X=s.nextInt();s.next();s.next();int E=s.nextInt();
		boolean[] b=new boolean[F];
		Integer[][] d=new Integer[E][2];
		for(int i=0;i<E;i++){
			d[i][0]=s.nextInt();d[i][1]=s.nextInt();
		}
		Arrays.sort(d,new Comparator<Integer[]>(){
			public int compare(Integer[] a,Integer[] b){
				return a[0].compareTo(b[0]);
			}
		});
		while(F>0){
			int L=s.nextInt(),P=s.nextInt();
			String g=s.next(),m="WAIT";
			if(L!=-1){
				int T=L<F-1?d[L][1]:X;
				if((T<P&g.equals("RIGHT")&!b[L])|(T>P&g.equals("LEFT")&!b[L])){
					m="BLOCK";b[L]=true;
				}
			}
			System.out.println(m);
		}
	}
}