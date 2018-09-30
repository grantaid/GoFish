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
		ace = new Card(1);
		two = new Card(2);
		three = new Card(3);
		four = new Card(4);
		five = new Card(5);
		six = new Card(6);
		seven = new Card(7);
		eight = new Card(8);
		nine = new Card(9);
		ten = new Card(10);
		jack = new Card(11);
		queen = new Card(12);
		king = new Card(13);
		players = new Player[numPlayers];
		for(int temp=0;temp<numPlayers;temp++){
			players[temp]=new Player(names[temp]);
			for(int i=0;i<7;i++){
				drawCard(players[temp]);
			}
		}
	}
	private Card ace, two, three, four, five, six, seven, eight, nine, ten, jack, queen, king;
	private String[] names = {"Basil", "Tundi", "Junior", "Rick"};
	private Player[] players;
	private int numPlayers;
	private int[][] dealer;
	private int size;
	private int dealerCounter;
	public void round(){
		for(int i=0;i<numPlayers;i++){
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
					//find random memory of a player to target first
					int m=(int)(Math.random()*numPlayers);
					int n=m;
					//loop through memory for valid memory (10) (not self)
					for(int h=m;m<n+numPlayers && !tried;h++,m++){
						if(h==numPlayers){
							h=0;
						}
						if((int)(players[i].hand[k].memory[i][h]/10)==1 && h!=i){
							//search opponent's hand
							win=searchTarget(i,h,k);
							tried=true;
							//bad guess
							if(!win){
								players[i].hand[k].learnFail(i,h);
							}
						}
					}
				}
				j=o;
				//loop through cards until valid memory (!20) found (not self)
				for(int k=j;j<o+players[i].handCounter && !tried;k++,j++){
					if(k==players[i].handCounter){
						k=0;
					}
					//find random memory of a player to target first
					int m=(int)(Math.random()*numPlayers);
					int n=m;
					//loop through memory for valid target (!20)
					for(int h=m;m<n+numPlayers && !tried;h++,m++){
						if(h==numPlayers)
							h=0;
						if((int)(players[i].hand[k].memory[i][h]/10)!=2 && h!=i){
							win=searchTarget(i,h,k);
							tried=true;
							if(!win){
								players[i].hand[k].learnFail(i,h);
							}
						}
					}
				}
				//Ask about first card (k=0)
				//find random memory of a player to target
				if(!tried){
					int m=(int)(Math.random()*numPlayers);
					if(m==i){
						if(m==(numPlayers-1)){
							m--;
						}
						else{
							m++;
						}
					}
					win=searchTarget(i,m,0);
					if(!win){
						players[i].hand[0].learnFail(i,m);
					}
				}
			}
			if(win)
				i--;
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
		public Card(int face){
			memory = new int[numPlayers][numPlayers];
			this.face=face;
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
