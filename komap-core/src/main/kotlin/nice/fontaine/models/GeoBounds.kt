package nice.fontaine.models

import nice.fontaine.utils.GeoUtil.maxRange
import java.awt.geom.Rectangle2D
import kotlin.math.max
import kotlin.math.min

const val TOO_LESS_POSITIONS = "The attribute 'geoPositions' cannot be null and must have 2 or more elements"
const val INVALID_GEOBOUNDS = "GeoBounds is not valid"

class GeoBounds(geoPositions: Set<GeoPosition>?) {
    private val rectangles: Array<Rectangle2D>
    val northWest: GeoPosition
        get() = GeoPosition(rectangles[0].minX, rectangles[0].maxY)
    val southEast: GeoPosition
        get() {
            val rectangle = if (rectangles.size > 1) rectangles[1] else rectangles[0]
            return GeoPosition(rectangle.maxX, rectangle.minY)
        }

    init {
        check(!(geoPositions == null || geoPositions.size < 2)) { TOO_LESS_POSITIONS }
        var (minLat, maxLat) = maxRange()
        var (minLng, maxLng) = maxRange()
        for (position in geoPositions) {
            minLat = min(minLat, position.latitude)
            minLng = min(minLng, position.longitude)
            maxLat = max(maxLat, position.latitude)
            maxLng = max(maxLng, position.longitude)
        }
        rectangles = setRect(minLat, minLng, maxLat, maxLng)
    }

    private fun setRect(
        minLat: Double,
        minLng: Double,
        maxLat: Double,
        maxLng: Double
    ): Array<Rectangle2D> {
        require(minLat < maxLat) { INVALID_GEOBOUNDS }
        if (minLng >= maxLng) {
            if (minLng > 0 && minLng < 180 && maxLng < 0) {
                return arrayOf(
                        Rectangle2D.Double(minLng, minLat, 180 - minLng, maxLat - minLat),
                        Rectangle2D.Double(-180.0, minLat, maxLng + 180, maxLat - minLat))
            } else {
                return arrayOf(Rectangle2D.Double(minLng, minLat, maxLng - minLng, maxLat - minLat))
            }
        } else {
            return arrayOf(Rectangle2D.Double(minLng, minLat, maxLng - minLng, maxLat - minLat))
        }
    }

    fun intersects(other: GeoBounds): Boolean {
        var rv = false
        for (r1 in rectangles) {
            for (r2 in other.rectangles) {
                rv = r1.intersects(r2)
                if (rv) break
            }
            if (rv) break
        }
        return rv
    }
}
