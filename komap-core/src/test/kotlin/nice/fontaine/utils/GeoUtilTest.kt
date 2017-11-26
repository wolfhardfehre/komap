package nice.fontaine.utils

import com.nhaarman.mockito_kotlin.*
import javafx.geometry.Point2D
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.MapBox
import nice.fontaine.models.TileInfo
import nice.fontaine.processors.TileFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class GeoUtilTest {

    companion object {
        const val PRECISION = 1e-7
    }

    private val zoom = 10
    private val info: TileInfo = mock()

    @Test fun getMapSize() {
        whenever(info.mapWidthInTilesAt(1)).thenReturn(10.toDouble())

        val dimen = GeoUtil.getMapSize(zoom, info)

        assertEquals(10.0, dimen.height, 1e7)
        assertEquals(10.0, dimen.width, 1e7)
    }

    @Test fun isValidTileReturnsFalseWhenXAndYNegative() {
        val (x, y) = Pair(-1, -1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertFalse(valid)
        verify(info, never()).centerPxAt(zoom)
    }

    @Test fun isValidTileReturnsFalseWhenYNegative() {
        val (x, y) = Pair(1, -1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertFalse(valid)
        verify(info, never()).centerPxAt(zoom)
    }

    @Test fun isValidTileReturnsFalseWhenXNegative() {
        val (x, y) = Pair(-1, 1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertFalse(valid)
        verify(info, never()).centerPxAt(zoom)
    }

    @Test fun isValidCenterXTooSmall() {
        val info: TileInfo = MapBox("")
        val spy = spy(info)
        val (x, y) = Pair(2, 1)

        val valid = GeoUtil.isValidTile(x, y, 19, spy)

        assertFalse(valid)
        verify(spy, times(1)).centerPxAt(19)
        verify(spy, times(1)).getTileSize()
    }

    @Test fun isValidCenterYTooSmall() {
        val info: TileInfo = MapBox("")
        val spy = spy(info)
        val (x, y) = Pair(0, 2)

        val valid = GeoUtil.isValidTile(x, y, 19, spy)

        assertFalse(valid)
        verify(spy, times(2)).centerPxAt(19)
        verify(spy, times(2)).getTileSize()
    }

    @Test fun geoToPixelReturns2DPoint() {
        val info: TileInfo = MapBox("")
        val position = GeoPosition(52.0, 13.0)

        val actual = GeoUtil.geoToPixel(position, 10, info)

        assertEquals(70269.1555555, actual.x, PRECISION)
        assertEquals(43295.0599511, actual.y, PRECISION)
    }

    @Test fun clampLower() {
        val actual = GeoUtil.clamp(-6.0, -5.0, 5.0)

        assertEquals(-5.0, actual, PRECISION)
    }

    @Test fun clampHigher() {
        val actual = GeoUtil.clamp(6.0, -5.0, 5.0)

        assertEquals(5.0, actual, PRECISION)
    }

    @Test fun clampSame() {
        val actual = GeoUtil.clamp(3.0, -5.0, 5.0)

        assertEquals(3.0, actual, PRECISION)
    }

    @Test fun pixelToGeoReturnsGeoPosition() {
        val info: TileInfo = MapBox("")
        val point2D = Point2D(70269.1555555, 43295.0599511)

        val actual = GeoUtil.pixelToGeo(point2D, 10, info)

        assertEquals(13.0, actual.getLongitude(), PRECISION)
        assertEquals(52.0, actual.getLatitude(), PRECISION)
    }

    @Test fun boundingBoxReturnsRectangle2D() {
        val info: TileInfo = MapBox("")
        val factory = TileFactory(info)
        val positions = getPositions()

        val actual = GeoUtil.boundingBox(positions, factory, zoom)

        assertEquals(65900.0888888, actual.minX, PRECISION)
        assertEquals(67356.4444444, actual.maxX, PRECISION)
        assertEquals(63713.2405672, actual.minY, PRECISION)
        assertEquals(65171.8926250, actual.maxY, PRECISION)
    }

    @Test fun getMapBounds() {
    }

    private fun getPositions(): Set<GeoPosition> {
        val positions = HashSet<GeoPosition>()
        (1..5).mapTo(positions) { GeoPosition(it.toDouble(), it.toDouble()) }
        return positions
    }
}