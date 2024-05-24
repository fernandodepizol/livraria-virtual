package br.com.uniciv.rest.livraria;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.Link;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="itemBusca")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemBusca {

	@XmlElement
	private Livro livro;
	
	@XmlElement
	@XmlJavaTypeAdapter(Link.JaxbAdapter.class)
	private List<Link> links = new ArrayList<Link>();
	
	
	public void addLink(Link link) {
		this.links.add(link);
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Link> getLinks() {
		return links;
	}
	
	
}
