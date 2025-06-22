package me.iliasse.gestion_produits.controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("activePage", "login");
        return "views/auth/login";
    }
}
