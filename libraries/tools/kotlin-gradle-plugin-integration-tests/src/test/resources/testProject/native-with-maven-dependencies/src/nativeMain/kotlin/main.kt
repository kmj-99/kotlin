import okio.FileSystem
import okio.Path.Companion.toPath

fun main() {
    FileSystem.SYSTEM.delete("toto".toPath(), false)
}