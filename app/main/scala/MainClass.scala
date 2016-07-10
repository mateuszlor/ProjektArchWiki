package main.scala

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

import javax.net.ssl._

object MainClass {

 def main(args: Array[String]) = {
    println("hello")

    //Disable SSL
    // SSL Context initialization and configuration
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, Array(TrustAll), new java.security.SecureRandom())
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier(VerifiesAllHostNames)

    //end

//    play("https://wiki.archlinux.org/index.php/Daemons_(%D0%A0%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9)", "https://wiki.archlinux.org/index.php/Category:Kernel")
    //play(getLinkToRandomPage(), getLinkToRandomPage())
    play("https://wiki.archlinux.org/index.php/OpenRC", "https://wiki.archlinux.org/index.php/Persistent_block_device_naming_(Espa%C3%B1ol)")
  }

  def play( url1 :String , url2: String) : List[String]  = {
    val browser = JsoupBrowser()
    val first = browser.get(url1).location
    val second = browser.get(url2).location
    var result = List(first)
    if(!first.equals(second))
      {
        var root = new Some(MTree(first))
        var tmpListOfUrl = prepareLinks(getListOfLinks(root.get.url))

        var listaWszystkichTree = new ListBuffer[MTree]
        var listaTMP = new ListBuffer[MTree]
        
        var exit = false
        var licznik = 0

        while(!exit){
          listaTMP ++=  createTreeListFromList(tmpListOfUrl, root)
          listaTMP = listaTMP.distinct

          println("petla " + licznik + ", liczba stron = " + listaTMP.length)

          listaTMP.foreach {
            x => 
            if(x.url.equals(second)) {
              println("\t\t KONIEC")
              result = x.getParents()
              println(result)
              exit = true
            } 
          }
          listaWszystkichTree ++= listaTMP
          listaWszystkichTree = listaWszystkichTree.distinct

          listaTMP.clear()
          licznik = licznik + 1
          root = Some(listaWszystkichTree.toList(licznik))
          try {
            tmpListOfUrl = prepareLinks(getListOfLinks(root.get.url))
          }
          catch { 
            case e: Exception => print("BLAD")
          }
        }
      }
      result
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
    val url = browser.get("https://wiki.archlinux.org/index.php/Special:Random").location
    // println("Got random link: " url)
    url
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
