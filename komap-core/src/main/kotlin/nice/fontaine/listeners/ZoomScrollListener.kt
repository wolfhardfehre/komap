package nice.fontaine.listeners

import nice.fontaine.map.MapCanvas
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.awt.geom.Point2D

class ZoomScrollListener(private val canvas: MapCanvas) : MouseWheelListener {
    override fun mouseWheelMoved(event: MouseWheelEvent?) {
        if (event == null) return
        val bound = canvas.getViewportBounds()

        val dx = event.x - bound.width / 2
        val dy = event.y - bound.height / 2

        val oldMapSize = canvas.getTileFactory().mapSize(canvas.getZoom())
        val delta = -(event.scrollAmount / Math.abs(event.scrollAmount))
        canvas.setZoom(canvas.getZoom() + delta)

        val mapSize = canvas.getTileFactory().mapSize(canvas.getZoom())

        val center = canvas.getCenter()

        val dzw = mapSize.getWidth() / oldMapSize.getWidth()
        val dzh = mapSize.getHeight() / oldMapSize.getHeight()

        val x = center.getX() + dx * (dzw - 1)
        val y = center.getY() + dy * (dzh - 1)

        canvas.setCenter(Point2D.Double(x, y))
        canvas.draw()
    }
}
