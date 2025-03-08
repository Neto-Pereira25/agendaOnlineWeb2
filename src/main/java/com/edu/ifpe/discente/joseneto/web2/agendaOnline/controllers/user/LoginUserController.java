package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.user;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.db.DBException;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryUser;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
public class LoginUserController {

    private String msg = null;
    private String error = null;
    private User user = null;

    private final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private final RepositoryUser REPOSITORY_USER = new RepositoryUser();

    @Autowired
    private HttpSession session;

    @RequestMapping({ "/login" })
    public String login(Model model, @PathParam("email") String email, @PathParam("password") String password) {

        try {
            if (email.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Preencha todos os campos para fazer login");
            }

            this.user = REPOSITORY_USER.findByEmail(email);

            if (this.user == null) {
                throw new IllegalArgumentException("Login inválido, tente novamente!");
            }

            if (!PASSWORD_ENCODER.matches(password, this.user.getPassword())) {
                throw new IllegalArgumentException("Login inválida, tente novamente!");
            }

            this.msg = "Usuário logado com sucesso";
            model.addAttribute("msg", this.msg);
            session.setAttribute("user", this.user);
            this.msg = null;

            return "pages/user/userData";
        } catch (IllegalArgumentException e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;

            return "index";
        } catch (SQLException e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;

            return "index";
        } catch (Exception e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;

            return "pages/user/userData";
        }
    }

    @RequestMapping({ "/logout" })
    public String logout(Model model) {

        try {
            if (user != null) {
                session.removeAttribute("user");

                this.msg = "Usuário deslogado com sucesso";
                model.addAttribute("msg", this.msg);
                this.msg = null;
            }
            return "index";
        } catch (Exception e) {
            this.error = "Algo deu errado, contate o ADM do sistema";
            model.addAttribute("error", this.error);
            this.error = null;
            return "index";
        }
    }
}
