package nice.fontaine.utils

import java.awt.Image
import java.net.URL
import javax.imageio.ImageIO

fun String.toImage(): Image = ImageIO.read(this.toUrl())

fun String.toUrl(): URL? = object {}::class.java.classLoader.getResource(this)
