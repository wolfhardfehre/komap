package nice.fontaine.views

import nice.fontaine.models.CCoord
import java.awt.Graphics
import java.awt.Image
import java.util.concurrent.atomic.AtomicBoolean
import javax.imageio.ImageIO

const val FILE_NAME = "images/loading.png"

class TileGraphic(
        private val url: String,
        private var coord: CCoord,
        overlay: GraphicOverlay
) : Graphic(overlay) {
    private var loading = AtomicBoolean(false)
    private val default_image = object {}::class.java.classLoader.getResource(FILE_NAME)
    private var image: Image = ImageIO.read(default_image)

    override fun invalidate() {
        overlay.invalidate()
    }

    override fun contains(other: CCoord): Boolean = coord.x < other.x &&
            coord.x + image.getWidth(null) > other.x &&
            coord.y < other.y && coord.y + image.getHeight(null) > other.y

    override fun draw(context: Graphics) {
        context.drawImage(image, coord.x, coord.y, null)
    }

    override fun moveTo(coord: CCoord) {
        this.coord = coord
    }

    fun change(image: Image) {
        this.image = image
        loading.set(false)
        invalidate()
    }

    fun url(): String = url

    fun loading(): Boolean = loading.get()

    fun setLoading() {
        loading.set(true)
    }
}
