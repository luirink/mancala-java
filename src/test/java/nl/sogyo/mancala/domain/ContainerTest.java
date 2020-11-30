package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContainerTest {
	@Test
	public void createPit_4_stones()
	//check if new pit has 4 stones
	{		
		Pit Pit = new Pit(6);
		int numStones = Pit.getStones();
		assertEquals(4,numStones);
	}

	@Test
	public void createNeighborPit_4_stones()
	//check if new pit has a neighbor, which also has 4 stones
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer();
		int numStones = nextPit.getStones();
		assertEquals(4,numStones);
	}
	
	@Test
	public void createMyKalaha()
	//check if kalaha is created at right spot with right owner
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer(6);
		boolean isMyKalaha = nextPit.isMyKalaha();
		assertEquals(true, isMyKalaha);
	}
	
	public void createMyKalahabiggerBoard()
	//check if kalaha is created at right spot with right owner
	{
		Pit Pit = new Pit(12);
		Container nextPit = Pit.getnextContainer(12);
		boolean isMyKalaha = nextPit.isMyKalaha();
		assertEquals(true, isMyKalaha);
	}
	
	@Test
	public void createKalaha_0_stones()
	//check if kalaha has 0 stones at start
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer(6);
		int numStones = nextPit.getStones();
		assertEquals(0,numStones);
	}
	
	@Test
	
	public void getOpposite()
	//check if opposite pit is correctly set
	{
		Pit Pit = new Pit(6);
		assertEquals(Pit.getnextContainer(12), Pit.getOpposite());
		assertEquals(Pit.getnextContainer(10), Pit.getnextContainer(2).getOpposite());
	}
	
	@Test
	public void getnextContainer_13_isKalaha_Opponent()
	//check if kalaha of opponent has correct owner
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer(13);
		assertEquals(true,nextPit.isKalaha());
		assertEquals(Pit.getOwner().getMyOpponent(), nextPit.getOwner());
	}
	
	@Test
	public void getnextContainerint_14_give_self()
	//check if getnextContainer(int 14) gives self
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer(14);
		assertEquals(Pit,nextPit);
	}
	
	@Test
	//Not proper test driven development, rethink on how to do this 
	public void getnextContainer3times_worksame_withint3()
	//check if getnextContainer() 3 times works the same as getnextContainer(3)
	{
		Pit Pit = new Pit(6);
		Container nextPit = Pit.getnextContainer();
		Container nextnextPit = nextPit.getnextContainer();
		Container nextnextnextPit = nextnextPit.getnextContainer();
		Container Pit3Neighbor = Pit.getnextContainer(3);
		assertEquals(nextnextnextPit,Pit3Neighbor);
	}
	
	@Test
	//Not proper test driven development, rethink on how to do this 
	public void does_addStone_1_add_1_stone()
	//check if a stone is added to next Pit with addStone function
	{
		Pit Pit = new Pit(6);
		Pit.addStone(1);
		int numStones = Pit.getStones();
		assertEquals(5, numStones);
	}
	
	@Test
	public void makeMove_on_firstPit_and_EmptyPit() 
	//make a move on first pit, and see if it is empty after the move
	{
		Pit Pit = new Pit(6);
		Pit.playPit(0);
		int numStones = Pit.getStones();
		assertEquals(0, numStones);
	}

	@Test
	public void makeMove_on_secondPit_and_EmptyPit() 
	//make a move on second pit and empty it
	{
		Pit Pit = new Pit(6);
		Pit.playPit(1);
		int numStones = Pit.getnextContainer().getStones();
		assertEquals(0, numStones);
	}
	
	@Test
	public void makeMove_on_Pit_1stone_givetonextPit()
	//check if when a move is made the stones are passed to next container same owner
	{
		Pit firstPit = new Pit(6);
		var nextPit = firstPit.getnextContainer();
		var nextnextPit = nextPit.getnextContainer();
		int numStones = nextnextPit.getStones();
		nextPit.setStones(1);
		firstPit.playPit(1);
		assertEquals((numStones+1), nextnextPit.getStones() );
	}
	
	@Test
	public void makeMove_on_Pit_passKalaha()
	//check if when a move is made the stones are passed to next container same owner
	{
		Pit firstPit = new Pit(6);
		int numStones = firstPit.getnextContainer(7).getStones();
		firstPit.setStones(10);
		firstPit.playPit(0);
		assertEquals((numStones+1), firstPit.getnextContainer(7).getStones() );
	}

	@Test
	public void getMyKalaha_givesownKalaha()
	{
		Pit firstPit = new Pit(6);
		assertEquals(firstPit.getnextContainer(6), firstPit.getMyKalaha());
	}
	
	
	@Test
	public void lastContainer_isEmptyPit_stealFromOpponent()
	//end in own last pit
	{
		Pit firstPit = new Pit(6);
		firstPit.setStones(1);
		firstPit.getnextContainer().setStones(0);
		firstPit.playPit(0);
		
		assertEquals(0, firstPit.getStones() );
		assertEquals(0, firstPit.getnextContainer().getStones() );
		assertEquals(true, firstPit.getOwner().getMyOpponent().isMyTurn());
		assertEquals(5, firstPit.getMyKalaha().getStones() );

	}

	@Test
	public void makeMove_on_opponentPit() 
	//should be unable to make a move on opponents pit
	{
		Pit Pit = new Pit(6);
		Pit.playPit(10);
		int numStones = Pit.getStones();
		assertEquals(4, numStones);
	}

	
	@Test
	public void makeMove_on_Pit_passKalaha_endinemptyOpponent() 
	//ending in empty pit of opponent should not lead to stealing
	{
		Pit Pit = new Pit(6);
		Pit.setStones(10);
		Pit.getnextContainer(10).setStones(0);
		Pit.playPit(0);
		assertEquals(1, Pit.getnextContainer(10).getStones());
		assertEquals(1, Pit.getMyKalaha().getStones());
	}
	
	@Test
	public void makeMove_on_Pit_passbothKalahas() 
	//pass both kalahas
	{
		Pit Pit = new Pit(6);
		Pit.setStones(14);
		Pit.playPit(0);
		assertEquals(1, Pit.getStones());
		assertEquals(1, Pit.getMyKalaha().getStones());
		assertEquals(0, Pit.getnextContainer(8).getMyKalaha().getStones());
	}
	
	@Test
	public void makeMove_on_Pit_end_in_own_Kalaha() 
	//end in own kalaha, means player should not be switched
	{
		Pit Pit = new Pit(6);
		Pit.playPit(2);
		assertEquals(0, Pit.getnextContainer(2).getStones());
		assertEquals(1, Pit.getMyKalaha().getStones());
		assertEquals(true, Pit.getOwner().isMyTurn());
	}
	
	
	@Test
	public void assertEndGameWinnerplayer1()
	{
		Pit firstPit = new Pit(6);
		firstPit.setStones(0);
		firstPit.getnextContainer().setStones(0);
		firstPit.getnextContainer(2).setStones(0);
		firstPit.getnextContainer(3).setStones(0);
		firstPit.getnextContainer(4).setStones(0);
		firstPit.getnextContainer(7).setStones(0);
		firstPit.getnextContainer(8).setStones(0);
		firstPit.getnextContainer(9).setStones(0);
		firstPit.getnextContainer(10).setStones(0);
		firstPit.getMyKalaha().setStones(36);
		firstPit.playPit(5);
		assertEquals(true, firstPit.getOwner().isWinner());
	}
	
	@Test
	public void assertEndGameWinnerplayer2()
	{
		Pit firstPit = new Pit(6);
		firstPit.setStones(14);
		firstPit.getnextContainer().setStones(0);
		firstPit.getnextContainer(2).setStones(0);
		firstPit.getnextContainer(3).setStones(0);
		firstPit.getnextContainer(4).setStones(0);
		firstPit.getnextContainer(5).setStones(0);
		firstPit.getnextContainer(7).setStones(0);
		firstPit.getnextContainer(8).setStones(0);
		firstPit.getnextContainer(9).setStones(0);
		firstPit.getnextContainer(10).setStones(0);
		firstPit.getnextContainer(11).setStones(0);
		firstPit.getnextContainer(7).getMyKalaha().setStones(30);
		firstPit.getOwner().switchPlayer();
		firstPit.playPit(12);
		assertEquals(true, firstPit.getnextContainer(7).getOwner().isWinner());
	}
	


	


}
