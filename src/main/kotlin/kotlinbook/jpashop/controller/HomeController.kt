package kotlinbook.jpashop.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HomeController {
    @RequestMapping("/")
    fun home(): String {
//        log.info("home controller")
        return "home"
    }
}