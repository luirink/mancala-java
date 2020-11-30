package nl.sogyo.mancala.domain;

abstract class Container {
	private Player owner;
	private int numStones;
	private Container nextContainer;
	private int numPits;
		
	public int getStones() {
		return numStones;
	}
	
	public void setStones(int numStonesToSet) {
		this.numStones=numStonesToSet;
	}
	
	public void addStone(int i) {
		this.numStones+=i;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setOwnerNew() {
		this.owner = new Player();
	}

	public void setOwner(Player player) {
		this.owner = player;
	}
	
	public void setnextContainer(Pit firstPit) {
		this.nextContainer = firstPit;
	}
	
	public void setnextContainer(Player player, int numPits, int numtogo, boolean firstPlayer, Pit firstPit) {
		this.nextContainer = new Pit(player, numPits, numtogo, firstPlayer, firstPit);
	}
	
	public void setnextContainer(Player player, int numPits, boolean firstPlayer, Pit firstPit) {
		this.nextContainer = new Kalaha(player, numPits, firstPlayer, firstPit);
	}
	public Container getnextContainer() {
		return nextContainer;
	}
	
	public Container getnextContainer(int numcontainersaway){
		if (numcontainersaway>1) {
			return nextContainer.getnextContainer((numcontainersaway-1 ));
		}
		return nextContainer;
	}
	
	public void setNumPits(int numPits) {
    	this.numPits=numPits;
    }
	
    public int getNumPits() {
    	return this.numPits;
    }
	
	public boolean isMyKalaha() {
		if (playerIsOwner()) {
			return (this instanceof Kalaha);
		}
		else { 
			return false;
		}
	}
	
	public boolean playerIsOwner() {
		return owner.isMyTurn();
	}
	
	public void checkIfEndGame() {
		//get the two first pits of the players
		//check for both if all pits are empty
		this.getMyKalaha().getnextContainer().checkChainEmptyPits();
		this.getnextContainer(getNumPits()+1).getMyKalaha().getnextContainer().checkChainEmptyPits();
	}
	
    public void calculateWinner() {
    	//calculatewinner is started from the kalaha after an empty chain
    	//the next container is the start of a row. Calculate the total stones of that player.
    	//check if is more equal, more or less than half of all stones
    	//set winner accordingly
    	//when a tie, both are designated as winner
    	int total1 = this.getnextContainer().calculateTotalStones();
    	if (total1 == ((this.getNumPits()*2*4)/2)) {
    		//It's A TIE!
    		this.getOwner().setWinner();
    		this.getOwner().getMyOpponent().setWinner();
    	}
    	else if (total1 >  ((this.getNumPits()*2*4)/2)) {
    		this.getnextContainer().getOwner().setWinner();
    	}
    	else {
    		this.getOwner().setWinner();
    	}
    	
    	
    }

	public abstract void stealFromOpponent();
    public abstract void playPit(int pitNumAway);
    public abstract Container getOpposite();
    public abstract int numToMyKalaha();
    public abstract boolean isKalaha();
    public abstract void lastContainer();
    public abstract Container getMyKalaha();
    public abstract void passStones(int stones);
    public abstract void checkChainEmptyPits();
    public abstract boolean isEmptyPit();
    public abstract int calculateTotalStones();
}

class Pit extends Container {
	
	public Pit (int numPits)
	{
		this.setNumPits(numPits);
		this.setStones(4);
		this.setOwnerNew();
		boolean firstPlayer= true;
		this.setnextContainer(this.getOwner(), this.getNumPits(), (this.getNumPits()-1), firstPlayer, this);
	}

	public Pit(Player player, int numPits, int numtogo, boolean firstPlayer, Pit firstPit) {
		this.setStones(4);
		this.setNumPits(numPits);
		this.setOwner(player);
		if (numtogo > 1) {
			//make pits
			this.setnextContainer(player, numPits, (numtogo-1), firstPlayer, firstPit);
		}
		else {
			//make kalaha
			this.setnextContainer(player, numPits, firstPlayer, firstPit);
		}
	}
	public void playPit(int pitNumAway) {
		if (pitNumAway == 0) {
			if (this.isEmptyPit()==false && this.playerIsOwner()) {
				int numStonesNow = this.getStones();
				this.setStones(0);
				this.getnextContainer().passStones(numStonesNow);
			}
			else {
				//THIS SHOULD BE CHANGED TO LOGGER MESSAGE, and should result in re-asking input... so maybe throw exception
				System.out.println("--- You can't play this bowl, choose another! ---");
			}
		}
		else {
			this.getnextContainer().playPit((pitNumAway-1));
		}
	}
	
	public Container getOpposite() {
		Container oppositeContainer = this.getnextContainer((this.numToMyKalaha()*2));
		return oppositeContainer;
	}
	
	public int numToMyKalaha() {		
		return (this.getnextContainer().numToMyKalaha()) +1;
	}
	
	public void stealFromOpponent() {
		//empty the opposite pit and its own and move to own kalaha
		int totalStones = this.getStones() + this.getOpposite().getStones();
		this.getMyKalaha().addStone(totalStones);
		this.setStones(0);
		this.getOpposite().setStones(0);
		this.getOwner().switchPlayer();
		this.checkIfEndGame();
	}
	
	public boolean isKalaha() {
		return false;
	}
	
	public Container getMyKalaha() {
		return this.getnextContainer().getMyKalaha();
    }
	
	public void lastContainer() {
		//if the pit is the last pit of a turn, check if it was the players pit, if it was empty before, and if there are stones in opposite pit
		//if so, steal from opponent
		//if not, switch player and check if it is the end of the game.
		if (this.playerIsOwner() && this.getStones()==1 && this.getOpposite().getStones() >0 ){
				this.stealFromOpponent();
		}
		else{			
			this.getOwner().switchPlayer();
			this.checkIfEndGame();
		}
	}
	
	public void passStones(int stones) {
		this.addStone(1);
		if (stones>1) {
			this.getnextContainer().passStones((stones-1));
		}
		else {   
			this.lastContainer();
		}
	}	
		
	public void checkChainEmptyPits(){
		//start from first Pit of player, and check if all pits of player are empty
		if (this.isEmptyPit()){
			this.getnextContainer().checkChainEmptyPits();			
		}
		
	}
	public boolean isEmptyPit() {
		if (this.getStones()==0) {
			return true;
		}
		return false;
	}
	
	public int calculateTotalStones() {
		//calculate the total number of stones from a player (start with the first Pit of a player!)
		return this.getStones() + this.getnextContainer().calculateTotalStones() ;
	}
}

class Kalaha extends Container {
	
	public Kalaha(Player player, int numPits, boolean firstPlayer, Pit firstPit) {
		this.setStones(0);
		this.setOwner(player);
		this.setNumPits(numPits);
		if (firstPlayer==true) {
			Player opponent = player.getMyOpponent();
			this.setnextContainer(opponent, numPits, numPits, false, firstPit);
		}
		else {
			this.setnextContainer(firstPit);
		}
	}
	
	public void passStones(int stones) {
		//if the player is the owner of the kalaha, stones can be added
		if (this.playerIsOwner()) {
			this.addStone(1);
			if (stones>1) {
				this.getnextContainer().passStones((stones-1));
			}
			else if (stones==1) {
				this.lastContainer();
			}
		}
		else {
			this.getnextContainer().passStones((stones));
		}
	}
	public int numToMyKalaha() {
		//arrived at kalaha, zo return 0
		return 0;
	}

	public boolean isKalaha() {
		return true;
	}
	
	public void stealFromOpponent(){
    }
	

	public void playPit(int pitNumAway){
		//the kalaha itself can't be played
		if (pitNumAway == 0) {
			//THIS SHOULD BE CHANGED TO LOGGER MESSAGE, and should result in re-asking input... so maybe throw exception
			System.out.println("--- You can't play this bowl, choose another! ---"); 
		}
		else {
				this.getnextContainer().playPit((pitNumAway-1));
			}
		}
    
    public Container getMyKalaha() {
    	//as this is called from a Pit or kalaha of a certain owner, this kalaha can be returned without checking the owner again
    	return this;
    }
    public Container getOpposite(){
    	//never needed, in theory, but it gives the opposite kalaha
        return this.getnextContainer(this.getNumPits()+1);
    }
    
    public void lastContainer() {
    	//check if it is the end of the game, do not switch player
    	checkIfEndGame();
	}
    
    public void checkChainEmptyPits(){
		//arrived here, means that the chain was empty, define a winner
    	//calculate winner
    	this.calculateWinner();
	}
    
    public boolean isEmptyPit() {
    	//even if it is empty, it is not a pit, so return false
		return false;
	}
    
    public int calculateTotalStones() {
    	//the final container is the kalaha, so return only it's stones
    	return this.getStones();
    }
}


