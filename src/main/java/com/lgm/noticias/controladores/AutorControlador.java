package com.lgm.noticias.controladores;

import com.lgm.noticias.entidades.Autor;
import com.lgm.noticias.excepciones.MyException;
import com.lgm.noticias.servicios.AutorServicio;
import com.lgm.noticias.servicios.NoticiaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @GetMapping("/cargar")
    public String cargarAutor (){
        return "autor_carga.html";
    }
    
    @PostMapping("/registro")
    public String registroAutor (MultipartFile archivo , @RequestParam String nombre, ModelMap model, ModelMap modelo){
        try {
            autorServicio.crearAutor(archivo, nombre);
            model.put("exito", "El autor se cargó correctamente");
            listarAutor(modelo, model);
            return "autor_admin.html";
        } catch (MyException e) {
            model.put("error", e.getMessage());
            return "autor_carga.html";
        }   
    }

    @GetMapping("/listar")
    public String listarAutor (ModelMap modelo, ModelMap model){ // la información la mandamos al html mediante un modelo
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor.html";
    }
    
    @GetMapping("/editar/{id}")
    public String editarAutor (@PathVariable String id, ModelMap modelo){
        modelo.put("autor",autorServicio.getOne(id));
        return "autor_edicion.html";
    }
    
    @PostMapping("/editar/{id}")
    public String editarAutor ( MultipartFile archivo, String id, @RequestParam String nombre) throws Exception{
        try{
            autorServicio.editarAutor(archivo, id, nombre);
            return "redirect:/autor/listar";
        }catch(Exception e){
            return "redirect:/autor/editar";      
        }
    }

    @GetMapping("/admin")
    public String adminAutor (ModelMap modelo){
        List<Autor> autores = autorServicio.listarAutores();
        modelo.addAttribute("autores", autores);
        return "autor_admin.html";
    }
    
    @GetMapping("/adminGral")
    public String gralAdmin() {

        return "admin.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarAutor (@PathVariable String id){
        try{    
            autorServicio.borrarAutor(id);
            return "redirect:/autor/admin";
            
        }catch(MyException ex){
            System.out.println(ex.getMessage());
            return "redirect:/admin";
        }
    }
    
    @GetMapping("/estado/{id}")
    public String cambiarEstadoAutor (@PathVariable String id){
        if(autorServicio.cambiarEstadoAutor(id)){
                return "redirect:/autor/admin";
            }else{
                return "redirect:/autor/admin";
            }
    }
    
    
}
