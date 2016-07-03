package controllers

import play.api.libs.json._
import play.api.mvc._
object Info extends Controller {

  def info = Action {

    val html ="""<html>
                  <head>
                    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
                  </head>
                  <body>
                    <div class="container jumbotron">
                      <div class="text-center">
                        <h1>ArchWiki</h1>
                        <h3>You can check shortest path between two pages using POST to /pages</h3>
                        <p>
                          <b>Example body:</b>
                        </p>
                      </div>
                      <div widht="300px">
                        <pre>{
  "first" = "https://wiki.archlinux.org/index.php/Special:Random"
  "second" = "https://wiki.archlinux.org/index.php/Special:Random"
}</pre>
                    </div>
                  </body>
                </html>""" 

    Ok(html).as("text/html")
  }
}
