import java.awt.Color
import java.awt.image.BufferedImage

import ImagePanel._

object procUtils {
  def imgToPixels(img : BufferedImage) : Array[Color] = {
    val pixels = new Array[Color](imgHeight * imgWidth)
    for(x <- 0 until imgWidth; y <- 0 until imgHeight)
      pixels(index(x, y)) = new Color(img.getRGB(x, y))
    pixels
  }

  def pixelsToImg(pixels : Array[Color]) : BufferedImage = {
    val img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB)

    for(x <- 0 until imgWidth; y <- 0 until imgHeight)
      img.setRGB(x, y, pixels(index(x, y)).getRGB)
    img
  }

  def thresholdHSB(inputPixels : Array[Color], minH : Int, maxH: Int, minS : Int, maxS : Int, minB : Int, maxB : Int) : Unit = {
    val hsb = new Array[Float](3)
    for(i <- 0 until resolution) {
      Color.RGBtoHSB(inputPixels(i).getRed, inputPixels(i).getGreen, inputPixels(i).getBlue, hsb)
      if(hsb(0) >= minH / 255.0 && hsb(0) <= maxH / 255.0
        && hsb(1) >= minS / 255.0 && hsb(1) <= maxS / 255.0
        && hsb(2) >= minB / 255.0 && hsb(2) <= maxB / 255.0){
        inputPixels(i) = Color.white
      }else{
        inputPixels(i) = Color.black
      }
    }
  }

  def convolute(pixels : Array[Color], kernel : Array[Array[Float]]) : Unit = {
    val pixelsCopy = new Array[Color](pixels.length)
    pixels.copyToArray(pixelsCopy)

    val bright = pixels.map(c => Color.RGBtoHSB(c.getRed, c.getBlue, c.getGreen, null)(2))

    val normFactor = kernel.toList.flatten.sum
    val kernelSize = kernel.length //kernel must be squared in this case
    for(x <- 0 until imgWidth; y <- 0 until imgHeight){
      pixels.update(index(x, y), Color.getHSBColor(0, 0, getBrightness(bright, kernel, normFactor, x, y)))
    }
  }

  def getBrightness(bright: Array[Float], kernel : Array[Array[Float]], normFactor : Float, x : Int, y : Int) : Float = {
    val kernelSize : Int = kernel.length
    var brightness : Float = 0
    for(dx <- -(kernelSize / 2) to kernelSize / 2){
      for(dy <- -(kernelSize / 2) to kernelSize / 2){
        val kerVal = kernel(kernelSize / 2 + dx)(kernelSize / 2 + dy)
        brightness += bright(index(dx + x, dy + y)) * kerVal
      }
    }
    brightness / normFactor
  }

  def index(x : Int, y : Int) : Int = {
    val xc = if(x < 0) 0 else if (x >= imgWidth) imgWidth - 1 else x
    val yc = if(y < 0) 0 else if (y >= imgHeight) imgHeight - 1 else y
    xc + yc * imgWidth
  }

  def scharr(pixels : Array[Color]) : Array[Color] = {
    val vKernel : Array[Array[Float]] = Array(Array(3, 0, -3),Array(10, 0, -10), Array(3, 0, -3))
    val hKernel : Array[Array[Float]] = Array(Array(3, 10, 3), Array(0, 0, 0), Array(-3, -10, -3))
    val bright = pixels.map(c => Color.RGBtoHSB(c.getRed, c.getBlue, c.getGreen, null)(2))
    val result = Array.fill[Color](resolution)(Color.black)
    val buffer = new Array[Float](resolution)

    var max : Float = 0
    for(x <- 0 to imgWidth; y <- 0 to imgHeight){
      val sum_h = getBrightness(bright, hKernel, 1f, x, y)
      val sum_v = getBrightness(bright, vKernel, 1f, x, y)

      val sum : Float = Math.sqrt(Math.pow(sum_h, 2) + Math.pow(sum_v, 2)).toFloat
      max = if(max < sum) sum else max
      buffer(index(x, y)) =  sum
    }

    for(x <- 2 to imgWidth - 2; y <- 2 to imgHeight - 2){
      result.update(index(x, y), Color.getHSBColor(0, 0, buffer(index(x, y)) / max))
    }
    result
  }
}