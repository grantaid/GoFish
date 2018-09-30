public class Game{
	public Game(int numPlayers){
		this.numPlayers=numPlayers;
		size=numPlayers%4==0?numPlayers:(numPlayers/4+1)*4;
		dealer = new int[13*size][2];
		for(int temp=0;temp<dealer.length;temp++){
			dealer[temp][0]=temp%13;
			dealer[temp][1]=temp%4;
		}
		dealerCounter=13*size;
		ace = new Card(1,"ace");
		two = new Card(2,"2");
		three = new Card(3,"3");
		four = new Card(4,"4");
		five = new Card(5,"5");
		six = new Card(6,"6");
		seven = new Card(7,"7");
		eight = new Card(8,"8");
		nine = new Card(9,"9");
		ten = new Card(10,"10");
		jack = new Card(11,"jack");
		queen = new Card(12,"queen");
		king = new Card(13,"king");
		players = new Player[numPlayers];
		for(int temp=0;temp<numPlayers;temp++){
			players[temp]=new Player(names[temp]);
			for(int i=0;i<7;i++){
				drawCard(players[temp]);
			}
		}
	}
	private Card ace, two, three, four, five, six, seven, eight, nine, ten, jack, queen, king;
	private String[] names = {"Basil", "Tundi", "Junior", "Madison","Zen","Yunquan","Max","Nathanial","Tony","Jon","Jane","Myron","Spencer",
	"Garfield","Ahmed","Mike","Tatiana","Darnel","Bing","Mashood","Makseen","Karen","Arlette","Lyle"};
	private Player[] players;
	private int numPlayers;
	private int[][] dealer;
	private int size;
	private int dealerCounter;
	public void round(){
		for(int i=0;i<numPlayers;i++){
			int[] sizeTarget = new int[numPlayers];
			for(int g=0;g<numPlayers;g++){
				sizeTarget[g]=g;
			}
			boolean win = false;
			boolean tried = false;
			System.out.print(players[i].name+"'s turn: ");
			if(players[i].handCounter==0){
				drawCard(players[i]);
				if(players[i].handCounter==0){
					System.out.println("passes.");
				}
			}
			if(players[i].handCounter>0){
				//find random card to target first
				int j=(int)(Math.random()*players[i].handCounter);
				//loop through cards until valid memory (10) found
				int o=j;
				for(int k=j;j<o+players[i].handCounter && !tried;k++,j++){
					if(k==players[i].handCounter){
						k=0;
					}
					//find random memory (10) of a player to target first
					int m=(int)(Math.random()*numPlayers);
					int n=m;
					//loop through memory for valid memory (10) (not self) (size>0)
					for(int h=m;m<n+numPlayers && !tried;h++,m++){
						if(h==numPlayers){
							h=0;
						}
						if(((int)(players[i].hand[k].memory[i][h]/10)==1 && h!=i) && players[h].handCounter>0){
							//search opponent's hand
							win=searchTarget(i,h,k);
							tried=true;
							//bad guess
							if(!win){
								players[i].hand[k].learnFail(i,h);
								drawCard(players[i]);
							}
						}
					}
				}
				//loop through cards (from start!) until valid memory (!20) found (not self)
				for(int k=0;k<players[i].handCounter && !tried;k++){
					//find largest hand size to target sequentially
					for(int m=0;m<numPlayers-1;m++){
						int largest=m;
						for(int n=m;n<numPlayers;n++){
							if(players[sizeTarget[n]].handCounter>players[sizeTarget[largest]].handCounter){
								largest=n;
							}
						}
						int temp=sizeTarget[m];
						sizeTarget[m]=sizeTarget[largest];
						sizeTarget[largest]=temp;
					}
					//loop through memory for valid target (!20) (>0 cards) (!self)
					for(int h=0;h<numPlayers && !tried;h++){
						if(((int)(players[i].hand[k].memory[i][sizeTarget[h]]/10)!=2 && i!=sizeTarget[h])
						&& players[sizeTarget[h]].handCounter>0){
							win=searchTarget(i,sizeTarget[h],k);
							tried=true;
							if(!win){
								players[i].hand[k].learnFail(i,sizeTarget[h]);
								drawCard(players[i]);
							}
						}
					}
				}
				//Ask about first card (k=0)
				//find random memory of a player to target
				if(!tried){
					int m=(int)(Math.random()*numPlayers);
					if(m==i){
						if(m==(numPlayers-1))
							m--;
						else
							m++;
					}
					win=searchTarget(i,m,0);
					if(!win){
						players[i].hand[0].learnFail(i,m);
						drawCard(players[i]);
					}
				}
			}
			if(win)
				i--;
		}
		for(int i=0;i<numPlayers;i++){
			System.out.println(players[i].name+" has "+players[i].handCounter+
			" cards and "+players[i].pairs+" pairs.");
		}
	}
	public boolean searchTarget(int i, int h, int k){
		//search opponent's hand
		for(int l=0;l<players[h].handCounter;l++){
			//good guess
			if(players[h].hand[l].face==players[i].hand[k].face){
				System.out.println(players[i].name+" received a "+players[h].hand[l].face+" from "+
				players[h].name+".");
				for(int z=0;z<numPlayers;z++){
					for(int y=0;y<numPlayers;y++){
						if(y==i || y==h){
							players[h].hand[l].memory[z][y]=20;
						}
					}
				}
				players[h].removeCard(l);
				players[i].removeCard(k);
				players[i].pairs++;
				return true;
			}
		}
		System.out.println(players[i].name+" did not get a "+players[i].hand[k].face+" from "+
		players[h].name+".");
		return false;
	}
	public void drawCard(Player in){
		if(dealerCounter==0)
			return;
		int give = (int)(Math.random()*dealerCounter);
		Card choice = ace;
		switch(dealer[give][0]){
			case 1: choice = ace;
				break;
			case 2: choice = two;
				break;
			case 3: choice = three;
				break;
			case 4: choice = four;
				break;
			case 5: choice = five;
				break;
			case 6: choice = six;
				break;
			case 7: choice = seven;
				break;
			case 8: choice = eight;
				break;
			case 9: choice = nine;
				break;
			case 10: choice = ten;
				break;
			case 11: choice = jack;
				break;
			case 12: choice = queen;
				break;
			case 13: choice = king;
				break;
		}
		if(in.addCard(choice,dealer[give][1])){
			System.out.println(in.name+" found a pair.");
		}
		dealer[give][0]=dealer[dealerCounter-1][0];
		dealer[give][1]=dealer[--dealerCounter][1];
	}
	private class Card{
		public int[][] memory;
		public int face;
		public String name;
		public Card(int face, String name){
			memory = new int[numPlayers][numPlayers];
			this.face=face;
			this.name=name;
		}
		public void ageMemory(){
			for(int i=0;i<memory.length;i++){
				for(int j=0;j<memory[0].length;j++){
					if(memory[i][j]!=0){
						memory[i][j]++;
						if(memory[i][j]%10>8){
							memory[i][j]=0;
						}
					}
				}
			}
		}
		public void learnYes(int learner, int target){
			memory[learner][target]=10;
		}
		public void learnFail(int target1, int target2){
			for(int z=0;z<numPlayers;z++){
				for(int y=0;y<numPlayers;y++){
					if(y==target1){
						memory[z][y]=10;
					}
					if(y==target2){
						memory[z][y]=20;
					}
				}
			}
		}
	}
	private class Player{
		public Player(String name){
			hand = new Card[13];
			handSuit = new int[13];
			handCounter=0;
			this.name=name;
			pairs=0;
		}
		String name;
		Card[] hand;
		int[] handSuit;
		int handCounter;
		int pairs;
		public boolean addCard(Card in, int suit){
			for(int i=0;i<hand.length; i++){
				if(i==handCounter){
					hand[i]=in;
					handCounter++;
					return false;
				}
				if(hand[i].face==in.face){
					removeCard(i);
					pairs++;
					return true;
				}
				if(in.face<hand[i].face){
					for(int j=handCounter;j>i;j--){
						hand[j]=hand[j-1];
					}
					hand[i]=in;
					handCounter++;
					return false;
				}
			}
			System.out.println("error");
			return false;
		}
		public void removeCard(int place){
			if(place>=handCounter)
				System.out.println("error removing card");
			while(place+1<handCounter){
				hand[place]=hand[place+1];
				handSuit[place]=handSuit[place+1];
				place++;
			}
			handCounter--;
		}
	}
}
