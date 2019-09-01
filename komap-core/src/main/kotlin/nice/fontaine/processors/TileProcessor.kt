package nice.fontaine.processors

import nice.fontaine.models.CCoord
import nice.fontaine.models.TCoord
import nice.fontaine.views.GraphicOverlay
import java.awt.geom.Rectangle2D
import javax.swing.JFrame

class TileProcessor(
        private val factory: TileFactory,
        private val overlay: GraphicOverlay,
        private val canvas: JFrame
) {
    fun process(bounds: Rectangle2D, zoom: Int) {
        val tileSize = factory.getTileSize()
        val (width, height) = Pair(bounds.width / tileSize, bounds.height / tileSize)
        val (dx, dy) = Pair(bounds.minX / tileSize, bounds.minY / tileSize)

        for (x in 0..width.toInt()) {
            (0..height.toInt())
                    .map { TCoord(x + dx.toInt(), it + dy.toInt()) }
                    .forEach { computeTile(it, tileSize, bounds, zoom) }
        }
    }

    private fun computeTile(tileCoord: TCoord, tileSize: Int, bounds: Rectangle2D, zoom: Int) {
        val canvasCoord = computeCanvas(tileCoord, tileSize.toDouble(), bounds)
        if (intersects(canvasCoord, tileSize.toDouble())) {
            if (!isTileOnMap(tileCoord, zoom)) return
            val tile = factory.getTile(overlay, tileCoord, canvasCoord, zoom)
            overlay.add(tile)
        }
    }

    private fun computeCanvas(tileCoord: TCoord, tileSize: Double, bounds: Rectangle2D): CCoord {
        val x = tileCoord.x * tileSize - bounds.minX
        val y = tileCoord.y * tileSize - bounds.minY
        return CCoord(x.toInt(), y.toInt())
    }

    private fun isTileOnMap(tileCoord: TCoord, zoom: Int): Boolean {
        val mapSize = factory.getMapSize(zoom)
        return tileCoord.y >= 0 && tileCoord.y < mapSize.height
    }

    private fun intersects(canvasCoord: CCoord, tileSize: Double): Boolean {
        val canvasArea = canvasArea()
        val tileArea = tileArea(canvasCoord, tileSize)
        return canvasArea.intersects(tileArea)
    }

    private fun canvasArea(): Rectangle2D =
            Rectangle2D.Double(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

    private fun tileArea(canvasCoord: CCoord, tileSize: Double): Rectangle2D =
            Rectangle2D.Double(canvasCoord.x.toDouble(), canvasCoord.y.toDouble(), tileSize, tileSize)
}
