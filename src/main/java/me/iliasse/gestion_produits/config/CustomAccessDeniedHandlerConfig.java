package me.iliasse.gestion_produits.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Configuration
public class CustomAccessDeniedHandlerConfig implements AccessDeniedHandler {

    private RequestMappingHandlerMapping handlerMapping;

    public CustomAccessDeniedHandlerConfig(RequestMappingHandlerMapping handlerMapping){
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException denied) throws IOException, ServletException {
        if(!this.isUrlMapped(req)){
            res.sendError(404);
        }
        else{
            res.sendError(403);
        }
    }

    private boolean isUrlMapped(HttpServletRequest req){
        try{
            HandlerExecutionChain handler = handlerMapping.getHandler(req);

            return handler != null;
        }
        catch(Exception ex){
            return false;
        }
    }
}
