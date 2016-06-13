package io.github.rlazoti.tictacjoe.backend

object App extends App {

  val currentBoardId = 1
  val currentBoard = Board(currentBoardId)
  var boards = List(currentBoard)

  val moves = List(
    Move(currentBoard.user, Position(0, 2)),
    Move(currentBoard.opponent, Position(1, 0)),
    Move(currentBoard.user, Position(2, 0)),
    Move(currentBoard.opponent, Position(1, 2)),
    Move(currentBoard.user, Position(1, 1)),
    Move(currentBoard.opponent, Position(2, 2))
  )

  currentBoard.printCurrentState()

  moves.foreach { nextMove =>
    boards = boards
      .filter { board => board.id == currentBoardId }
      .map { board => board.addMove(nextMove) }
      //.map { board => board.printCurrentState(); board }
  }

}
