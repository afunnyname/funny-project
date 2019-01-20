package co.joyship.FunWithImagesServer

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class HelloController {
  
  @PostMapping("/hello")
  fun sayHello(@RequestParam("name") name: String?): String {
    return name?.let { "Hello $name" } ?: run { "Hello world" }
  }
  
}