package nl.sogyo.mancala.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {

	@Test
	public void does_my_newPlayer_have_name_Player1()
	{
		Player player = new Player();
		var name = player.getMyName();
		assertEquals("Player1", name);
	}
	@Test
	public void does_my_newPlayer_have_Opponent_with_name_Player2()
	{
		Player player = new Player();
		Player player2  = player.getMyOpponent();
		var name = player2.getMyName();
		assertEquals("Player2", name);
	}
	@Test
	public void is_turn_firstplayer_true() {
		Player player = new Player();
		var myTurn = player.isMyTurn();
		assertEquals(true, myTurn);
	}
	@Test
	public void is_turn_secondplayer_false() {
		Player player = new Player();
		Player player2 = player.getMyOpponent();
		var myTurn = player2.isMyTurn();
		assertEquals(false, myTurn);
	}
	@Test
	public void switchPlayerturn()
	{
		Player player = new Player();
		player.switchPlayer();
		var myTurn = player.isMyTurn();
		assertEquals(false, myTurn);
		assertEquals(true, player.getMyOpponent().isMyTurn());
	}


}
