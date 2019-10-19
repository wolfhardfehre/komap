package nice.fontaine.views

import nice.fontaine.models.CCoord

abstract class Graphic(internal val overlay: GraphicOverlay) : View {
    abstract fun moveTo(coord: CCoord)
}
