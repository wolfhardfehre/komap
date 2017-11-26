package nice.fontaine

import javafx.event.EventTarget
import nice.fontaine.map.MapCanvas
import nice.fontaine.processors.TileFactory
import tornadofx.*

fun EventTarget.mapcanvas(factory: TileFactory,
                          width: Double = 0.0,
                          height: Double = 0.0, op: (MapCanvas.() -> Unit)? = null) =
        opcr(this, MapCanvas(factory, width, height), op)