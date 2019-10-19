package nice.fontaine

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nice.fontaine.map.MapCanvas
import nice.fontaine.map.MapPresenter
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.TileInfo
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.processors.TileFactory
import org.junit.Before
import org.junit.Test
import java.awt.geom.Point2D

class MapPresenterTest {
    private val canvas: MapCanvas = mockk(relaxed = true)
    private val factory: TileFactory = mockk(relaxed = true)
    private val overlay: GraphicOverlay = mockk(relaxed = true)
    private val info: TileInfo = mockk(relaxed = true)
    private lateinit var presenter: MapPresenter

    @Before fun setup() {
        presenter = MapPresenter(canvas, factory, overlay)
    }

    @Test fun `should get address when focus changed`() {
        val position: GeoPosition = mockk()
        val pixel = Point2D.Double(0.0, 0.0)
        every { factory.geoToPixel(position, 16) } returns pixel

        presenter.focusAddress(position)

        verify { factory.geoToPixel(position, 16) }
        verify { canvas.draw() }
        assertThat(presenter.getAddress()).isEqualTo(position)
    }

    @Test fun `should have correct center when set`() {
        val point2D: Point2D = mockk()

        presenter.setCenter(point2D)

        assertThat(presenter.getCenter()).isEqualTo(point2D)
    }

    @Test fun `should get zoom when zoom changed`() {
        every { info.getMinZoom() } returns 0
        every { info.getMaxZoom() } returns 20
        every { factory.info() } returns info

        presenter.setZoom(4)

        assertThat(presenter.getZoom()).isEqualTo(4)
    }
}
