import com.soywiz.korge.tests.ViewsForTesting
import com.soywiz.korio.concurrent.atomic.korAtomic
import org.andstatus.game2048.model.GamePosition
import org.andstatus.game2048.model.Piece
import org.andstatus.game2048.model.PlacedPiece
import org.andstatus.game2048.model.Ply
import org.andstatus.game2048.view.ViewData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersistenceTest : ViewsForTesting(log = true) {

    @Test
    fun persistenceTest() {
        val testWasExecuted = korAtomic(false)
        viewsTest {
            unsetGameView()
            initializeViewDataInTest {
                persistGameRecordTest(0)
                persistGameRecordTest(1)
                persistGameRecordTest(2)
                testWasExecuted.value = true
            }
        }
        waitFor("persistenceTest was executed") { testWasExecuted.value }
    }

    private fun ViewData.persistGameRecordTest(nPlies: Int) {
        val board = presenter.model.gamePosition.board
        val plies = ArrayList<Ply>()
        var nPliesActual = 0
        while (nPliesActual < nPlies) {
            val square = when (nPliesActual) {
                1 -> board.toSquare(2, 2)
                else -> board.toSquare(1, 3)
            }
            val ply = Ply.computerPly(PlacedPiece(Piece.N2, square), 0)
            assertTrue(ply.toMap().keys.size > 2, ply.toMap().toString())
            plies.add(ply)
            nPliesActual++
        }

        val gameRecord = newGameRecord(
            settings,
            GamePosition(board, plyNumber = nPlies),  // Final board is incorrect here - it is empty.
            presenter.model.history.idForNewGame(),
            emptyList(),
            plies
        )
        val sharedJson = gameRecord.toSharedJson()
        val message = "nMoves:$nPlies, $sharedJson"

        if (nPlies > 0) {
            assertTrue(sharedJson.contains("place"), message)
        }
        presenter.loadSharedJson(sharedJson)
        waitFor("currentGame loaded") {
            !presenter.model.history.currentGame.gamePlies.notCompleted
        }

        val gameRecordOpened = presenter.model.history.currentGame
        assertEquals(gameRecord.gamePlies.toLongString(), gameRecordOpened.gamePlies.toLongString(), message)
    }
}