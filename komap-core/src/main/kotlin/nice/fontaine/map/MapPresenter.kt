package nice.fontaine.map

import nice.fontaine.models.GeoPosition
import nice.fontaine.processors.TileProcessor
import nice.fontaine.utils.GeoUtil
import nice.fontaine.views.GraphicOverlay
import nice.fontaine.processors.TileFactory
import java.awt.geom.Dimension2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D

class MapPresenter(
        private val canvas: MapCanvas,
        private val factory: TileFactory,
        private val overlay: GraphicOverlay
) : MapContract.Presenter {
    private var zoom: Int = 16
    private var address = GeoPosition(52.474476, 13.402944)
    private var center = factory.geoToPixel(address, zoom)
    private val processor: TileProcessor = TileProcessor(factory, overlay, canvas)

    override fun drawTiles(bounds: Rectangle2D) {
        processor.process(bounds, zoom)
        val context = canvas.graphics
        context.clearRect(0, 0, bounds.width.toInt(), bounds.height.toInt())
        overlay.draw(context)
    }

    override fun getTileFactory(): TileFactory = factory

    override fun getAddress(): GeoPosition = address

    override fun focusAddress(addressLocation: GeoPosition) {
        this.address = addressLocation
        val center = factory.geoToPixel(addressLocation, zoom)
        setCenter(center)
        canvas.draw()
    }

    override fun getZoom(): Int = zoom

    override fun setZoom(zoom: Int) {
        if (zoom == this.zoom || zoomOutOfRange(zoom)) return
        recomputeCenter(zoom)
        this.zoom = zoom
    }

    private fun zoomOutOfRange(zoom: Int): Boolean {
        val info = factory.info()
        return !info.isValidZoom(zoom)
    }

    private fun recomputeCenter(zoom: Int) {
        val mapSize = factory.mapSize(zoom)
        val oldMapSize = factory.mapSize(this.zoom)

        val oldCenter = getCenter()
        val newCenter = computeCenter(oldCenter, mapSize, oldMapSize)
        setCenter(newCenter)
    }

    private fun computeCenter(oldCenter: Point2D, mapSize: Dimension2D, oldMapSize: Dimension2D): Point2D =
            Point2D.Double(
                oldCenter.x * (mapSize.width / oldMapSize.width),
                oldCenter.y * (mapSize.height / oldMapSize.height)
            )

    override fun getCenterPosition(): GeoPosition = factory.pixelToGeo(getCenter(), zoom)

    override fun getCenter(): Point2D = center

    override fun setCenter(center: Point2D) {
        this.center = center
    }

    override fun calculateViewportBounds(width: Int, height: Int): Rectangle2D {
        val (viewportX, viewportY) = Pair(center.x - width / 2, center.y - height / 2)
        return Rectangle2D.Double(viewportX, viewportY, width.toDouble(), height.toDouble())
    }

    override fun zoomToBestFit(positions: Set<GeoPosition>, maxFraction: Double, width: Int, height: Int) {
        if (positions.isEmpty()) return
        if (maxFraction <= 0 || maxFraction > 1) throw IllegalArgumentException("maxFraction must be between 0 and 1")

        val info = factory.info()
        var bounds = generateBoundingRect(positions)

        val (centerX, centerY) = Pair(bounds.minX + bounds.width / 2, bounds.minY + bounds.height / 2)
        val bc = Point2D.Double(centerX, centerY)
        val center = factory.pixelToGeo(bc, getZoom())
        setCenterPosition(center)

        if (positions.size == 1) return
        setZoom(info.maxZoom)
        var bestZoom = getZoom()
        val viewport = calculateViewportBounds(width, height)
        while (true) {
            bounds = generateBoundingRect(positions)
            if (bounds.width < viewport.width * maxFraction && bounds.height < viewport.height * maxFraction) {
                bestZoom = getZoom()
            } else break
            if (getZoom() == info.minZoom) break
            else setZoom(getZoom() - 1)
        }
        setZoom(bestZoom)
    }

    private fun generateBoundingRect(positions: Set<GeoPosition>): Rectangle2D =
            GeoUtil.boundingBox(positions, factory, zoom)

    private fun setCenterPosition(geoPosition: GeoPosition) {
        setCenter(factory.geoToPixel(geoPosition, zoom))
    }
}
