package br.com.uniciv.rest.livraria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivroRepositorio {

	private Map<Long, Livro> livros = new HashMap<>();
	
	public LivroRepositorio() {
		Livro livro1= new Livro(1L, "Livro A", "ISBN-1234", "Genero A", 23.99, "Autor1");
		Livro livro2= new Livro(2L, "Livro B", "ISBN-4321", "Genero B", 23.99, "Autor2");
		Livro livro3= new Livro(3L, "Livro C", "ISBN-1324", "Genero C", 23.99, "Autor1");
		
		livros.put(livro1.getId(), livro1);
		livros.put(livro2.getId(), livro2);
	}
	public List<Livro> getLivros() {
		return new ArrayList<>(livros.values());
	}
	public Livro getLivroPorIsbn(String isbn) {
		for(Livro livro:livros.values()) {
			if(isbn.equals(livro.getIsbn())) {
				return livro;
			}
		}
		return null;
	}
}
