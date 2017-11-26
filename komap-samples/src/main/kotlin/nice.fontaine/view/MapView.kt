package nice.fontaine.view

import nice.fontaine.app.MAPBOX_TOKEN
import nice.fontaine.mapcanvas
import nice.fontaine.models.GeoPosition
import nice.fontaine.models.MapBox
import nice.fontaine.processors.TileFactory
import tornadofx.*

class MapView : View("KoMaP") {

    private val info = MapBox(MAPBOX_TOKEN, MapBox.Base.LIGHT)
    private val frankfurt = GeoPosition(50.11, 8.68)

    override val root = stackpane {
        mapcanvas(TileFactory(info), 600.0, 600.0) {
            setFocus(frankfurt)
        }
    }
}
