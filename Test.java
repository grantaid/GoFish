public class Test{
	public void run(){
		One test1 = new One(5);
		Two test2 = new Two(3+test1.num);
		System.out.println(test1.num+"\n"+test2.num);
	}

	private class One{
		public One(int in){
			num=in;
		}
		int num;
	}
	private class Two{
		public Two(int in){
			num = in;
		}
		int num;
	}
}
