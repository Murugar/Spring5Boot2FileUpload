package com.iqmsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Controller
public class UploadController {
   

   
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index() {
        return "upload";
    }

    private void executeUpload(String uploadDir,MultipartFile file,RedirectAttributes redirectAttributes) throws Exception
    {
      
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;
        File serverFile = new File(uploadDir + filename);
        file.transferTo(serverFile);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded '" + file.getOriginalFilename() + "'");
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    String upload(HttpServletRequest request, MultipartFile file,RedirectAttributes redirectAttributes)
    {
        try {

            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
                return "redirect:uploadStatus";
            }
          
            String uploadDir = request.getSession().getServletContext().getRealPath("/") +"upload/";
          
            File dir = new File(uploadDir);
            if(!dir.exists()) {
                dir.mkdir();
            }
            executeUpload(uploadDir,file,redirectAttributes);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @RequestMapping(value = "/uploads",method = RequestMethod.POST)
    public String uploads(HttpServletRequest request,@RequestParam("file") MultipartFile[] file,RedirectAttributes redirectAttributes)
    {

        if (file.length<0) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {


        
            String uploadDir = request.getSession().getServletContext().getRealPath("/") +"upload/";
           
            File dir = new File(uploadDir);
            if(!dir.exists())
            {
                dir.mkdir();
            }
          
            for (int i =0;i<file.length;i++) {
                if(file[i] != null) {
                
                    executeUpload(uploadDir, file[i],redirectAttributes);
                }
            }
        }catch (Exception e)
        {
           
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }


    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }



 
}