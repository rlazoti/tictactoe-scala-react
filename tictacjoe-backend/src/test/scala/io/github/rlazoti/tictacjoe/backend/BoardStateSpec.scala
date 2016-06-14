package io.github.rlazoti.tictacjoe.backend

import org.scalatest._

class BoardStateSpec extends FunSuite with Matchers {

  private implicit val settings = GameSettings()
  private val emptyRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, settings.emptyPositionValue)
  private val opponent = Computer()
  private val user = User("Rafael")
  private val initialBoardState = InitialBoardState()

  test("All positions of Board's initial state should be blank") {
    initialBoardState.blanks should be (settings.boardWidth * settings.boardWidth)
    initialBoardState.positions.map { row =>
      row should contain theSameElementsAs emptyRow
    }
  }

  test("Board's initial state should not define the last move's player") {
    initialBoardState.lastMovePlayer shouldBe None
  }

  test("Board's initial state should not define the game as over") {
    initialBoardState.over(user, opponent) should be (false)
  }

  test("An user's move should be registered into Board's state") {
    val state = NextBoardState(initialBoardState, Move(opponent, Position(0, 1)))
    val expectedRow = Array(settings.emptyPositionValue, Computer().mark, settings.emptyPositionValue)

    state.blanks should be (initialBoardState.blanks - 1)
    state.lastMovePlayer shouldBe Some(opponent)
    state.positions(0) should contain theSameElementsInOrderAs expectedRow
    state.positions(1) should contain theSameElementsInOrderAs emptyRow
    state.positions(2) should contain theSameElementsInOrderAs emptyRow
  }

  test("Three users' moves should be registered into Board's state") {
    val firstMoveState = NextBoardState(initialBoardState, Move(opponent, Position(1, 1)))
    val secondMoveState = NextBoardState(firstMoveState, Move(user, Position(2, 2)))
    val thirdMoveState = NextBoardState(secondMoveState, Move(opponent, Position(0, 0)))

    val expectedFirstRow = Array(opponent.mark, settings.emptyPositionValue, settings.emptyPositionValue)
    val expectedSecondRow = Array(settings.emptyPositionValue, opponent.mark, settings.emptyPositionValue)
    val expectedThirdRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, user.mark)

    thirdMoveState.blanks should be (initialBoardState.blanks - 3)
    thirdMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
    thirdMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
    thirdMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow
  }

  test("The same user cannot send more than one move in a row") {
    val firstMoveState = NextBoardState(initialBoardState, Move(user, Position(1, 1)))
    an [IllegalArgumentException] should be thrownBy NextBoardState(firstMoveState, Move(user, Position(2, 2)))
  }

  test("A draw game state should have all positions defined") {
    val firstMoveState = NextBoardState(initialBoardState, Move(opponent, Position(1, 1)))
    val secondMoveState = NextBoardState(firstMoveState, Move(user, Position(2, 2)))
    val thirdMoveState = NextBoardState(secondMoveState, Move(opponent, Position(0, 0)))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(user, Position(2, 0)))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(opponent, Position(2, 1)))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(user, Position(0, 1)))
    val seventhMoveState = NextBoardState(sixthMoveState, Move(opponent, Position(0, 2)))
    val eighthMoveState = NextBoardState(seventhMoveState, Move(user, Position(1, 0)))
    val ninthMoveState = NextBoardState(eighthMoveState, Move(opponent, Position(1, 2)))

    val expectedFirstRow = Array(opponent.mark, user.mark, opponent.mark)
    val expectedSecondRow = Array(user.mark, opponent.mark, opponent.mark)
    val expectedThirdRow = Array(user.mark, opponent.mark, user.mark)

    ninthMoveState.blanks shouldBe 0
    ninthMoveState.lastMovePlayer shouldBe Some(opponent)

    ninthMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
    ninthMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
    ninthMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow

    ninthMoveState.won(user) shouldBe false
    ninthMoveState.won(opponent) shouldBe false
    ninthMoveState.over(user, opponent) shouldBe true
    ninthMoveState.draw(user, opponent) shouldBe true
  }

  test("A draw board game should not accept new moves") {
    val firstMoveState = NextBoardState(initialBoardState, Move(opponent, Position(1, 1)))
    val secondMoveState = NextBoardState(firstMoveState, Move(user, Position(2, 2)))
    val thirdMoveState = NextBoardState(secondMoveState, Move(opponent, Position(0, 0)))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(user, Position(2, 0)))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(opponent, Position(2, 1)))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(user, Position(0, 1)))
    val seventhMoveState = NextBoardState(sixthMoveState, Move(opponent, Position(0, 2)))
    val eighthMoveState = NextBoardState(seventhMoveState, Move(user, Position(1, 0)))
    val ninthMoveState = NextBoardState(eighthMoveState, Move(opponent, Position(1, 2)))

    an [UnsupportedOperationException] should be thrownBy NextBoardState(ninthMoveState, Move(user, Position(1, 1)))
  }

}
