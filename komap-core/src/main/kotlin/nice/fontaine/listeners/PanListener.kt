package nice.fontaine.listeners

import nice.fontaine.map.MapCanvas
import java.awt.Cursor
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.geom.Point2D

class PanListener(private val canvas: MapCanvas) : MouseListener, MouseMotionListener {
    private var previous: MouseEvent? = null
    private var priorCursor: Cursor? = null

    override fun mouseDragged(event: MouseEvent?) {
        if (event == null || event.button != MouseEvent.NOBUTTON) return
        val center = canvas.getCenter()
        var x = center.x
        var y = center.y
        if (previous != null) {
            x += (previous!!.x - event.x)
            y += (previous!!.y - event.y)
        }
        val factory = canvas.getTileFactory()
        val zoom = canvas.getZoom()
        y = factory.recomputeY(zoom, y)

        previous = event
        canvas.setCenter(Point2D.Double(x, y))
        canvas.draw()
    }

    override fun mouseReleased(event: MouseEvent?) {
        previous = null
        canvas.cursor = priorCursor
    }

    override fun mousePressed(event: MouseEvent?) {
        if (event == null || event.button != MouseEvent.NOBUTTON) return
        previous = event
        priorCursor = canvas.cursor
    }

    override fun mouseMoved(event: MouseEvent?) {}

    override fun mouseEntered(event: MouseEvent?) {}

    override fun mouseClicked(event: MouseEvent?) {}

    override fun mouseExited(event: MouseEvent?) {}
}
