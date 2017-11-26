package nice.fontaine.map

import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import nice.fontaine.listeners.PanListener
import nice.fontaine.listeners.ZoomClickListener
import nice.fontaine.listeners.ZoomScrollListener
import nice.fontaine.models.GeoPosition
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.processors.TileFactory

class MapCanvas(factory: TileFactory,
                width: Double,
                height: Double)
    : Canvas(width, height), MapContract.View {

    private val overlay = GraphicOverlay(this)
    private val presenter: MapContract.Presenter =
            MapPresenter(this, factory, overlay)

    init {
        onScroll = ZoomScrollListener(this)
        onMouseClicked = ZoomClickListener(this)
        val listener = PanListener(this)
        onMousePressed = listener
        onMouseDragged = listener
        onMouseReleased = listener
    }

    @Synchronized override fun draw() {
        val viewportBounds = getViewportBounds()
        presenter.drawTiles(viewportBounds)
    }

    override fun getCanvasBounds(): Bounds = layoutBounds

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

    override fun minHeight(width: Double): Double = 256.0

    override fun maxHeight(width: Double): Double = 5000.0

    override fun minWidth(height: Double): Double = 256.0

    override fun maxWidth(height: Double): Double = 5000.0

    override fun isResizable(): Boolean = true

    override fun resize(width: Double, height: Double) {
        super.setWidth(width)
        super.setHeight(height)
        draw()
    }
}