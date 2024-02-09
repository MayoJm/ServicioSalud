package com.appsalud.plataformaSalud.servicios;

import com.appsalud.plataformaSalud.entidades.Imagen;
import com.appsalud.plataformaSalud.excepciones.MiException;
import com.appsalud.plataformaSalud.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagenServicio {
    @Autowired ImagenRepositorio imagenRepositorio;


    public Imagen guardar(MultipartFile archivo){
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
        return (null);
    }

    public Imagen actualizar(MultipartFile archivo,String idImagen) throws MiException {
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();
                if (idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if (respuesta.isPresent()){
                        imagen = respuesta.get();
                    }
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
        return (null);
    }
}
