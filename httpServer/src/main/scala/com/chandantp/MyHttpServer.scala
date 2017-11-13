package com.chandantp

import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpServer}

object MyHttpServer {

  def main(args: Array[String]) {

    val server = HttpServer.create(new InetSocketAddress(8000), 0)

    server.createContext("/", (hex: HttpExchange) => {
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
      println("Request Method: " + hex.getRequestMethod)
      println("Request URI: " + hex.getRequestURI)
      println("Request Body:")
      Iterator
        .continually(hex.getRequestBody.read)
        .takeWhile(_ != -1)
        .foreach(System.out.write)
      println
      println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

      val response = "Received OK!"
      hex.getResponseBody.write(response.getBytes)
      hex.getResponseBody.close
      hex.sendResponseHeaders(200, response.length)
      hex.close
    })

    server.setExecutor(null) // creates a default executor
    server.start

    println("Listening on localhost:8000 (press any key to exit) ....")
    System.in.read
    server.stop(0)
  }

}
