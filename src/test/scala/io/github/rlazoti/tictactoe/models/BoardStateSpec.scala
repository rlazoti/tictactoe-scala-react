package io.github.rlazoti.tictactoe.models

import org.scalatest._

class BoardStateSpec extends FunSuite with Matchers {

  private val settings = GameSettings(Easy("easy"))
  private val emptyRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, settings.emptyPositionValue)
  private val opponent = Computer(Nought())
  private val user = User(Cross())
  private val initialBoardState = InitialBoardState(settings, user, opponent)

  test("All positions of Board's initial state should be blank") {
    initialBoardState.blanks should be (settings.boardWidth * settings.boardWidth)
    initialBoardState.positions.foreach { row =>
      row should contain theSameElementsAs emptyRow
    }
  }

  test("Board's initial state should have a player and its opponent") {
    initialBoardState.player shouldBe user
    initialBoardState.opponentPlayer shouldBe opponent
  }

  test("Board's initial state should not define the game as over") {
    initialBoardState.over() shouldBe false
  }

  test("Board's initial state should not define the game as draw") {
    initialBoardState.draw() shouldBe false
  }

  test("Board's initial state should not accept that user and opponent use the same piece") {
    an [IllegalArgumentException] should be thrownBy
    InitialBoardState(settings, User(Cross()), Computer(Cross()))

    an [IllegalArgumentException] should be thrownBy
    InitialBoardState(settings, User(Nought()), Computer(Nought()))
  }

  test("An opponent's move should be registered into a new Board's state") {
    val state = NextBoardState(initialBoardState, Move(0, 1))
    val expectedRow = Array(settings.emptyPositionValue, opponent.getPiece, settings.emptyPositionValue)

    state.player shouldBe opponent
    state.opponentPlayer shouldBe user
    state.blanks should be (initialBoardState.blanks - 1)
    state.positions(0) should contain theSameElementsInOrderAs expectedRow
    state.positions(1) should contain theSameElementsInOrderAs emptyRow
    state.positions(2) should contain theSameElementsInOrderAs emptyRow
  }

  test("Three moves should be registered into a new Board's state") {
    val firstMoveState = NextBoardState(initialBoardState, Move(1, 1))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(0, 0))

    val expectedFirstRow = Array(opponent.getPiece, settings.emptyPositionValue, settings.emptyPositionValue)
    val expectedSecondRow = Array(settings.emptyPositionValue, opponent.getPiece, settings.emptyPositionValue)
    val expectedThirdRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, user.getPiece)

    thirdMoveState.blanks should be (initialBoardState.blanks - 3)
    thirdMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
    thirdMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
    thirdMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow
  }

  test("A draw game state should have all positions defined") {
    val firstMoveState = NextBoardState(initialBoardState, Move(1, 1))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(0, 0))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(2, 0))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(2, 1))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(0, 1))
    val seventhMoveState = NextBoardState(sixthMoveState, Move(0, 2))
    val eighthMoveState = NextBoardState(seventhMoveState, Move(1, 0))
    val ninthMoveState = NextBoardState(eighthMoveState, Move(1, 2))

    val expectedFirstRow = Array(opponent.getPiece, user.getPiece, opponent.getPiece)
    val expectedSecondRow = Array(user.getPiece, opponent.getPiece, opponent.getPiece)
    val expectedThirdRow = Array(user.getPiece, opponent.getPiece, user.getPiece)

    ninthMoveState.blanks shouldBe 0
    ninthMoveState.player shouldBe opponent

    ninthMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
    ninthMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
    ninthMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow

    ninthMoveState.won(user) shouldBe false
    ninthMoveState.won(opponent) shouldBe false
    ninthMoveState.over() shouldBe true
    ninthMoveState.draw() shouldBe true
  }

  test("A draw board game state should not accept new moves") {
    val firstMoveState = NextBoardState(initialBoardState, Move(1, 1))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(0, 0))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(2, 0))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(2, 1))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(0, 1))
    val seventhMoveState = NextBoardState(sixthMoveState, Move(0, 2))
    val eighthMoveState = NextBoardState(seventhMoveState, Move(1, 0))
    val ninthMoveState = NextBoardState(eighthMoveState, Move(1, 2))

    an [UnsupportedOperationException] should be thrownBy NextBoardState(ninthMoveState, Move(1, 1))
  }

  test("A board with a player's horizontal line of the same piece should be defined as won") {
    val firstMoveState = NextBoardState(initialBoardState, Move(1, 1))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(1, 0))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(2, 0))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(1, 2))

    fifhtMoveState.over() shouldBe true
    fifhtMoveState.draw() shouldBe false
    fifhtMoveState.won(user) shouldBe false
    fifhtMoveState.won(opponent) shouldBe true
  }

  test("A board with a player's vertical line of the same piece should be defined as won") {
    val firstMoveState = NextBoardState(initialBoardState, Move(1, 1))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(1, 0))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(1, 2))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(0, 0))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(0, 2))

    sixthMoveState.over() shouldBe true
    sixthMoveState.draw() shouldBe false
    sixthMoveState.won(user) shouldBe true
    sixthMoveState.won(opponent) shouldBe false
  }

  test("A board with a player's diagonal line of the same piece should be defined as won") {
    val firstMoveState = NextBoardState(initialBoardState, Move(0, 0))
    val secondMoveState = NextBoardState(firstMoveState, Move(2, 1))
    val thirdMoveState = NextBoardState(secondMoveState, Move(1, 1))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(1, 2))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(2, 2))

    fifhtMoveState.over() shouldBe true
    fifhtMoveState.draw() shouldBe false
    fifhtMoveState.won(user) shouldBe false
    fifhtMoveState.won(opponent) shouldBe true
  }

  test("A board with a player's reverse diagonal line of the same piece should be defined as won") {
    val firstMoveState = NextBoardState(initialBoardState, Move(0, 0))
    val secondMoveState = NextBoardState(firstMoveState, Move(0, 2))
    val thirdMoveState = NextBoardState(secondMoveState, Move(1, 2))
    val fourthMoveState = NextBoardState(thirdMoveState, Move(1, 1))
    val fifhtMoveState = NextBoardState(fourthMoveState, Move(2, 2))
    val sixthMoveState = NextBoardState(fifhtMoveState, Move(2, 0))

    sixthMoveState.over() shouldBe true
    sixthMoveState.draw() shouldBe false
    sixthMoveState.won(user) shouldBe true
    sixthMoveState.won(opponent) shouldBe false
  }

}
