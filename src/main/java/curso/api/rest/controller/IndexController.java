package curso.api.rest.controller;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController /*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {

    @Autowired /*se fosse CDI seria @Inject*/
    private UsuarioRepository usuarioRepository;

    /*Serviço RESTful*/
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Usuario>> init(){

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> usuario(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/relatoriopdf", produces = "application/json")
    public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        /*O retorno seria um relatorio*/
        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){

        Usuario usuarioNovo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioNovo, HttpStatus.OK);

    }


}
