package org.springframework.samples.petclinic.system


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
internal class WelcomeController {

    @RequestMapping("/")
    fun welcome(): String {
        return "welcome"
    }
}
