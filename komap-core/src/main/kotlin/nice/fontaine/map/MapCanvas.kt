package nice.fontaine.map

import nice.fontaine.listeners.PanListener
import nice.fontaine.listeners.ZoomClickListener
import nice.fontaine.listeners.ZoomScrollListener
import nice.fontaine.models.GeoPosition
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.processors.TileFactory
import java.awt.Rectangle
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import javax.swing.JFrame

class MapCanvas(
        factory: TileFactory,
        width: Int,
        height: Int
) : JFrame(), MapContract.View {
    private val overlay = GraphicOverlay(this)
    private val presenter: MapContract.Presenter = MapPresenter(this, factory, overlay)

    init {
        setSize(width, height)
        setLocationRelativeTo(null)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
        addMouseWheelListener(ZoomScrollListener(this))
        addMouseListener(ZoomClickListener(this))
        val pan = PanListener(this)
        addMouseListener(pan)
        addMouseMotionListener(pan)
    }

    @Synchronized override fun draw() {
        val viewportBounds = getViewportBounds()
        presenter.drawTiles(viewportBounds)
    }

    override fun getCanvasBounds(): Rectangle = bounds

    fun setZoom(zoom: Int) {
        presenter.setZoom(zoom)
    }

    fun getZoom(): Int = presenter.getZoom()

    fun getFocus(): GeoPosition = presenter.getAddress()

    fun setFocus(position: GeoPosition) {
        presenter.focusAddress(position)
    }

    fun getCenter(): Point2D = presenter.getCenter()

    fun setCenter(center: Point2D) {
        presenter.setCenter(center)
    }

    fun getViewportBounds(): Rectangle2D = presenter.calculateViewportBounds(width, height)

    fun getTileFactory(): TileFactory = presenter.getTileFactory()
}
