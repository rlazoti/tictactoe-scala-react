package io.github.rlazoti.tictacjoe.backend.models

trait BoardState {

  def blanks: Int
  def settings: GameSettings
  def player: Player
  def opponentPlayer: Player
  def positions: Array[Array[String]]

  def over() =
    won(player) || won(opponentPlayer) || draw()

  def draw() =
    !won(player) && !won(opponentPlayer) && blanks == 0

  def won(somePlayer: Player) =
    winningCol(somePlayer) ||
    winningRow(somePlayer) ||
    winningDiagonal(somePlayer) ||
    winningReverseDiagonal(somePlayer)

  private def checkValues(somePlayer: Player, values: Array[String]) =
    values.foldLeft(true) { (acc, value) => acc && value == somePlayer.getMark }

  private def winningDiagonal(somePlayer: Player) = {
    val values =
      for (index <- 0 to settings.boardWidth - 1)
      yield positions(index)(index)

    checkValues(somePlayer, values.toArray)
  }

  private def winningReverseDiagonal(somePlayer: Player) = {
    val values =
      for (index <- 0 to settings.boardWidth - 1)
      yield positions(settings.boardWidth - 1 - index)(index)

    checkValues(somePlayer, values.toArray)
  }

  private def winningRow(somePlayer: Player) =
    positions
      .map { row => checkValues(somePlayer, row) }
      .count { result => result == true } > 0

  private def winningCol(somePlayer: Player) = {
    def getColumn(columnIndex: Int) =
      positions.map { row => row(columnIndex) }

    val columns =
      for (index <- 0 to settings.boardWidth - 1)
      yield getColumn(index)

    columns
      .map { row => checkValues(somePlayer, row) }
      .count { result => result == true } > 0
  }

}

case class InitialBoardState(val settings: GameSettings, val player: Player, val opponentPlayer: Player)
    extends BoardState {

  if (player.getMark == opponentPlayer.getMark)
    throw new IllegalArgumentException("Both player and opponent cannot use the same mark")

  val blanks = settings.boardWidth * settings.boardWidth
  val positions = Array.fill(settings.boardWidth, settings.boardWidth)(settings.emptyPositionValue)
}

case class NextBoardState(currentState: BoardState, opponentsMove: Move) extends BoardState {

  if (currentState.over())
    throw new UnsupportedOperationException(s"No one can perform a new move ater the end of game.")

  val blanks = currentState.blanks - 1
  val settings = currentState.settings
  val player = currentState.opponentPlayer
  val opponentPlayer = currentState.player
  val positions = applyOpponentsMove(opponentsMove, currentState.opponentPlayer)

  private def applyOpponentsMove(move: Move, opponent: Player) = {
    val newPositions = currentState.positions.map { values => values.map(identity) }
    newPositions(move.row)(move.col) = opponent.getMark
    newPositions
  }

}
