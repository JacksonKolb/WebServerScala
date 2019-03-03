import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import java.io._
import java.util.Scanner
import java.lang.StringBuilder

/* This code was adapted from the Akka documentation. */

object WebServer {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("datboi")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    while (true) {

    	println("Hey betch, enter a file name:")		
		
		val input = StdIn.readLine()

		val html = string_to_file(input)
		println(html)			
		
		val path_suffix = "demo"

		val route = path(path_suffix) {
    		get {
      			complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
    		}
  		}
  		val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)
	
  		println("Server online at http://localhost:8000/" + path_suffix)

  		StdIn.readLine()
    }    
  }


  def string_to_file(file_name: String): String = {

  	val path = "C:\\Users\\drewe\\Desktop\\ScalaServer\\src\\main\\scala\\" + file_name
  	var file = new File("")

  	try {
  		file = new File(path)
  	}
  	catch  {
  		case e: Exception => println("File not found: " + path)
  	}
  	
  	val scanner = new Scanner(file)
  	val builder = new StringBuilder()

  	while(scanner.hasNext()) {
  		builder.append(scanner.next())
  	}

  	return builder.toString()
  }
}