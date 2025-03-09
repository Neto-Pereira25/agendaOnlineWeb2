package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.contact;

import java.util.ArrayList;
import java.util.List;

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
public class FindContactController {

    private String msg = null;
    private String error = null;
    private User user = null;

    @Autowired
    private HttpSession session;
    private final RepositoryContact REPOSITORY_CONTACT = new RepositoryContact();

    @RequestMapping({ "/findContactByName" })
    public String findContactByName(Model model, @PathParam("name") String name) {

        this.user = (User) session.getAttribute("user");

        if (user != null) {

            try {
                if (name != null && !name.trim().isEmpty()) {
                    List<Contact> contacts = REPOSITORY_CONTACT.findAllByName(user.getId(), name);

                    if (contacts != null) {
                        model.addAttribute("contacts", contacts);
                        this.msg = "Contatos encontrados com sucesso";
                        model.addAttribute("msg", this.msg);
                        this.msg = null;
                    }

                } else {
                    model.addAttribute("contacts", new ArrayList<>());
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            } catch (Exception e) {
                this.error = "Erro ao tentar buscar contatos pelo nome, tente novamente!";
                model.addAttribute("error", this.error);
                this.error = null;
            }

            return "pages/contact/findContact";
        } else {
            return "index";
        }

    }
}
