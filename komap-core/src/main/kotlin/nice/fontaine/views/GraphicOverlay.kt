package nice.fontaine.views

import nice.fontaine.map.MapCanvas
import nice.fontaine.models.LruCache
import java.awt.Graphics
import java.util.*

class GraphicOverlay(
        private val parent: MapCanvas,
        capacity: Int = 200
) : View {
    private val lock = Any()
    private val graphics = Collections.newSetFromMap(LruCache<Graphic, Boolean>(capacity))

    override fun invalidate() {
        parent.draw()
    }

    fun clear() {
        synchronized(lock) {
            graphics.clear()
        }
    }

    fun add(graphic: Graphic) {
        synchronized(lock) {
            graphics.add(graphic)
        }
    }

    fun remove(graphic: Graphic) {
        synchronized(lock) {
            graphics.remove(graphic)
        }
    }

    override fun draw(context: Graphics) {
        synchronized(lock) {
            for (graphic in graphics) {
                graphic.draw(context)
            }
        }
    }
}
