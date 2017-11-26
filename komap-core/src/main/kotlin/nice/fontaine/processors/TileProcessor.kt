package nice.fontaine.processors

import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import nice.fontaine.models.CCoord
import nice.fontaine.models.TCoord
import nice.fontaine.views.GraphicOverlay

class TileProcessor(private val factory: TileFactory,
                    private val overlay: GraphicOverlay,
                    private val canvas: Canvas) {

    fun process(bounds: Rectangle2D, zoom: Int) {
        val tileSize = factory.getTileSize()
        val (width, height) = Pair(bounds.width / tileSize + 2, bounds.height / tileSize + 2)
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
        return CCoord(tileCoord.x * tileSize - bounds.minX, tileCoord.y * tileSize - bounds.minY)
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

    private fun canvasArea(): Rectangle2D {
        val b = canvas.layoutBounds
        return Rectangle2D(b.minX, b.minY, b.width, b.height)
    }

    private fun tileArea(canvasCoord: CCoord, tileSize: Double):
            Rectangle2D = Rectangle2D(canvasCoord.x, canvasCoord.y, tileSize, tileSize)
}