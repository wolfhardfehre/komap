package nice.fontaine.models

import javafx.geometry.Rectangle2D
import nice.fontaine.utils.GeoUtil.maxRange

class GeoBounds {

    companion object {
        const val TOO_LESS_POSITIONS = "The attribute 'geoPositions' cannot be null and must have 2 or more elements."
    }

    private var rectangles: Array<Rectangle2D>? = null

    constructor(minLat: Double, minLng: Double, maxLat: Double, maxLng: Double) {
        setRect(minLat, minLng, maxLat, maxLng)
    }

    constructor(geoPositions: Set<GeoPosition>?) {
        if (geoPositions == null || geoPositions.size < 2) throw IllegalArgumentException(TOO_LESS_POSITIONS)
        var (minLat, maxLat) = maxRange()
        var (minLng, maxLng) = maxRange()
        for (position in geoPositions) {
            minLat = Math.min(minLat, position.getLatitude())
            minLng = Math.min(minLng, position.getLongitude())
            maxLat = Math.max(maxLat, position.getLatitude())
            maxLng = Math.max(maxLng, position.getLongitude())
        }
        setRect(minLat, minLng, maxLat, maxLng)
    }

    private fun setRect(minLat: Double, minLng: Double, maxLat: Double, maxLng: Double) {
        if (minLat >= maxLat) throw IllegalArgumentException("GeoBounds is not valid - minLat must be less that maxLat.")
        if (minLng >= maxLng) {
            if (minLng > 0 && minLng < 180 && maxLng < 0) {
                rectangles = arrayOf(
                        Rectangle2D(minLng, minLat, 180 - minLng, maxLat - minLat),
                        Rectangle2D(-180.0, minLat, maxLng + 180, maxLat - minLat))
            } else {
                rectangles = arrayOf(Rectangle2D(minLng, minLat, maxLng - minLng, maxLat - minLat))
                throw IllegalArgumentException("GeoBounds is not valid - minLng must be less that maxLng or " + "minLng must be greater than 0 and maxLng must be less than 0.")
            }
        } else {
            rectangles = arrayOf(Rectangle2D(minLng, minLat, maxLng - minLng, maxLat - minLat))
        }
    }

    fun intersects(other: GeoBounds): Boolean {
        var rv = false
        for (r1 in rectangles!!) {
            for (r2 in other.rectangles!!) {
                rv = r1.intersects(r2)
                if (rv) break
            }
            if (rv) break
        }
        return rv
    }

    val northWest: GeoPosition
        get() = GeoPosition(rectangles!![0].minX, rectangles!![0].maxY)

    val southEast: GeoPosition
        get() {
            val rectangle = if (rectangles!!.size > 1) rectangles!![1] else rectangles!![0]
            return GeoPosition(rectangle.maxX, rectangle.minY)
        }
}