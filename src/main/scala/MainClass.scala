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

//    play("https://wiki.archlinux.org/index.php/Daemons_(%D0%A0%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9)", "https://wiki.archlinux.org/index.php/Category:Kernel")
    play(getLinkToRandomPage(), getLinkToRandomPage())
  }

  def play( url1 :String , url2: String) = {
    if(!url1.equals(url2))
      {
        var root = new Some(MTree(url1))
        var tmpListOfUrl = prepareLinks(getListOfLinks(root.get.url))

        var listaWszystkichTree = new ListBuffer[MTree]
        var listaTMP = new ListBuffer[MTree]

        var exit = false
        var licznik = 0
        while(!exit){
          println("petla, licznik="+licznik)
          listaTMP ++=  createTreeListFromList( tmpListOfUrl,root)
          listaTMP = listaTMP.distinct

          listaTMP.foreach{x => if(x.url.equals(url2)) {println("\t\t KONIEC") ; println(url2+"\t" +x.getParents()+"\t" +url1) ; exit = true} }
          listaWszystkichTree ++= listaTMP
          listaWszystkichTree = listaWszystkichTree.distinct

          listaTMP.clear()
          licznik=licznik+1
          root = Some(listaWszystkichTree.toList(licznik))
          try {
            tmpListOfUrl = prepareLinks(getListOfLinks(root.get.url))
          }
          catch { case e: Exception => print("BLAD")}
        }
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
    val pattern = new Regex("^/index.php/.*$")
    var l = new ListBuffer[Option[String]];
    list.foreach {
      case Some(x) =>  l +=  pattern findFirstIn x
      case None => ""
    }
    val output = new ListBuffer[String];
    l.toList.flatten.foreach( x =>  output+=("https://wiki.archlinux.org"+x))
    output.toList
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
