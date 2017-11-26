package nice.fontaine.processors

import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import nice.fontaine.models.*
import nice.fontaine.utils.GeoUtil
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.views.TileGraphic

class TileFactory(private var info: TileInfo) {

    private val tileMap = LruCache<String, TileGraphic>(200)
    private val loader = TileLoader()

    fun getInfo(): TileInfo = info

    fun getTileSize(): Int = info.getTileSize()

    fun getMapSize(zoom: Int): Dimension2D = GeoUtil.getMapSize(zoom, info)

    fun pixelToGeo(pixelCoordinate: Point2D, zoom: Int):
            GeoPosition = GeoUtil.pixelToGeo(pixelCoordinate, zoom, info)

    fun geoToPixel(c: GeoPosition, zoomLevel: Int):
            Point2D = GeoUtil.geoToPixel(c, zoomLevel, info)

    fun getTile(overlay: GraphicOverlay, tileCoord: TCoord,
                canvasCoord: CCoord, zoom: Int): TileGraphic {
        val tileX = recomputeX(tileCoord.x, zoom)
        val url = info.getTileUrl(tileX, tileCoord.y, zoom)
        if (tileMap.containsKey(url)) {
            val tile = tileMap[url]!!
            if (!tile.loading()) tile.moveTo(canvasCoord)
            return tile
        }
        println(url)
        return buildTile(overlay, tileCoord, canvasCoord, zoom, url)
    }

    private fun buildTile(overlay: GraphicOverlay, tileCoord: TCoord,
                          canvasCoord: CCoord, zoom: Int, url: String): TileGraphic {
        val tile = TileGraphic(url, canvasCoord, overlay)
        if (isValidTile(tileCoord, zoom)) {
            loader.load(tile)
            tileMap.put(url, tile)
        }
        return tile
    }

    private fun isValidTile(coord: TCoord, zoom: Int):
            Boolean = GeoUtil.isValidTile(coord.x, coord.y, zoom, info)

    private fun recomputeX(x: Int, zoom: Int): Int {
        var tileX = x
        val numTilesWide = getMapSize(zoom).width.toInt()
        if (tileX < 0) tileX = numTilesWide - Math.abs(tileX) % numTilesWide
        tileX %= numTilesWide
        return tileX
    }
}