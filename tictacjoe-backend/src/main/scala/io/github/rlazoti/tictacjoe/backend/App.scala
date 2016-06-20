package io.github.rlazoti.tictacjoe.backend

import io.github.rlazoti.tictacjoe.backend.models._

object App extends App {

  val currentBoardId = 1
  val settings = GameSettings(currentBoardId, Easy())
  val currentBoard = Board.newGame(settings, User(MarkX()), Computer(MarkO()), "You")
  var boards = List(currentBoard)

  val moves = List(
    Move(0, 2),
    Move(1, 0),
    Move(2, 0),
    Move(1, 2),
    Move(1, 1),
    Move(2, 2)
  )

  currentBoard.printCurrentState()

  moves.foreach { nextMove =>
    boards = boards
      .filter { board => board.settings.gameId == currentBoardId }
      .map { board => board.addMove(nextMove) }
  }

}
