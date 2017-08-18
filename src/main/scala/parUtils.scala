import java.awt.Color
import java.awt.image.BufferedImage
import ImagePanel._

import scala.collection.parallel._
import scala.collection.parallel.mutable.ParArray

object parUtils {
  def imgToPixels(img : BufferedImage) : ParArray[(Int, Color)] ={
    val pixels : ParArray[Int] = (0 until resolution).toParArray //new ParArray[Color](resolution)
    pixels.map(idx => (idx, new Color(img.getRGB(idx % imgWidth, idx / imgWidth))))
  }

  def pixelsToImg(pixels : ParArray[(Int, Color)]) : BufferedImage = {
    val img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)
    pixels.foreach{case (idx, color) => img.setRGB(idx % imgWidth, idx / imgWidth, color.getRGB)}
    img
  }
}
