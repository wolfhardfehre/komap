package nice.fontaine.processors

import nice.fontaine.views.TileGraphic
import java.net.URL
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import javax.imageio.ImageIO

class TileLoader {
    private val threadPoolSize = Runtime.getRuntime().availableProcessors()
    private val service = Executors.newFixedThreadPool(threadPoolSize, TileThreadFactory())
    private val tileQueue: BlockingQueue<TileGraphic> = LinkedBlockingQueue<TileGraphic>()

    fun load(tile: TileGraphic) {
        if (tile.isLoading) return
        tile.setLoading()
        try {
            tileQueue.put(tile)
            service.submit(TileRunner())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private inner class TileThreadFactory : ThreadFactory {
        private var count = 0

        override fun newThread(runnable: Runnable): Thread {
            val thread = Thread(runnable, "tile-pool-" + count++)
            thread.priority = Thread.MIN_PRIORITY
            thread.isDaemon = true
            return thread
        }
    }

    private inner class TileRunner : Runnable {
        override fun run() {
            val tile = tileQueue.remove()
            val url = URL(tile.url)
            val image = ImageIO.read(url)
            tile.changeImage(image)
        }
    }
}
