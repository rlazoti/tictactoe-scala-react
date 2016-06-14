package io.github.rlazoti.tictacjoe.backend

import org.scalatest._

class AppTest extends FunSuite with Matchers {

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

  test("Board's initial state should not define the game as over") {
    initialBoardState.over(user, opponent) should be (false)
  }

  test("An user's move should be registered into Board's state") {
    val state = NextBoardState(initialBoardState, Move(opponent, Position(0, 1)))
    val expectedRow = Array(settings.emptyPositionValue, Computer().mark, settings.emptyPositionValue)

    state.blanks should be (initialBoardState.blanks - 1)
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

  // test("A draw game should not register new moves") {
  //   val firstMoveState = NextState(initialState, Move(opponent, Position(1, 1)))
  //   val secondMoveState = NextState(firstMoveState, Move(user, Position(2, 2)))
  //   val thirdMoveState = NextState(secondMoveState, Move(opponent, Position(0, 0)))
  //   val fourthMoveState = NextState(thirdMoveState, Move(user, Position(2, 0)))
  //   val fifhtMoveState = NextState(fourthMoveState, Move(opponent, Position(2, 1)))

  //   val expectedFirstRow = Array(opponent.mark, Game.EmptyPosition, Game.EmptyPosition)
  //   val expectedSecondRow = Array(Game.EmptyPosition, opponent.mark, Game.EmptyPosition)
  //   val expectedThirdRow = Array(Game.EmptyPosition, Game.EmptyPosition, user.mark)

  //   thirdMoveState.blanks should be (InitialState().blanks - 3)
  //   thirdMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
  //   thirdMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
  //   thirdMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow
  // }

}
