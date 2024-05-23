package br.com.uniciv.rest.livraria;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
		
		return livroRepo.getLivroPorIsbn(isbn);
	}
	
}
