package nice.fontaine.map

import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import nice.fontaine.models.GeoPosition
import nice.fontaine.processors.TileFactory

interface MapContract {

    interface View {
        fun draw()
        fun getCanvasBounds(): Bounds
    }

    interface Presenter {
        fun drawTiles(bounds: Rectangle2D)

        fun getAddress(): GeoPosition
        fun focusAddress(addressLocation: GeoPosition)

        fun getZoom(): Int
        fun setZoom(zoom: Int)

        fun getCenterPosition(): GeoPosition
        fun zoomToBestFit(positions: Set<GeoPosition>, maxFraction: Double, width: Double, height: Double)

        fun getCenter(): Point2D
        fun setCenter(center: Point2D)

        fun calculateViewportBounds(width: Double, height: Double): Rectangle2D

        fun getTileFactory(): TileFactory
    }
}