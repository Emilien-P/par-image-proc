import javax.swing._
import java.awt._


/**
  * Created by emilien on 3/24/17.
  */
object Viewer{
  class ViewFrame extends JFrame {
    setName("Viewer JFrame")
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setSize(1000, 1000)
    setLayout(new BorderLayout)

    add(ImagePanel, BorderLayout.CENTER)

    setVisible(true)
  }
  val frame = new ViewFrame

  def main(args: Array[String]): Unit ={
    frame.repaint()
  }
}