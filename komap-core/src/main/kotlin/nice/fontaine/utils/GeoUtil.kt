package nice.fontaine.utils

import nice.fontaine.map.MapCanvas
import nice.fontaine.models.GeoBounds
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.TileInfo
import nice.fontaine.processors.TileFactory
import java.awt.Dimension
import java.awt.geom.Dimension2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.lang.Math.*
import java.util.HashSet

object GeoUtil {

    fun getMapSize(zoom: Int, info: TileInfo): Dimension2D {
        val size = info.mapWidthInTilesAt(zoom).toInt()
        return Dimension(size, size)
    }

    fun isValidTile(x: Int, y: Int, zoom: Int, info: TileInfo): Boolean {
        if (x < 0 || y < 0) return false
        if (info.centerPxAt(zoom).x * 2 <= x * info.getTileSize()) return false
        if (info.centerPxAt(zoom).y * 2 <= y * info.getTileSize()) return false
        return !(zoom < info.getMinZoom() || zoom > info.getMaxZoom())
    }

    fun geoToPixel(position: GeoPosition, zoom: Int, info: TileInfo): Point2D {
        val x = info.centerPxAt(zoom).x + position.getLongitude() * info.widthInDeg(zoom)
        var e = Math.sin(Math.toRadians(position.getLatitude()))
        e = clamp(e, -0.9999, 0.9999)
        val y = info.centerPxAt(zoom).y + 0.5 * Math.log((1 + e) / (1 - e)) * -1 * info.widthInRad(zoom)
        return Point2D.Double(x, y)
    }

    fun clamp(x: Double, lower: Double, upper: Double): Double = if (x < lower) lower else if (x > upper) upper else x

    fun pixelToGeo(coordinate: Point2D, zoom: Int, info: TileInfo): GeoPosition {
        val lon = (coordinate.x - info.centerPxAt(zoom).x) / info.widthInDeg(zoom)
        val e1 = (coordinate.y - info.centerPxAt(zoom).y) / -info.widthInRad(zoom)
        val lat = Math.toDegrees(2 * atan(exp(e1)) - PI / 2)
        return GeoPosition(lat, lon)
    }

    fun boundingBox(positions: Set<GeoPosition>, factory: TileFactory, zoom: Int): Rectangle2D {
        var (minX, maxX) = maxRange()
        var (minY, maxY) = maxRange()

        for (position in positions) {
            val point = factory.geoToPixel(position, zoom)
            if (point.x < minX) minX = point.x
            if (point.x > maxX) maxX = point.x
            if (point.y < minY) minY = point.y
            if (point.y > maxY) maxY = point.y
        }

        return Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY)
    }

    fun maxRange(): Pair<Double, Double> = Pair(Double.MAX_VALUE, Double.MIN_VALUE)

    fun resolution(lat: Double, tileSize: Int, zoom: Int): Double {
        val horizontalTileSize = 40075016.686 / tileSize
        return horizontalTileSize * Math.cos(lat) / Math.pow(2.0, zoom.toDouble())
    }

    fun getMapBounds(mapCanvas: MapCanvas): GeoBounds {
        return getMapGeoBounds(mapCanvas)
    }

    private fun getMapGeoBounds(mapCanvas: MapCanvas): GeoBounds {
        val set = HashSet<GeoPosition>()
        val tileFactory = mapCanvas.getTileFactory()
        val zoom = mapCanvas.getZoom()
        val bounds = mapCanvas.getViewportBounds()
        var pt = Point2D.Double(bounds.minX, bounds.minY)
        set.add(tileFactory.pixelToGeo(pt, zoom))
        pt = Point2D.Double(bounds.minX + bounds.width, bounds.minY + bounds.height)
        set.add(tileFactory.pixelToGeo(pt, zoom))
        return GeoBounds(set)
    }
}
