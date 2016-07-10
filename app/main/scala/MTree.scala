package main.scala

import scala.None

/**
  * Created by marek on 24.06.16.
  */
case class MTree(url : String, parent: Option[MTree] = None){

  def getParents() : List[String] = {
    parent match {
      case Some(parent) => url :: parent.getParents()
      case None =>  List(url)
    }
  }
}