package io.github.rlazoti.tictactoe.models

import org.scalatest._

class PlayerSpec extends FunSuite with Matchers {

  test("Piece should accept 'x' as Input") {
    val piece = Piece.getByType("x")
    piece shouldBe Cross()
  }

  test("Piece should accept 'X' as Input") {
    val piece = Piece.getByType("X")
    piece shouldBe Cross()
  }

  test("Any other input different from 'x' and 'X' should be transformed into Nought piece") {
    Piece.getByType("a") shouldBe Nought()
    Piece.getByType("B") shouldBe Nought()
    Piece.getByType("123") shouldBe Nought()
    Piece.getByType(" ") shouldBe Nought()
    Piece.getByType("") shouldBe Nought()
  }

  test("An opponent's piece should always be the opposite piece to the player's piece") {
    Piece.getOpponentPiece(Piece.getByType("x")) shouldBe Nought()
    Piece.getOpponentPiece(Piece.getByType("o")) shouldBe Cross()
  }

}
