package io.github.rlazoti.tictacjoe.backend

object Game {
  type Positions = Array[Array[String]]
  val EmptyPosition = "-"
  val width = 3
}

trait Player {
  def name: String
  def mark: String
}

case class Position(val row: Int, val col: Int)

case class Computer() extends Player {
  val name = "CPU"
  val mark = "O"
}

case class User(val name: String) extends Player {
  val mark = "X"
}

trait BoardState {
  def blanks: Int
  def positions: Game.Positions

  def over(player: Player, opponent: Player) =
    won(player) || won(opponent) || draw(player, opponent)

  def draw(player: Player, opponent: Player) =
    !won(player) && !won(opponent) && blanks == 0

  def won(player: Player) =
    winningCol(player) || winningRow(player) || winningDiagonal(player) || winningReverseDiagonal(player)

  private def checkValues(player: Player, values: Array[String]) =
    values.foldLeft(true) { (acc, value) => acc && value == player.mark }

  private def winningDiagonal(player: Player) = {
    val values =
      for (index <- 0 to Game.width - 1)
      yield positions(index)(index)

    checkValues(player, values.toArray)
  }

  private def winningReverseDiagonal(player: Player) = {
    val values =
      for (index <- 0 to Game.width - 1)
      yield positions(Game.width - 1 - index)(index)

    checkValues(player, values.toArray)
  }

  private def winningRow(player: Player) =
    positions
      .map { row => checkValues(player, row) }
      .count { result => result == true } > 0

  private def winningCol(player: Player) = {
    def getColumn(columnIndex: Int) =
      positions.map { row => row(columnIndex) }

    val columns =
      for (index <- 0 to Game.width - 1)
      yield getColumn(index)

    columns
      .map { row => checkValues(player, row) }
      .count { result => result == true } > 0
  }

}

case class InitialState() extends BoardState {
  val blanks = Game.width * Game.width
  val positions = Array.fill(Game.width, Game.width)(Game.EmptyPosition)
}

case class NextState(currentState: BoardState, move: Move) extends BoardState {
  val blanks = currentState.blanks - 1
  val positions = buildPositions()

  private def buildPositions() = {
    val newPositions = currentState.positions
    newPositions(move.position.row)(move.position.col) = move.player.mark
    newPositions
  }
}

case class Move(val player: Player, val position: Position)

case class Board(
  val id: Int,
  val width: Int = Game.width,
  val opponent: Player = Computer(),
  val user: Player = User("Rodrigo"),
  val currentState: BoardState = InitialState()) {

  private def isEnded() =
    currentState.over(user, opponent)

  private def userWon() =
    currentState.won(user)

  private def opponentWon() =
    currentState.won(opponent)

  private def draw() =
    currentState.draw(user, opponent)

  def addMove(move: Move) = {
    if (isEnded()) this
    else {
      val board = Board(id, width, opponent, user, NextState(currentState, move))
      println(s"next move => ${move.player.name} marks ${move.position}")
      board.printCurrentState()
      board
    }
  }

  def printCurrentState() = {
    val message =
      if (userWon()) s"${user.name} won!"
      else if (opponentWon()) s"${opponent.name} won!"
      else if (draw()) s"Game Draw!"
      else "Game is not over yet!"

    println(s"""
      #=============== Board =================
      #id: $id
      #status: ${message}
      #blanks: ${currentState.blanks}
      #positions:
      #${currentState.positions.map(_.mkString("|")).mkString("\n")}
      #=======================================
      #""".stripMargin('#'))
  }
}

object Test extends App {
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
