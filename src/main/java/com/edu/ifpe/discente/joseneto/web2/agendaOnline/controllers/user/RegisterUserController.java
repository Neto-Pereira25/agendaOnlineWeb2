package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.DBException;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryUser;

import jakarta.websocket.server.PathParam;

@Controller
public class RegisterUserController {

    private String msg = null;
    private String error = null;
    private final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final RepositoryUser repositoryUser = new RepositoryUser();

    @RequestMapping({ "/" })
    public String home(Model model) {
        return "index";
    }

    @RequestMapping({ "/registeruser" })
    public String registerUser(Model model, @PathParam("name") String name, @PathParam("email") String email,
            @PathParam("password") String password, @PathParam("repeatPassword") String repeatPassword) {
        try {

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                throw new IllegalArgumentException("Preencha todos os campos");
            }

            if (!password.equals(repeatPassword)) {
                throw new IllegalArgumentException("As senhas devem ser iguais");
            }

            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setPassword(PASSWORD_ENCODER.encode(password));

            repositoryUser.insert(u);

            this.msg = "Usuário cadastrado com sucesso";
            model.addAttribute("msg", this.msg);
            this.msg = null;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;
            return "index";
        } catch (DBException e) {
            e.printStackTrace();
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            this.error = "Falha ao cadastrar usuário";
            model.addAttribute("error", this.error);
            this.error = null;
            return "index";
        }

        return "index";
    }
}
