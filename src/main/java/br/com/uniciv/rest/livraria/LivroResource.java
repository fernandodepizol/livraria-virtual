package br.com.uniciv.rest.livraria;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Path("livro")
public class LivroResource {
	
	private LivroRepositorio livroRepo = new LivroRepositorio();

	@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Livros getLivros() {
		Livros livros = new Livros();
		livros.setLivros(livroRepo.getLivros());
        return livros;
    }
	
	@GET
	@Path("/{isbn}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Livro getLivroPorIsbn(@PathParam("isbn") String isbn) {
		try {
			return livroRepo.getLivroPorIsbn(isbn);
		}catch (LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
	}
	
}
