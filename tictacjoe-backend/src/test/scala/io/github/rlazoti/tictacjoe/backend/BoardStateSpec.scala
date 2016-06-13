package io.github.rlazoti.tictacjoe.backend

import org.scalatest._

class AppTest extends FunSuite with Matchers {

  private val emptyRow = Array(Game.EmptyPosition, Game.EmptyPosition, Game.EmptyPosition)

  test("All positions of Board's initial state should be blank") {
    val state = InitialState()
    val expectedRow = Array(Game.EmptyPosition, Game.EmptyPosition, Game.EmptyPosition)

    state.blanks should be (Game.width * Game.width)
    state.positions.map { row =>
      row should contain theSameElementsAs emptyRow
    }
  }

  test("A user's move should be registered into Board's state") {
    val state = NextState(InitialState(), Move(Computer(), Position(0, 1)))
    val expectedRow = Array(Game.EmptyPosition, Computer().mark, Game.EmptyPosition)

    state.blanks should be (InitialState().blanks - 1)
    state.positions(0) should contain theSameElementsInOrderAs expectedRow
    state.positions(1) should contain theSameElementsInOrderAs emptyRow
    state.positions(2) should contain theSameElementsInOrderAs emptyRow
  }

}
