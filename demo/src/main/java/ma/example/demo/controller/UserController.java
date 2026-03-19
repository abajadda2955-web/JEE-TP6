package ma.example.demo.controller;

import jakarta.validation.Valid;
import ma.example.demo.entity.User;
import ma.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //  afficher liste
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    //  formulaire ajout
    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    // ajouter user
    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result) {
        System.out.println(">>> CONTROLLER CALLED <<<");

        System.out.println(" NAME = " + user.getName());
        System.out.println(" EMAIL = " + user.getEmail());

        if (result.hasErrors()) {
            System.out.println(" ERRORS !!!");
            return "add-user";
        }

        userRepository.save(user);
        System.out.println(" SAVED !!!");

        return "redirect:/";
    }

    //  afficher update
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    //  update user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRepository.save(user);
        return "redirect:/";
    }

    //  delete
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        userRepository.delete(user);
        return "redirect:/";
    }
}