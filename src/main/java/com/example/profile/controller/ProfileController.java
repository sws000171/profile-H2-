package com.example.profile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import com.example.profile.models.Member;
import com.example.profile.service.ProfileService;

@Controller
public class ProfileController {

    @Autowired
    ProfileService ps;
     
    @GetMapping("/")
    public String Controller(Model model){
        List<Member> memberAll = ps.findAll();
        model.addAttribute("member", memberAll);
        return "index";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Member> memberAll = ps.findAll();
        model.addAttribute("member", memberAll);
        return "list";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute Member member,
                           @ModelAttribute String completeMsg, Model model){
        model.addAttribute("completeMsg", completeMsg);
        return "register";
    }

    @PostMapping("/insert")
    public String memberInsert(
        @Validated @ModelAttribute Member member, BindingResult result, 
        @RequestParam("faceImg") MultipartFile faceFile, 
        @RequestParam("backImg") MultipartFile backFile, 
        RedirectAttributes redirectAttributes, Model model){
        //バリデーションエラーがある場合はregister.htmlを表示
        if (result.hasErrors()){
                /* for (FieldError error : result.getFieldErrors()) {
                    String field = error.getField();
                    String message = error.getDefaultMessage();
                    System.out.println(field);
                    System.out.println(message);
                   } */
            return "register";
        }
        //ファイルの入力なしはエラーを返す
        if(faceFile.isEmpty())  {
			model.addAttribute("faceErr", "ファイルを指定してください");
			return "register";
		}
        if(backFile.isEmpty()) {
			model.addAttribute("backErr", "ファイルを指定してください");
			return "register";
		}

        //書き込み
        ps.memberInsert(member,faceFile,backFile);
        redirectAttributes.addFlashAttribute("completeMsg","登録完了");
        //return "redirect:/register";
        List<Member> memberAll = ps.findAll();
        model.addAttribute("member", memberAll);
        return "redirect:/";
    }

    @GetMapping(value="/edit/{id}")
    public String memberGet(@PathVariable Long id, @ModelAttribute Member member, 
        @ModelAttribute String completeMsg, Model model){
        member = ps.memberGet(id);
        model.addAttribute("member", member);
        model.addAttribute("completeMsg", completeMsg);
        return "edit";
    }

    @PostMapping(value="/edit/{id}")
    public String MemberSet(@Validated @ModelAttribute Member member, 
        BindingResult result, @PathVariable Long id, RedirectAttributes redirectAttributes,
        @RequestParam("faceImg") MultipartFile faceFile, 
        @RequestParam("backImg") MultipartFile backFile, Model model){

        if (result.hasErrors()){
            return "edit";
        }
            
        ps.memberSet(member, faceFile, backFile);
        //edit成功
        redirectAttributes.addFlashAttribute("completeMsg","更新完了");
        //return ("redirect:/edit/" + id);
        List<Member> memberAll = ps.findAll();
        model.addAttribute("member", memberAll);
        return "redirect:/list";
    }

    @GetMapping(value="/delete/{id}")
    public String memberDelete(@PathVariable Long id){
        ps.memberDelete(id);
        return "redirect:/list";
    }    
}
