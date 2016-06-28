package io.github.rlazoti.tictacjoe.backend.models

sealed trait BoardState {
  def settings: GameSettings
  def player: Player
  def opponentPlayer: Player
  def positions: Array[Array[String]]
  def lastMove: Option[Move]

  def blanks(): Int =
    positions.flatten.count { value =>
      value.equals(settings.emptyPositionValue)
    }

  def over(): Boolean =
    won(player) || won(opponentPlayer) || draw()

  def draw(): Boolean =
    !won(player) && !won(opponentPlayer) && blanks() == 0

  def won(somePlayer: Player): Boolean =
    winningCol(somePlayer) ||
    winningRow(somePlayer) ||
    winningDiagonal(somePlayer) ||
    winningReverseDiagonal(somePlayer)

  protected def validatePlayers() =
    if (player.getMark == opponentPlayer.getMark)
      throw new IllegalArgumentException("Both player and opponent cannot use the same mark")

  private def checkValues(somePlayer: Player, values: Array[String]) =
    values.foldLeft(true) { (acc, value) =>
      acc && value == somePlayer.getMark
    }

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

  validatePlayers()

  val lastMove = None
  val positions = Array.fill(settings.boardWidth, settings.boardWidth)(settings.emptyPositionValue)
}

case class CurrentBoardState(val settings: GameSettings, val player: Player, val opponentPlayer: Player,
  val positions: Array[Array[String]]) extends BoardState {

  val lastMove = None
  validatePlayers()
}

case class NextBoardState(currentState: BoardState, opponentsMove: Move) extends BoardState {

  if (currentState.over())
    throw new UnsupportedOperationException(s"No one can perform a new move ater the end of game.")

  val settings = currentState.settings
  val player = currentState.opponentPlayer
  val opponentPlayer = currentState.player
  val positions = applyOpponentsMove(opponentsMove, currentState.opponentPlayer)
  val lastMove = Some(opponentsMove)

  private def applyOpponentsMove(move: Move, opponent: Player) = {
    if (!currentState.positions(move.row)(move.col).equals(settings.emptyPositionValue))
      throw new UnsupportedOperationException("It's not valid to mark an already marked position.")

    val newPositions = currentState.positions.map { values => values.map(identity) }
    newPositions(move.row)(move.col) = opponent.getMark
    newPositions
  }

}
