package io.github.rlazoti.tictacjoe.backend.models

import org.scalatest._

class BoardStateSpec extends FunSuite with Matchers {

  private implicit val settings = GameSettings(1, Easy())
  private val emptyRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, settings.emptyPositionValue)
  private val opponent = Computer(MarkO())
  private val user = User(MarkX())
  private val initialBoardState = InitialBoardState(settings, user, opponent)

  test("All positions of Board's initial state should be blank") {
    initialBoardState.blanks should be (settings.boardWidth * settings.boardWidth)
    initialBoardState.positions.map { row =>
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

  test("Board's initial state should not accept that user and opponent use the same mark") {
    an [IllegalArgumentException] should be thrownBy
    InitialBoardState(settings, User("Test 1", MarkX()), Computer(MarkX()))

    an [IllegalArgumentException] should be thrownBy
    InitialBoardState(settings, User("Test 2", MarkO()), Computer(MarkO()))
  }

  test("An opponent's move should be registered into a new Board's state") {
    val state = NextBoardState(initialBoardState, Move(0, 1))
    val expectedRow = Array(settings.emptyPositionValue, opponent.getMark, settings.emptyPositionValue)

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

    val expectedFirstRow = Array(opponent.getMark, settings.emptyPositionValue, settings.emptyPositionValue)
    val expectedSecondRow = Array(settings.emptyPositionValue, opponent.getMark, settings.emptyPositionValue)
    val expectedThirdRow = Array(settings.emptyPositionValue, settings.emptyPositionValue, user.getMark)

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

    val expectedFirstRow = Array(opponent.getMark, user.getMark, opponent.getMark)
    val expectedSecondRow = Array(user.getMark, opponent.getMark, opponent.getMark)
    val expectedThirdRow = Array(user.getMark, opponent.getMark, user.getMark)

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

}