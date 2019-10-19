package nice.fontaine.views

import nice.fontaine.models.CCoord
import nice.fontaine.utils.toImage
import java.awt.Graphics
import java.awt.Image
import java.util.concurrent.atomic.AtomicBoolean

const val DEFAULT_IMAGE = "images/loading.png"


class TileGraphic(
        val url: String,
        private var coord: CCoord,
        overlay: GraphicOverlay
) : Graphic(overlay) {
    private var loading = AtomicBoolean(false)
    private var image: Image = DEFAULT_IMAGE.toImage()
    val isLoading: Boolean
        get() = loading.get()

    override fun invalidate() {
        overlay.invalidate()
    }

    override fun draw(context: Graphics) {
        context.drawImage(image, coord.x, coord.y, null)
    }

    override fun moveTo(coord: CCoord) {
        this.coord = coord
    }

    fun changeImage(image: Image) {
        this.image = image
        loading.set(false)
        invalidate()
    }

    fun setLoading() {
        loading.set(true)
    }
}
