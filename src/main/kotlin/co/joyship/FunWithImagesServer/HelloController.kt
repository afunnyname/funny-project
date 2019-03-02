package co.joyship.FunWithImagesServer

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files


@Suppress("unused")
@RestController
class HelloController {

    @PostMapping("/hello")
    fun sayHello(@RequestParam("name") name: String?): String {
        return name?.let { "Hello $name" } ?: run { "Hello world" }
    }

    @PostMapping("/send-file")
    fun sendFile(@RequestParam("file") file: MultipartFile): ResponseEntity<ByteArray> {
        saveFile(file)
        val body = Files.readAllBytes(File("cutecat.png").toPath())
        return ResponseEntity(body, HttpHeaders(), HttpStatus.OK)
    }

    fun saveFile(file: MultipartFile): File {
        val convFile = File(file.originalFilename!!)
        convFile.createNewFile()
        val fos = FileOutputStream(convFile)
        fos.write(file.bytes)
        fos.close()
        return convFile
    }
}
