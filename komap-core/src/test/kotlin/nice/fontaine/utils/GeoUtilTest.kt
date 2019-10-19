package nice.fontaine.utils

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.MapBox
import nice.fontaine.models.TileInfo
import nice.fontaine.processors.TileFactory
import org.junit.Test
import java.awt.geom.Point2D


private const val PRECISION = 1e-7

class GeoUtilTest {
    private val zoom = 10
    private val info: TileInfo = mockk(relaxed = true)
    private val positions: Set<GeoPosition>
        get() = (1..5).map { GeoPosition(it.toDouble(), it.toDouble()) }.toSet()

    @Test
    fun `should get correct dimensions when`() {
        val mapWidthInTiles = 5
        every { info.mapWidthInTilesAt(zoom) } returns mapWidthInTiles

        val dimen = GeoUtil.getMapSize(zoom, info)

        assertThat(dimen.height).isEqualTo(mapWidthInTiles)
        assertThat(dimen.width).isEqualTo(mapWidthInTiles)
    }

    @Test
    fun `should not be a valid tile when x and y negative`() {
        val (x, y) = Pair(-1, -1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertThat(valid).isFalse()
        verify(exactly = 0) { info.centerPxAt(zoom) }
    }

    @Test
    fun `should not be a valid tile when only y negative`() {
        val (x, y) = Pair(1, -1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertThat(valid).isFalse()
        verify(exactly = 0) { info.centerPxAt(zoom) }
    }

    @Test
    fun `should not be a valid tile when only x negative`() {
        val (x, y) = Pair(-1, 1)

        val valid = GeoUtil.isValidTile(x, y, zoom, info)

        assertThat(valid).isFalse()
        verify(exactly = 0) { info.centerPxAt(zoom) }
    }

    @Test
    fun `should not be a valid tile when center x too small`() {
        val info: TileInfo = spyk(MapBox(""))
        val (x, y) = Pair(2, 1)

        val valid = GeoUtil.isValidTile(x, y, 19, info)

        assertThat(valid).isFalse()
        verify { info.centerPxAt(19) }
    }

    @Test
    fun `should not be a valid tile when center y too small`() {
        val info: TileInfo = spyk(MapBox(""))
        val (x, y) = Pair(0, 2)

        val valid = GeoUtil.isValidTile(x, y, 19, info)

        assertThat(valid).isFalse()
        verify(exactly = 2) { info.centerPxAt(19) }
    }

    @Test
    fun `should get correct pixel coordinate when geo position inserted`() {
        val info: TileInfo = MapBox("")
        val position = GeoPosition(52.0, 13.0)

        val actual = GeoUtil.geoToPixel(position, 10, info)

        assertAll {
            assertThat(actual.x).isCloseTo(70269.1555555, PRECISION)
            assertThat(actual.y).isCloseTo(43295.0599511, PRECISION)
        }
    }

    @Test
    fun `should clamp to lower border when value lower range`() {
        val actual = GeoUtil.clamp(-6.0, -5.0, 5.0)

        assertThat(actual).isEqualTo(-5.0)
    }

    @Test
    fun `should clamp to higher border when value higher range`() {
        val actual = GeoUtil.clamp(6.0, -5.0, 5.0)

        assertThat(actual).isEqualTo(5.0)
    }

    @Test
    fun `should stay same when value in range`() {
        val actual = GeoUtil.clamp(3.0, -5.0, 5.0)

        assertThat(actual).isEqualTo(3.0)
    }

    @Test
    fun `should get geo position when pixel entered`() {
        val info: TileInfo = MapBox("")
        val point2D = Point2D.Double(70269.1555555, 43295.0599511)

        val actual = GeoUtil.pixelToGeo(point2D, 10, info)

        assertAll {
            assertThat(actual.getLongitude()).isCloseTo(13.0, PRECISION)
            assertThat(actual.getLatitude()).isCloseTo(52.0, PRECISION)
        }
    }

    @Test
    fun boundingBoxReturnsRectangle2D() {
        val info: TileInfo = MapBox("")
        val factory = TileFactory(info)

        val actual = GeoUtil.boundingBox(positions, factory, zoom)

        assertAll {
            assertThat(actual.minX).isCloseTo(65900.0888888, PRECISION)
            assertThat(actual.maxX).isCloseTo(67356.4444444, PRECISION)
            assertThat(actual.minY).isCloseTo(63713.2405672, PRECISION)
            assertThat(actual.maxY).isCloseTo(65171.8926250, PRECISION)
        }
    }
}
