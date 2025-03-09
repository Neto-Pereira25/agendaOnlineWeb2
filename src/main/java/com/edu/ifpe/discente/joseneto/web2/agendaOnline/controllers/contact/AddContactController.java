package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.contact;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Contact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Phone;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryContact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryPhone;

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
    private final RepositoryPhone REPOSITORY_PHONE = new RepositoryPhone();

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
            model.addAttribute("user", this.user);
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
                model.addAttribute("user", this.user);
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
                model.addAttribute("user", this.user);
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

                if (contact != null) {
                    REPOSITORY_CONTACT.update(contact);
                } else {
                    throw new IllegalArgumentException("Dados do contato com problemas, tente novamente!");
                }

                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());

                if (contacts != null) {
                    model.addAttribute("user", this.user);
                    model.addAttribute("contacts", contacts);
                } else {
                    throw new IllegalArgumentException("Lista de contatos não pode ser encontrada, tente novamente!");
                }

            } catch (IllegalArgumentException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
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

    @RequestMapping({ "/removeContact/{id}" })
    public String removeContact(Model model, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                Contact c = (Contact) REPOSITORY_CONTACT.findById(id);

                if (c != null) {
                    REPOSITORY_CONTACT.delete(c.getId());
                } else {
                    throw new IllegalArgumentException("Contato não encontrado!");
                }

                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());
                model.addAttribute("user", this.user);
                model.addAttribute("contacts", contacts);

            } catch (IllegalArgumentException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
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

    @RequestMapping({ "/addContactPhone/{id}" })
    public String addContactPhone(Model model, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                Contact c = (Contact) REPOSITORY_CONTACT.findById(id);

                if (c != null) {
                    model.addAttribute("contact_phone_add", c);
                } else {
                    throw new IllegalArgumentException("O contato não pode ser encontrado, tente novamente!");
                }

            } catch (IllegalArgumentException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            } catch (SQLException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }
            return "pages/contact/addPhone";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/insertphone" })
    public String insertPhone(Model model,
            @ModelAttribute("contact_phone_add") Contact contact,
            // @PathParam("contact_id") int contact_id,
            @PathParam("number") String number,
            @PathParam("typePhone") String typePhone) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {

            try {
                String regex = "^(\\d{10}|\\d{11})$";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(number);

                if (number.isEmpty() || typePhone.isEmpty()) {
                    throw new IllegalArgumentException("Preencha todos os campos para cadastrar um novo telefone");
                }

                if (matcher.matches()) {
                    Phone ph = new Phone();
                    ph.setContactId(contact.getId());
                    ph.setPhoneNumber(number);
                    ph.setPhoneType(typePhone);

                    REPOSITORY_PHONE.insert(ph);

                } else {
                    throw new IllegalArgumentException("O número fornecido não é válido, tente novamente");
                }
            } catch (IllegalArgumentException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            } catch (SQLException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }

            try {
                List<Contact> contacts = REPOSITORY_CONTACT.findAll(this.user.getId());

                if (contacts != null) {
                    model.addAttribute("user", this.user);
                    model.addAttribute("contacts", contacts);
                }

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

}
