package com.sa.main;

import java.util.*;

public class AUC2{
	static ArrayList<Integer> ans = new ArrayList<Integer>();
	static ArrayList<Double> results = new ArrayList<Double>();
	static ArrayList<Integer> order = new ArrayList<Integer>();
	static int N = 0;
	

	static double auc(){
		int t = 0, f = 0, pt = 0, pf = 0;
		double x, y, px=0, py=0,ret=0;
		for(int i = 0; i<N; i++){
			t += ans.get(i);
			f += 1-ans.get(i);
		}
		for(int i = 0; i<N; i++){
			if(ans.get(order.get(i)) == 1){
				pt++;
			}else{
				pf++;
			}
			if(i == N-1 || results.get(order.get(i)).doubleValue() != results.get(order.get(i+1)).doubleValue()){
				x = (double)pf / f;
				y = (double)pt / t;
				ret += (px + x) * (y - py) / 2;
				px = x;
				py = y;
			}
		}
		return ret;
	}
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextInt()){
			ans.add(sc.nextInt());
			results.add(sc.nextDouble());
			order.add(N++);
		}
		Collections.sort(order,new Comparator<Integer>(){
			public int compare(Integer a, Integer b){
				double d = results.get(a);
				double e = results.get(b);
				if(d < e) return -1;
				else if(d > e) return 1;
				else return 0;
			}
		});
		System.out.printf("AUC = %f\n",auc());
	}
	
}


