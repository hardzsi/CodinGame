//F:levels|X:exitPos|E:lifts|d:data|L:cloneLev|P:clonePos|m:move|T:targetPos|g:direction|n:s.nextInt()/s.next()
import java.util.*;
class Player{
	static Scanner s=new Scanner(System.in);
	public static void main(String[] A){
		int F=n(),X,E,L,P,T,i;n();n();n();X=n();n();n();
		Integer[][] d=new Integer[E=n()][2];
		for(i=0;i<E;d[i][0]=n(),d[i++][1]=n());
		Arrays.sort(d,new Comparator<Integer[]>(){
			public int compare(Integer[] a,Integer[] b){
				return a[0].compareTo(b[0]);
			}
		});
		for(;;){
			L=n();P=n();
			String g=s.next(),m="WAIT";
			if(L>-1){
				T=L<F-1?d[L][1]:X;
				m=T<P&g.equals("RIGHT")|T>P&g.equals("LEFT")?"BLOCK":m;
			}
			System.out.println(m);
		}
	}
	static int n(){return s.nextInt();}
}