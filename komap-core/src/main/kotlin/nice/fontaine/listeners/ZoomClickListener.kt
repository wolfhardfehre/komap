package nice.fontaine.listeners

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import nice.fontaine.map.MapCanvas

class ZoomClickListener(private val canvas: MapCanvas) : EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent?) {
        if (event == null) return
        if (isLeftDoubleClicked(event)) recenterMap(event)
    }

    private fun isLeftDoubleClicked(event: MouseEvent): Boolean {
        return event.button == MouseButton.PRIMARY && event.clickCount == 2
    }

    private fun recenterMap(event: MouseEvent) {
        val bounds = canvas.getViewportBounds()
        val x = bounds.minX + event.x
        val y = bounds.minY + event.y
        canvas.setCenter(Point2D(x, y))
        canvas.setZoom(canvas.getZoom() - 1)
        canvas.draw()
    }
}