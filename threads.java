import java.io.*;


class Diff implements Runnable {
	
	int m,n,dif;
	Diff(int f,int g)
	{
		this.m=f;
		this.n=g;
	}
	public void run()
	{
		try {
			dif = n-m;
			System.out.println(
				"Difference D" + Thread.currentThread().getId()
				+ " is running");
			System.out.println("difference is :" + dif);
		}
		catch (ArithmeticException e) {
			// Throwing an exception
			System.out.println("Error value");
		}
	}
}


class threads implements Runnable {
	
	int l,i,sum;
	threads(int j,int k)
	{
		this.l=j;
		this.i=k;
	}
	public void run()
	{
		try {
			sum = l+i;
			System.out.println(
				"Sum S" + Thread.currentThread().getId()
				+ " is running");
			System.out.println("sum is :" + sum);
		}
		catch (ArithmeticException e) {
			// Throwing an exception
			System.out.println("Error value");
		}
	}
	
	public static void main(String args[])
	{
		int a=10,b=20,c;
		threads t = new threads(a,b);
		Thread t1 = new Thread(t);
		Diff di = new Diff(a,b);
		Thread t2 = new Thread(di);
		t1.start();
		t2.start();
	}
}
