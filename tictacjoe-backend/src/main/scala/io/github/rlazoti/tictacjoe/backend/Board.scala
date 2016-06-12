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
  def occupieds: Int
  def positions: Game.Positions
  //def nextPlayer: Player
}

case class InitialState() extends BoardState {
  val blanks = Game.width * Game.width
  val occupieds = blanks
  val positions = Array.fill(Game.width, Game.width)(Game.EmptyPosition)
}

case class NextState(currentState: BoardState, move: Move) extends BoardState {
  val blanks = currentState.blanks - 1
  val occupieds = currentState.occupieds - 1
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
  val cpu: Player = Computer(),
  val user: Player = User("Rodrigo"),
  val currentState: BoardState = InitialState()) {

  def addMove(move: Move) = {
    val b = Board(id, width, cpu, user, NextState(currentState, move))
    println(s"next move => ${move.player.name} marks ${move.position}")

    if (won(move.player)) println(s"${move.player.name} won!")

    b
  }

  private def won(player: Player) =
    winningCol(player) || winningRow(player) || winningDiagonal(player) || winningReverseDiagonal(player)

  private def winningDiagonal(player: Player) = {
    def checkValues(values: Array[String]) =
      values.foldLeft(true) { (acc, value) => acc && value == player.mark }

    val values =
      for (index <- 0 to Game.width - 1)
      yield currentState.positions(index)(index)

    checkValues(values.toArray)
  }

  private def winningReverseDiagonal(player: Player) = {
    def checkValues(values: Array[String]) =
      values.foldLeft(true) { (acc, value) => acc && value == player.mark }

    val values =
      for (row <- Game.width - 1 to 0; col <- 0 to Game.width - 1)
      yield currentState.positions(row)(col)

    checkValues(values.toArray)
  }

  private def winningRow(player: Player) = {
    def checkRow(row: Array[String]) =
      row.foldLeft(true) { (acc, value) => acc && value == player.mark }

    currentState.positions.map(checkRow(_)).count { result => result } > 0
  }

  private def winningCol(player: Player) = {
    def getCol(colIndex: Int) =
      currentState.positions.map { row => row(colIndex) }

    def checkCol(col: Array[String]) =
      col.foldLeft(true) { (acc, value) => acc && value == player.mark }

    val cols = for (index <- 0 to Game.width - 1) yield getCol(index)
    cols.map(checkCol(_)).count { result => result } > 0
  }

  def printCurrentState() =
    println(s"""
=============== Board =================
  id: $id
  blanks: ${currentState.blanks}
  occupieds: ${currentState.occupieds}
  positions: \n${currentState.positions.map(_.mkString("|"))mkString("\n")}
=======================================
""")

}

object Test extends App {
  val currentBoardId = 1
  val currentBoard = Board(currentBoardId)
  var boards = List(currentBoard)

  val moves = List(
    Move(currentBoard.user, Position(0, 2)),
    Move(currentBoard.cpu, Position(1, 0)),
    Move(currentBoard.user, Position(2, 0)),
    Move(currentBoard.cpu, Position(1, 2)),
    Move(currentBoard.user, Position(1, 1)),
    Move(currentBoard.cpu, Position(2, 2))
  )

  currentBoard.printCurrentState()

  moves.foreach { nextMove =>
    boards = boards
      .filter { board => board.id == currentBoardId }
      .map { board => board.addMove(nextMove) }
      .map { board => board.printCurrentState(); board }
  }

}
