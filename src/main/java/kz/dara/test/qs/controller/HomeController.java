package kz.dara.test.qs.controller;

import kz.dara.test.qs.model.ProductModel;
import kz.dara.test.qs.model.UserModel;
import kz.dara.test.qs.repository.ProductRepository;
import kz.dara.test.qs.service.ProductService;
import kz.dara.test.qs.service.FileStorageService;
import kz.dara.test.qs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {

    @Value("${images.path}")
    private String imagesPath;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository repository;

    private final ProductService service;

    private final FileStorageService fileStorageService;

    public HomeController(ProductService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String indexPage(Model model, @RequestParam(name = "key", required = false, defaultValue = "") String key){
        model.addAttribute("product", service.searchProduct(key));
        return "index";
    }

    @GetMapping("/sign-in-page")
    public String signInPage(){
        return "sign_in_page";
    }

    @GetMapping("/sign-up-page")
    public String signUpPage(){
        return "sign_up_page";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profilePage(){
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add-page")
    public String addPage(Model model) {
        return "add_page";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add-product")
    public String addProduct(@RequestParam(name = "image") MultipartFile image,
                             @RequestParam(name = "model") String productModel,
                             @RequestParam(name = "color") String color,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "price") Long price) {
        try {
            ProductModel model = service.addProduct(productModel, color, description, price, image, imagesPath);
            if (model != null) {
                return "redirect:/";
            } else {
                return "redirect:/add-product?error";
            }
        } catch (IOException e) {
            System.out.println(e);
            return "redirect:/add-product?error";
        }
    }

    @GetMapping("/show-page/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showPage(@PathVariable(name = "id") Long id, Model model){
        if (repository != null) {
            ProductModel productModel = repository.findById(id).orElse(null);
            model.addAttribute("product", productModel);
            return "show_page";
        }
        return "SORRY PRODUCT NOT FOUND";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String details(@PathVariable(name = "id") Long id, Model model){
        if (repository != null) {
            ProductModel productModel = repository.findById(id).orElse(null);
            model.addAttribute("product", productModel);
            return "details";
        }
        return "SORRY PRODUCT NOT FOUND";
    }

    @PostMapping("/edit-product")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String saveProduct(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "image") MultipartFile image,
                              @RequestParam(name = "model") String model,
                              @RequestParam(name = "color") String color,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "price") Long price){
        try {
            ProductModel productModel = service.saveProduct(id, model, color, description, price, image, imagesPath);
            if (productModel != null) {
                return "redirect:/";
            } else {
                return "redirect:/add-product?error";
            }
        } catch (IOException e) {
            System.out.println(e);
            return "redirect:/add-product?error";
        }
    }

    @PostMapping("delete-product")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String deleteProduct(@RequestParam(name = "id") Long id){
        service.deleteProduct(id);
        return "redirect:/";
    }

    @PostMapping("/to-sign-up")
    public String toSignUp(@RequestParam(name = "user_email") String email,
                           @RequestParam(name = "user_password") String password,
                           @RequestParam(name = "user_full_name") String fullName,
                           @RequestParam(name = "user_repeat_password") String repeatPassword) {
        if (userService.signUpUser(email, password, fullName, repeatPassword)) {
            return "redirect:/sign-up-page?success";
        } else {
            return "redirect:/sign-up-page?error";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("update-password-page")
    public String updatePasswordPage(){
        return "update_password_page";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("update-profile-page")
    public String updateProfilePage(){
        return "update_profile_page";
    }

    @PostMapping("/to-update-password")
    @PreAuthorize("isAuthenticated()")
    public String toUpdatePassword(
            @RequestParam(name = "user_old_password") String oldPassword,
            @RequestParam(name = "user_new_password") String newPassword,
            @RequestParam(name = "user_repeat_new_password") String repeatNewPassword) {
        if (newPassword.equals(repeatNewPassword)){
            UserModel userModel = userService.updatePassword(newPassword, oldPassword);
            if (userModel != null){
                return "redirect:/update-password-page?success";
            } else {
                return "redirect:/update-password-page?oldPasError";
            }
        } else {
            return "redirect:/update-password-page?passwordError";
        }
    }

    @PostMapping("/to-update-profile")
    @PreAuthorize("isAuthenticated()")
    public String toUpdateProfile(
            @RequestParam(name = "user_full_name") String fullName,
            @RequestParam(name = "user_password") String password) {
        if (userService.checkPassword(password)){
            UserModel userModel = userService.updateProfile(fullName);
            if (userModel != null){
                return "redirect:/update-profile-page?success";
            } else {
                return "redirect:/update-profile-page?fullNameError";
            }
        } else {
            return "redirect:/update-profile-page?fullNameError";
        }
    }
}
