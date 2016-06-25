/**
  * Created by marek on 24.06.16.
  */

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.collection.immutable.List
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

  def play( url1 :String , url2: String) = {
    if(!url1.equals(url2))
      {
        val root  = new Some(MTree(url1))
        val tmpListOfUrl = prepareLinks(getListOfLinks(root.get.url))
        val currentLevel = new ListBuffer[MTree] ;
        val tmpList = new ListBuffer[MTree] ;
        tmpList :: createTreeListFromList( tmpListOfUrl,root)
        tmpList.foreach{ x => if(x.equals(currentLevel)) x = None}
        tmpList.foreach{ x => if(x.url.equals(url2)) print(x.getParents())}
        currentLevel :: tmpList.toList
      }
  }

  def getListOfLinks(url : String) : List[Option[String]] = {
    val browser = JsoupBrowser()
    val doc = browser.get(url)
    val items: List[Element] = doc >> elementList("a")
    items.map(_ >?> attr("href")("a"))
  }

  def createTreeListFromList(inputList : List[String], mTree: Option[MTree]) : List[MTree] = {
    val list = new ListBuffer[MTree]
    inputList.foreach(x => list += new MTree(x , mTree))
    list.toList
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
    val ww = new ListBuffer[String];
    val pp = l.toList.flatten.foreach( x =>  ww+=("https://wiki.archlinux.org"+x))
    ww.toList
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
