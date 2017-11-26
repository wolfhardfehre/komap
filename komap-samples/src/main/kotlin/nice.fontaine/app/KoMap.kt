package nice.fontaine.app

import javafx.application.Application
import nice.fontaine.view.MapView
import tornadofx.App

class KoMap : App(MapView::class, Styles::class)

fun main(args: Array<String>) {
    Application.launch(KoMap::class.java, *args)
}