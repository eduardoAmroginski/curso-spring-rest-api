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

        usuario.getTelefones().forEach(telefone -> {
            telefone.setUsuario(usuario);
        });

        Usuario usuarioNovo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioNovo, HttpStatus.OK);

    }

    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){

        usuario.getTelefones().forEach(telefone -> {
            telefone.setUsuario(usuario);
        });

        Usuario usuarioAtualizar = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioAtualizar, HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}", produces = "application/text")
    public ResponseEntity delete(@PathVariable(value = "id") Long id){

        usuarioRepository.deleteById(id);

        String mensagem = "Usuário com o id: " + id + " deletado, com sucesso";

        return new ResponseEntity(mensagem, HttpStatus.OK);
    }


}
