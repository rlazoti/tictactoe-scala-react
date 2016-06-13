package io.github.rlazoti.tictacjoe.backend

import org.scalatest._

class AppTest extends FunSuite with Matchers {

  private val emptyRow = Array(Game.EmptyPosition, Game.EmptyPosition, Game.EmptyPosition)
  private val opponent = Computer()
  private val user = User("Rafael")
  private val initialState = InitialState()

  test("All positions of Board's initial state should be blank") {
    val expectedRow = Array(Game.EmptyPosition, Game.EmptyPosition, Game.EmptyPosition)

    initialState.blanks should be (Game.width * Game.width)
    initialState.positions.map { row =>
      row should contain theSameElementsAs emptyRow
    }
  }

  test("Board's initial state should not define the game as over") {
    initialState.over(user, opponent) should be (false)
  }

  test("An user's move should be registered into Board's state") {
    val state = NextState(initialState, Move(opponent, Position(0, 1)))
    val expectedRow = Array(Game.EmptyPosition, Computer().mark, Game.EmptyPosition)

    state.blanks should be (InitialState().blanks - 1)
    state.positions(0) should contain theSameElementsInOrderAs expectedRow
    state.positions(1) should contain theSameElementsInOrderAs emptyRow
    state.positions(2) should contain theSameElementsInOrderAs emptyRow
  }

  test("Three users' moves should be registered into Board's state") {
    val firstMoveState = NextState(initialState, Move(opponent, Position(1, 1)))
    val secondMoveState = NextState(firstMoveState, Move(user, Position(2, 2)))
    val thirdMoveState = NextState(secondMoveState, Move(opponent, Position(0, 0)))

    val expectedFirstRow = Array(opponent.mark, Game.EmptyPosition, Game.EmptyPosition)
    val expectedSecondRow = Array(Game.EmptyPosition, opponent.mark, Game.EmptyPosition)
    val expectedThirdRow = Array(Game.EmptyPosition, Game.EmptyPosition, user.mark)

    thirdMoveState.blanks should be (InitialState().blanks - 3)
    thirdMoveState.positions(0) should contain theSameElementsInOrderAs expectedFirstRow
    thirdMoveState.positions(1) should contain theSameElementsInOrderAs expectedSecondRow
    thirdMoveState.positions(2) should contain theSameElementsInOrderAs expectedThirdRow
  }

}
