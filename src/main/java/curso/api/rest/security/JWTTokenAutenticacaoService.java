package curso.api.rest.security;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Component
public class JWTTokenAutenticacaoService {

//  Tempo de validade do token 2 dias, em milissegundos
    private static final long EXPIRATION_TIME = 172800000;

//  Senha unica para compor a autenticação - Ideal deixar como variavel no application
    private static final String SECRET = "SenhaExtremamenteSecreta";

//  Prefixo padrão de Token
    private static final String TOKEN_PREFIX = "Bearer";

    private static final String HEADER_STRING = "Authorization";

    /*Gerando token de autenticação e adicionando ao cabeçalho e resposta HTTP*/
    public void addAuthentication(HttpServletResponse response, String username) throws Exception{

        /*Montagem do Token*/
        String JWT = Jwts.builder() /* Chama o gerador de Token */
                    .setSubject(username) /* Adiciona o usuário */
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Adiciona o tempo de expiração do Token */
                    .signWith(SignatureAlgorithm.HS512, SECRET) /* Concatena o algoritmo de geração de token com a senha SECRET */
                    .compact(); /* Compacta o token */

        /*Junta o token com o prefixo*/
        String token = TOKEN_PREFIX + " " + JWT; /*Bearer 87848794asdasdwasdasw */

        /*Adiciona no cabeçalho Http*/
        response.addHeader(HEADER_STRING, token); /*Authorization: Bearer 87848794asdasdwasdasw */

        /*Escreve token como resposta no corpo do http*/
        response.getWriter().write("{\"Authorization\" : \""+ token +"\"}");
    }

    /*Retorna o usuário validado com token ou caso não seja valido retorna null*/
    public Authentication getAuthentication(HttpServletRequest request){
        /*Pega o token enviado no cabeçalho http*/
        String token = request.getHeader(HEADER_STRING);
        if(token != null){
          /*Faz a validação do token do usuário na requisição*/
          String user = Jwts.parser().setSigningKey(SECRET) /*Bearer 87848794asdasdwasdasw */
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, "")) /* 87848794asdasdwasdasw */
                        .getBody().getSubject(); /*João Silva*/

            if(user != null){
                Usuario usuario = ApplicationContextLoad.getApplicationContext()
                        .getBean(UsuarioRepository.class).findUserbyLogin(user);

                /*Retorna o usuário logado*/
                if (usuario != null){
                    return new UsernamePasswordAuthenticationToken(
                            usuario.getLogin(),
                            usuario.getSenha(),
                            usuario.getAuthorities());
                }
            }

        }
        return null; /*Não autorizado*/
    }
}
