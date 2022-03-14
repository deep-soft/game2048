import com.soywiz.korge.tests.ViewsForTesting
import org.andstatus.game2048.gameIsLoading
import kotlin.test.Test
import kotlin.test.assertEquals

class LoadGameTest : ViewsForTesting(log = true) {

    @Test
    fun loadGameTest() = myViewsTest(this) {
        waitFor("Block is on board") {
            presenter.boardViews.blocks.isNotEmpty()
        }

        waitForMainViewShown {
            presenter.onLoadClick()
        }

        waitFor("Game is loaded") {
            !gameIsLoading.value
        }

        val game = presenter.model.history.currentGame
        assertEquals(7475, game.gamePlies.size, game.toString())
        assertEquals(746, game.gamePlies[5001].seconds, game.toString())
        assertEquals(1700, game.gamePlies[7475].seconds, game.toString())
    }
}