package org.andstatus.game2048

import com.soywiz.korge.Korge
import com.soywiz.korim.color.Colors
import com.soywiz.korma.geom.SizeInt
import kotlin.coroutines.coroutineContext
import kotlin.math.max
import kotlin.properties.Delegates

val defaultPortraitGameWindowSize = SizeInt(720, 1280)
val defaultDesktopGameWindowSize get() = SizeInt(defaultPortraitGameWindowSize.width / 2,
    defaultPortraitGameWindowSize.height / 2)
const val defaultPortraitTextSize = 64.0
var defaultTextSize: Double by Delegates.notNull()

suspend fun main() {
    val windowSize: SizeInt = coroutineContext.gameWindowSize
    val virtualHeight: Int = defaultPortraitGameWindowSize.height
    val windowProportionalWidth = virtualHeight * windowSize.width / windowSize.height
    // We will set the width depending on orientation, when we have landscape layout...
    val virtualWidth = max( windowProportionalWidth, defaultPortraitGameWindowSize.width)
    defaultTextSize = if (virtualHeight == defaultPortraitGameWindowSize.height) defaultPortraitTextSize
        else defaultPortraitTextSize * virtualHeight / defaultPortraitGameWindowSize.height
    val color = if (coroutineContext.isDarkThemeOn) Colors.BLACK else gameDefaultBackgroundColor
    Korge(width = windowSize.width, height = windowSize.height,
            virtualWidth = virtualWidth, virtualHeight = virtualHeight,
            bgcolor = color,
            gameId = "org.andstatus.game2048") {
        GameView.initialize(this, true)
    }
}