package nl.sogyo.mancala.domain;
class Player {
	boolean myTurn;
	boolean isWinner=false;
	Player myOpponent;
	String myName;
	
	public Player() {
		myTurn= true;
		myOpponent= new Player(this);
		myName="Player1";
	}
	
	public Player(Player opponent) {
		this.myTurn=false;
		this.myOpponent=opponent;
		this.myName="Player2";
	}
	
	public Player getMyOpponent() {
		return myOpponent;
	}
	
	public boolean isMyTurn() {
		return myTurn;
	}
	
	public String getMyName() {
		return myName;
	}
	
	public void switchPlayer() {
		myTurn= !myTurn;
		switchPlayer(myOpponent);
	}
	
	public void switchPlayer(Player player) {
		player.myTurn = !player.myTurn;
	}

	public void choosePit (Pit firstPit, int pitNumAway) {
		firstPit.playPit(pitNumAway);
	}

	public void setWinner() {
		this.isWinner =  true;
	}
	
	public boolean isWinner() {
		return isWinner;
	}
}