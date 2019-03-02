package co.joyship.FunWithImagesServer

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO


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

//        val body = Files.readAllBytes(File("cutecat.png").toPath())
        val body = grayScaleImage(file)
        return ResponseEntity(body, HttpHeaders(), HttpStatus.OK)
    }

    private fun grayScaleImage(file: MultipartFile): ByteArray {


        val imageFile = file.bytes.inputStream()
        val image = ImageIO.read(imageFile)

            val width = image.width
            val height = image.height

            for (i in 0 until height) {

                for (j in 0 until width) {

                    val c = Color(image.getRGB(j, i))
                    val red = (c.red * 0.299).toInt()
                    val green = (c.green * 0.587).toInt()
                    val blue = (c.blue * 0.114).toInt()
                    val newColor = Color(red + green + blue,

                            red + green + blue, red + green + blue)

                    image.setRGB(j, i, newColor.rgb)
                }
            }
        return bufferedImageToByteArray(image)

    }

    private fun bufferedImageToByteArray(image: BufferedImage): ByteArray {
        val baos = ByteArrayOutputStream()
        ImageIO.write(image, "png", baos)
        baos.flush()
        val imageInByte = baos.toByteArray()
        baos.close()
        return imageInByte
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
