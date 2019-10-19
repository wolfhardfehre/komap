package nice.fontaine.processors

import nice.fontaine.models.*
import nice.fontaine.utils.GeoUtil
import nice.fontaine.utils.mod
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.views.TileGraphic
import java.awt.Dimension
import java.awt.geom.Point2D
import java.util.*

class TileFactory(private var info: TileInfo) {
    private val tileCache = Collections.synchronizedMap(LruCache<String, TileGraphic>(200))
    private val loader = TileLoader()

    fun info(): TileInfo = info

    fun tileSize(): Int = info.tileSize

    fun mapSize(zoom: Int): Dimension = GeoUtil.getMapSize(zoom, info)

    fun pixelToGeo(pixelCoordinate: Point2D, zoom: Int): GeoPosition =
            GeoUtil.pixelToGeo(pixelCoordinate, zoom, info)

    fun geoToPixel(c: GeoPosition, zoomLevel: Int): Point2D =
            GeoUtil.geoToPixel(c, zoomLevel, info)

    fun getTile(overlay: GraphicOverlay, tileCoord: TCoord, canvasCoord: CCoord, zoom: Int): TileGraphic {
        val tileX = recomputeX(tileCoord.x, zoom)
        val url = info.tileUrl(tileX, tileCoord.y, zoom)
        if (tileCache.containsKey(url)) {
            val tile = tileCache[url]!!
            if (!tile.isLoading) tile.moveTo(canvasCoord)
            return tile
        }
        println(url)
        return buildTile(overlay, tileCoord, canvasCoord, zoom, url)
    }

    fun recomputeY(zoom: Int, y: Double): Double {
        val tileSize = tileSize()
        val mapSize = mapSize(zoom)
        val maxHeight = mapSize.height * tileSize - tileSize
        if (y < tileSize) return tileSize.toDouble()
        if (y > maxHeight) return maxHeight.toDouble()
        return y
    }

    private fun buildTile(
            overlay: GraphicOverlay,
            tileCoord: TCoord,
            canvasCoord: CCoord,
            zoom: Int,
            url: String
    ): TileGraphic {
        val tile = TileGraphic(url, canvasCoord, overlay)
        if (isValidTile(tileCoord, zoom)) {
            loader.load(tile)
            tileCache[url] = tile
        }
        return tile
    }

    private fun isValidTile(coord: TCoord, zoom: Int): Boolean =
            GeoUtil.isValidTile(coord.x, coord.y, zoom, info)

    private fun recomputeX(x: Int, zoom: Int): Int {
        var tileX = x
        val numTilesWide = mapSize(zoom).width
        if (tileX < 0) tileX = mod(tileX, numTilesWide)
        tileX %= numTilesWide
        return tileX
    }
}
