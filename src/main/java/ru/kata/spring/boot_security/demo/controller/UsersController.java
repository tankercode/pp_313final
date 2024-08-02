package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.config.DbInit;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UsersServiceImp;

@Controller
public class UsersController {

    private final UsersServiceImp usersServiceImp;

    @Autowired
    public UsersController(UsersServiceImp usersServiceImp) {
        this.usersServiceImp = usersServiceImp;
        new DbInit(this.usersServiceImp);
    }

    @GetMapping(value = "/")
    public String doDefaultRedirect() {
        return "redirect:/admin/main";
    }

    @GetMapping(value = "/admin/main")
    public String showUsersTable(Model model) {
        model.addAttribute("users", usersServiceImp.findAll());
        return "/admin/main";
    }

    @GetMapping(value = "/admin/new")
    public String showCreateUserPage(@ModelAttribute("user") User user,
                                     Model model) {
        model.addAttribute("allRoles", usersServiceImp.findAllRoles());
        return "/admin/new";
    }

    @PostMapping(value = "/admin/new")
    public String createNewUser(@ModelAttribute User user,
                                @RequestParam(value = "roleIds") int[] roles) {
        usersServiceImp.save(user, roles);
        return "redirect:/admin/main";
    }

    @GetMapping(value = "/admin/edit")
    public String showEditUserPage(@RequestParam("id") int id,
                                   Model model) {
        if (usersServiceImp.findOne(id).isPresent()) {
            model.addAttribute("currentUser", usersServiceImp.findOne(id).get());
            model.addAttribute("allRoles", usersServiceImp.findAllRoles());
        }

        return "/admin/edit";
    }

    @PatchMapping("/admin/edit")
    public String update(@ModelAttribute User updatedUser,
                         @RequestParam("roles") int[] rolesIds) {
        usersServiceImp.update(updatedUser, rolesIds);
        return "redirect:/admin/main";
    }


    @GetMapping(value = "/admin/delete")
    @DeleteMapping()
    public String delete(@RequestParam("id") int id) {
        usersServiceImp.deleteUserById(id);
        return "redirect:/admin/main";
    }

    @GetMapping(value = "/user")
    public String showUserPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "user";
    }

}
