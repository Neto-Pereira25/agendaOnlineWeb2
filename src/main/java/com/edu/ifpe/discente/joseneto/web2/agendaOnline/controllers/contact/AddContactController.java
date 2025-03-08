package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.contact;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Contact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryContact;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/contact")
public class AddContactController {

    private String msg = null;
    private String error = null;
    private User user = null;

    @Autowired
    private HttpSession session;
    private final RepositoryContact REPOSITORY_CONTACT = new RepositoryContact();

    @RequestMapping({ "/insertcontact" })
    public String insertContact(Model model, @PathParam("name") String name,
            @PathParam("email") String email, @PathParam("address") String address) {

        try {

            if (name.isEmpty() || email.isEmpty() || address.isEmpty()) {
                throw new IllegalArgumentException("Preencha todos os campos");
            }

            Contact c = new Contact();
            c.setName(name);
            c.setEmail(email);
            c.setAddress(address);
            c.setUserId(this.user.getId());

            REPOSITORY_CONTACT.insert(c);

            this.msg = "Contato cadastrado com sucesso";
            model.addAttribute("msg", this.msg);
            this.msg = null;

        } catch (IllegalArgumentException e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;
        } catch (SQLException e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;
        } catch (Exception e) {
            this.error = e.getMessage();
            model.addAttribute("error", this.error);
            this.error = null;
        }

        return "pages/contact/listContact";
    }

    @RequestMapping({ "/registerContact" })
    public String registerContact(Model model) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            model.addAttribute("user", user); // Passa o objeto 'user' para o modelo
            return "pages/contact/addContact"; // Página de adicionar contato
        } else {
            return "index"; // Se o usuário não estiver logado, redireciona para a página inicial
        }
    }
}
