package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.repository.LikeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class LikeController {
    @Autowired
    private lateinit var likeRepository : LikeRepository

//    GET /posts/{post-id}/likes



//    GET /posts/{post-id}/likes/count

//    GET /likes/{like-id}

//    POST /posts/{post-id}/like
}