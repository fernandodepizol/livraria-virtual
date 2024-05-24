package br.com.uniciv.rest.livraria;

import java.net.URI;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

@Path("livro")
public class LivroResource {
	
	private LivroRepositorio livroRepo = LivroRepositorio.getInstance();

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
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response criaLivro(Livro livro) {
		try {
			livroRepo.adicionaLivro(livro);
		
			URI uriLocation = UriBuilder.fromPath("livro/{isbn}").build(livro.getIsbn());
		
			return Response.created(uriLocation).entity(livro).build();
		}catch(LivroExistenteException e){
			throw new WebApplicationException(Status.CONFLICT);
		}
	}
	
	@PUT
	@Path("/{isbn}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response atualizaLivro(@PathParam("isbn") String isbn, Livro livro ) {
		try {
			Livro livroDoEstoque = livroRepo.getLivroPorIsbn(isbn);
			livroDoEstoque.setAutor(livro.getAutor());
			livroDoEstoque.setGenero(livro.getGenero());
			livroDoEstoque.setPreco(livro.getPreco());
			livroDoEstoque.setTitulo(livro.getTitulo());
			
			livroRepo.atualizaLivro(livroDoEstoque);
			
		}catch(LivroNaoEncontradoException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return Response.ok().entity(livro).build();
		
		
		
	}
		
}
