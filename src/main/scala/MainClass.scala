/**
  * Created by marek on 24.06.16.
  */

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

object MainClass {
 def main(args: Array[String]){
    println("hello")
   println(getListOfLinks("https://wiki.archlinux.org/index.php/Secure_Boot#Put_firmware_in_.22Setup_Mode.22"))
  }

  def getListOfLinks(link : String) : List[Option[String]] = {
    val browser = JsoupBrowser()
    val doc = browser.get(link)
    val items: List[Element] = doc >> elementList("a")
    items.map(_ >?> attr("href")("a"))
  }
}
