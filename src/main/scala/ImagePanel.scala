import java.awt._
import javax.swing.JPanel

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
  * Created by emilien on 4/15/17.
  */
object ImagePanel extends JPanel{

  val img : BufferedImage = ImageIO.read(new File("resources/fish.jpg"))
  val imgHeight : Int= img.getHeight()
  val imgWidth : Int= img.getWidth()
  val resolution : Int = imgWidth * imgHeight
  val blur : Array[Array[Float]] = Array(Array(5, 8, 5), Array(8, 15, 8), Array(5, 8, 5))

  val pixels = procUtils.imgToPixels(img)
  procUtils.thresholdHSB(pixels, 0, 100, 0, 255, 0, 250)
  procUtils.convolute(pixels, blur)
  val out = procUtils.pixelsToImg(pixels)



  override def paintComponent(graphics: Graphics): Unit = {
    super.paintComponent(graphics)

    graphics.drawImage(out, 0, 0, null)
  }
}