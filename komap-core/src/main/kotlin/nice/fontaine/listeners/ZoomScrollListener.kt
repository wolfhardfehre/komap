package nice.fontaine.listeners

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.ScrollEvent
import nice.fontaine.map.MapCanvas

class ZoomScrollListener(private val canvas: MapCanvas) : EventHandler<ScrollEvent> {

    override fun handle(event: ScrollEvent?) {
        if (event == null) return
        val bound = canvas.getViewportBounds()

        val dx = event.x - bound.width / 2
        val dy = event.y - bound.height / 2

        val oldMapSize = canvas.getTileFactory().getMapSize(canvas.getZoom())
        val delta = -(event.deltaY / Math.abs(event.deltaY)).toInt()
        canvas.setZoom(canvas.getZoom() + delta)

        val mapSize = canvas.getTileFactory().getMapSize(canvas.getZoom())

        val center = canvas.getCenter()

        val dzw = mapSize.getWidth() / oldMapSize.getWidth()
        val dzh = mapSize.getHeight() / oldMapSize.getHeight()

        val x = center.getX() + dx * (dzw - 1)
        val y = center.getY() + dy * (dzh - 1)

        canvas.setCenter(Point2D(x, y))
        canvas.draw()
    }
}