package com.edu.ifpe.discente.joseneto.web2.agendaOnline.controllers.contact;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Contact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.Phone;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.entities.User;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryContact;
import com.edu.ifpe.discente.joseneto.web2.agendaOnline.model.repositories.RepositoryPhone;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/contact")
public class PhoneController {

    private String msg = null;
    private String error = null;
    private User user = null;

    @Autowired
    private HttpSession session;
    private final RepositoryContact REPOSITORY_CONTACT = new RepositoryContact();
    private final RepositoryPhone REPOSITORY_PHONE = new RepositoryPhone();

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

                    this.msg = "Telefone inserido com sucesso!";
                    model.addAttribute("msg", this.msg);
                    this.msg = null;

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

    @RequestMapping({ "/listPhone/{id}" })
    public String listPhone(Model model, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {

                Contact c = REPOSITORY_CONTACT.findById(id);

                if (c != null) {
                    model.addAttribute("phone_list", c.getPhones());
                    model.addAttribute("current_contact", c);
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
            return "pages/contact/listPhone";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/removePhone/{contact_id}/{id}" })
    public String removePhone(Model model, @PathVariable("contact_id") int contact_id, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                Contact c = (Contact) REPOSITORY_CONTACT.findById(contact_id);

                if (c != null) {

                    REPOSITORY_PHONE.delete(id);

                    List<Phone> phones = c.getPhones();

                    model.addAttribute("phone_list", phones);
                    model.addAttribute("current_contact", c);
                    session.setAttribute("current_contact", c);

                    this.msg = "Telefone removido com sucesso!";
                    model.addAttribute("msg", this.msg);
                    this.msg = null;
                } else {
                    throw new IllegalArgumentException("Contato não encontrado!");
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
            return "redirect:/contact/loadListNumbers";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/loadListNumbers" })
    public String loadListNumbers(Model model) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            var contact = (Contact) session.getAttribute("current_contact");

            try {
                if (contact != null) {
                    List<Phone> phones = REPOSITORY_PHONE.findAll(contact.getId());
                    model.addAttribute("current_contact", contact);
                    model.addAttribute("phone_list", phones);
                }
            } catch (SQLException e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }

            return "pages/contact/listPhone";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/updatePhone/{id}" })
    public String updatePhone(Model model, @PathVariable("id") int id) {
        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {

                Phone ph = REPOSITORY_PHONE.findById(id);

                if (ph != null) {

                    Contact c = REPOSITORY_CONTACT.findById(ph.getContactId());

                    if (c != null) {
                        session.setAttribute("update_phone", ph);
                        model.addAttribute("update_phone", ph);
                        session.setAttribute("contact_phone", c);
                        model.addAttribute("contact_phone", c);
                    }

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
            return "pages/phone/updatePhone";
        } else {
            return "index";
        }
    }

    @RequestMapping({ "/editPhone" })
    public String editPhone(Model model, @PathParam("phone_id") int id,
            @PathParam("phoneType") String phoneType, @ModelAttribute("update_phone") Phone update_phone,
            @RequestParam("phoneNumber") String phoneNumber) {

        this.user = (User) session.getAttribute("user");

        if (this.user != null) {
            try {
                if (phoneNumber.isEmpty() || phoneType.isEmpty() || phoneNumber == null || phoneType == null) {
                    throw new IllegalArgumentException("Preencha todos os campos para cadastrar um novo telefone");
                }

                String regex = "^(\\d{10}|\\d{11})$";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(phoneNumber);

                if (matcher.matches()) {
                    Phone ph = REPOSITORY_PHONE.findById(id);
                    if (ph != null) {

                        ph.setPhoneNumber(update_phone.getPhoneNumber());
                        ph.setPhoneType(phoneType);

                        REPOSITORY_PHONE.update(ph);
                    } else {
                        throw new IllegalArgumentException("O número fornecido não foi encontrado, tente novamente");
                    }
                    this.msg = "Telefone atualizado com sucesso!";
                    model.addAttribute("msg", this.msg);
                    this.msg = null;
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
                Phone ph = REPOSITORY_PHONE.findById(id);
                if (ph != null) {
                    Contact c = REPOSITORY_CONTACT.findById(ph.getContactId());

                    if (c != null) {
                        List<Phone> phones = c.getPhones();

                        model.addAttribute("current_contact", c);
                        session.setAttribute("phone_list", phones);
                        model.addAttribute("phone_list", phones);
                    }
                }

            } catch (Exception e) {
                this.error = e.getMessage();
                model.addAttribute("error", this.error);
                this.error = null;
            }

            return "pages/contact/listPhone";
        } else {
            return "index";
        }
    }
}
