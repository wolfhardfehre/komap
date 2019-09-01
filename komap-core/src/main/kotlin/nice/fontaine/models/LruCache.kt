package nice.fontaine.models

class LruCache<K, V>(private val capacity: Int) : LinkedHashMap<K, V>(capacity + 1, 1.0f, true) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K,V>): Boolean {
        return size > this.capacity
    }
}
