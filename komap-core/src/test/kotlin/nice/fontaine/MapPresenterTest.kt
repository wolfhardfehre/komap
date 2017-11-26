package nice.fontaine

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import nice.fontaine.map.MapCanvas
import nice.fontaine.map.MapPresenter
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.TileInfo
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.processors.TileFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class MapPresenterTest {

    private val view: MapCanvas = mock()
    private val factory: TileFactory = mock()
    private val overlay: GraphicOverlay = mock()
    private val info: TileInfo = mock()
    private val size: Dimension2D = mock()
    private lateinit var presenter: MapPresenter

    @Before
    fun setup() {
        presenter = MapPresenter(view, factory, overlay)
    }

    @Test
    fun setFocus() {
        // given
        val position: GeoPosition = mock()
        Mockito.`when`(factory.geoToPixel(position, 10)).thenReturn(Point2D(0.0, 0.0))

        // when
        presenter.focusAddress(position)

        // then
        verify(factory, times(1)).geoToPixel(position, 10)
        verify(view, times(1)).draw()
        Assert.assertEquals(position, presenter.getAddress())
    }

    @Test
    fun setCenter() {
        // given
        val point2D: Point2D = mock()

        // when
        presenter.setCenter(point2D)

        // then
        verify(view, times(1)).draw()
        Assert.assertEquals(point2D, presenter.getCenter())
    }

    @Test
    fun setZoom() {
        // given
        val spy = spy(presenter);
        Mockito.`when`(size.width).thenReturn(200.0)
        Mockito.`when`(size.height).thenReturn(200.0)
        Mockito.`when`(info.getMaxZoom()).thenReturn(20)
        Mockito.`when`(info.getMinZoom()).thenReturn(1)
        Mockito.`when`(factory.getInfo()).thenReturn(info)
        Mockito.`when`(factory.getMapSize(4)).thenReturn(size)
        Mockito.`when`(factory.getMapSize(10)).thenReturn(size)
        Mockito.`when`(spy.getCenter()).thenReturn(Point2D(10.0, 10.0))

        // when
        spy.setZoom(4)

        // then
        Assert.assertEquals(4, spy.getZoom())
    }
}