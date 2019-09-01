package nice.fontaine.listeners

import nice.fontaine.map.MapCanvas
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.geom.Point2D

class ZoomClickListener(private val canvas: MapCanvas) : MouseListener {

    override fun mouseClicked(event: MouseEvent?) {
        println(event)
        if (event == null) return
        if (isLeftDoubleClicked(event)) recenterMap(event)
    }

    private fun isLeftDoubleClicked(event: MouseEvent): Boolean {
        return event.button == MouseEvent.BUTTON1 && event.clickCount == 2
    }

    private fun recenterMap(event: MouseEvent) {
        val bounds = canvas.getViewportBounds()
        val x = bounds.minX + event.x
        val y = bounds.minY + event.y
        canvas.setCenter(Point2D.Double(x, y))
        canvas.setZoom(canvas.getZoom() - 1)
        canvas.draw()
    }

    override fun mouseExited(event: MouseEvent?) {}

    override fun mousePressed(event: MouseEvent?) {}

    override fun mouseReleased(event: MouseEvent?) {}

    override fun mouseEntered(event: MouseEvent?) {}
}
