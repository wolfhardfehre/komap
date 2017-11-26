package nice.fontaine.listeners

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.input.*
import nice.fontaine.map.MapCanvas

class PanListener(private val canvas: MapCanvas) : EventHandler<MouseEvent> {

    private var previous: MouseEvent? = null
    private var priorCursor: Cursor? = null

    override fun handle(event: MouseEvent?) {
        if (event == null || event.button != MouseButton.PRIMARY) return
        when (event.eventType) {
            MouseEvent.MOUSE_PRESSED -> press(event)
            MouseEvent.MOUSE_DRAGGED -> drag(event)
            MouseEvent.MOUSE_RELEASED -> release(event)
        }
    }

    private fun press(event: MouseEvent) {
        previous = event
        priorCursor = canvas.cursor
    }

    private fun release(event: MouseEvent) {
        previous = null
        canvas.cursor = priorCursor
    }

    private fun drag(event: MouseEvent) {
        var x = canvas.getCenter().x
        var y = canvas.getCenter().y

        if (previous != null) {
            x += (previous!!.x - event.x)
            y += (previous!!.y - event.y)
        }

        val maxHeight = maxHeight()
        if (y < 0) y = 0.0
        if (y > maxHeight) y = maxHeight

        previous = event
        canvas.setCenter(Point2D(x, y))
        canvas.draw()
    }

    private fun maxHeight(): Double {
        val factory = canvas.getTileFactory()
        val mapSize = factory.getMapSize(canvas.getZoom())
        val tileSize = factory.getTileSize()
        return mapSize.getHeight() * tileSize
    }
}