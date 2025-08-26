package hua.dt.ds.work2025.propertyrental.controllers;

import hua.dt.ds.work2025.propertyrental.entities.PropertyForRent;
import hua.dt.ds.work2025.propertyrental.entities.PropertyStatus;
import hua.dt.ds.work2025.propertyrental.entities.User;
import hua.dt.ds.work2025.propertyrental.service.PropertyForRentService;
import hua.dt.ds.work2025.propertyrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import hua.dt.ds.work2025.propertyrental.entities.Property;
import hua.dt.ds.work2025.propertyrental.service.PropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("properties")
public class PropertyController {
    private final PropertyService propertyService;
    private final UserService userService;
    private final PropertyForRentService propertyForRentService;

    public PropertyController(PropertyService propertyService, UserService userService,
                              PropertyForRentService propertyForRentService) {
        this.propertyService = propertyService;
        this.userService = userService;
        this.propertyForRentService = propertyForRentService;
    }

    @RequestMapping()
    public String show(Model model) {
        model.addAttribute("property", new Property());
        return "propertylist";
    }

    @GetMapping("/list")
    public String listProperties(Model model,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Ο χρήστης δεν υπάρχει στη βάση δεδομένων"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());
        model.addAttribute("properties", propertyService.getProperties());
        return "/propertyforrent/propertyforrentform";
    }

    @GetMapping("/newProperty")
    public String showPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        model.addAttribute("propertyForRent", new PropertyForRent());
        model.addAttribute("formAction", "/properties/save");
        return "/propertyforrent/propertyform";
    }

    @GetMapping("/edit/{id}")
    public String editProperty(@PathVariable Long id, Model model) {
        PropertyForRent propertyforrent = propertyForRentService.getPropertyForRent(id);
        Property property = propertyforrent.getProperty();
        model.addAttribute("property", property);
        model.addAttribute("propertyForRent", propertyforrent);
        model.addAttribute("formAction", "/properties/update");
        return "/propertyforrent/propertyform";
    }

    @PostMapping("/save")
    public String saveProperty( @Valid @ModelAttribute("property") Property property,
                                BindingResult propertyBindingResult,
                                @Valid @ModelAttribute("propertyForRent") PropertyForRent propertyForRent,
                                BindingResult rentBindingResult,
                                @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (propertyBindingResult.hasErrors() || rentBindingResult.hasErrors()) {
            return "/propertyforrent/propertyform";
        }
        propertyForRentService.savePropertyForRentWithOwner(property, propertyForRent, loggedInUser);
        return "redirect:/propertyforrent/list";

    }


    @PostMapping("/update")
    public String updateProperty(@Valid @ModelAttribute("property") Property property,
                                 BindingResult propertyBindingResult,
                                 @Valid @ModelAttribute("propertyForRent") PropertyForRent propertyForRent,
                                 BindingResult rentBindingResult,
                                 @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (propertyBindingResult.hasErrors() || rentBindingResult.hasErrors()) {
            return "propertyforrent/propertyform";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        propertyForRentService.updatePropertyForRentWithOwner(property, propertyForRent, loggedInUser);

        return "redirect:/propertyforrent/list";

    }

    @PostMapping("/delete/{id}")
    public String deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return "redirect:/properties/list";
    }

}
