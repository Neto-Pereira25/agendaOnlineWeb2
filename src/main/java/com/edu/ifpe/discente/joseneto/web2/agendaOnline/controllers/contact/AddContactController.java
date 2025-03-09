package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.contact;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

            List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());
            model.addAttribute("contacts", contacts);
            session.setAttribute("contacts", contacts);
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
            model.addAttribute("user", user);
            return "pages/contact/addContact";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/listAllContact" })
    public String listAllContact(Model model) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());
                model.addAttribute("user", user);
                model.addAttribute("contacts", contacts);
            } catch (SQLException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }
            return "pages/contact/listContact";
        } else {
            return "index";
        }
    }

    @GetMapping({ "/updateContact/{id}" })
    public String updateContact(Model model, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());
                model.addAttribute("user", user);
                model.addAttribute("contacts", contacts);
                model.addAttribute("id_contact_update", id);

                Contact c = (Contact) REPOSITORY_CONTACT.findById(id);

                session.setAttribute("contact_update", c);
                model.addAttribute("contact_update", c);
            } catch (SQLException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }
            return "pages/contact/updateContact";
        } else {
            return "index";
        }
    }

    @PostMapping({ "/updateContact" })
    public String updateContactById(Model model, @ModelAttribute("contact_update") Contact contact) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {

                REPOSITORY_CONTACT.update(contact);

                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());
                model.addAttribute("user", user);
                model.addAttribute("contacts", contacts);
            } catch (Exception e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }
            return "pages/contact/listContact";
        } else {
            return "index";
        }
    }
}
