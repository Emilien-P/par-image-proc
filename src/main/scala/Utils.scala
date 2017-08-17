import java.awt.Color

object Utils {
  case class rgbColor(r: Int, g:Int, b:Int)

  def separateRGB(rgb : Int) : Color = {
    val r = rgb / (256 * 256)
    val g = rgb / 256 - r
    val b = rgb % 256
    new Color(r, g, b)
  }
}
