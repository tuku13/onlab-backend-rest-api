package hu.tuku13.onlabrestapi.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@RestController
class FileController {

    @PostMapping("/images/upload")
    fun uploadFile(@RequestParam("file") multipartFile: MultipartFile): ResponseEntity<String> {
        val fileName = "/static/images/${UUID.randomUUID()}.png"

        val file = File(".${fileName}")
        file.writeBytes(multipartFile.bytes)

        return ResponseEntity.ok("localhost:8080${fileName}")
    }
}