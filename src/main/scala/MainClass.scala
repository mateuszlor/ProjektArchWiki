/**
  * Created by marek on 24.06.16.
  */

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

object MainClass {
 def main(args: Array[String]){
    println("hello")
//   println(getListOfLinks("https://wiki.archlinux.org/index.php/Secure_Boot#Put_firmware_in_.22Setup_Mode.22"))
//   print(getLinkToRandomPage())

//    val o = new MTree("a" , Some(new MTree("b" )))
//   print(o.getParents())

   prepareLinks(getListOfLinks(getLinkToRandomPage())).foreach(println)
  }

  def getListOfLinks(url : String) : List[Option[String]] = {
    val browser = JsoupBrowser()
    val doc = browser.get(url)
    val items: List[Element] = doc >> elementList("a")
    items.map(_ >?> attr("href")("a"))
  }

  def getLinkToRandomPage() : String = {
    val browser = JsoupBrowser()
    browser.get("https://wiki.archlinux.org/index.php/Special:Random").location
  }

  def prepareLinks(list : List[Option[String]]) : List[String] = {
    val pat = new Regex("^/index.php/.*$")
    var l = new ListBuffer[Option[String]];
    list.foreach {
      case Some(x) =>  l +=  pat findFirstIn x
      case None => ""
    }
    l.toList.flatten
  }

  def checkValidLink(url : String) : Boolean = {
    val browser = JsoupBrowser()
    try {
      val doc = browser.get(url)
      val link = doc.location
      val items: List[Element] = doc >> elementList("p")
      link.contains("wiki.archlinux.org")
    }
    catch {
      case e: Exception => false
    }
  }
}
