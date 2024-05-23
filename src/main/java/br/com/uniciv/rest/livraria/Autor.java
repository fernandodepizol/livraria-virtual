package br.com.uniciv.rest.livraria;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="autor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Autor {

	@XmlAttribute
	private Long id;
	@XmlElement
	private String nome;
	
	
	public Autor() {}
	
	public Autor(String nome) {
		this.id = Math.round(Math.random()*10);
		this.nome = nome;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
