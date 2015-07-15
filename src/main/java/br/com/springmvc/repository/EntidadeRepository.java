package br.com.springmvc.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.springmvc.modelo.Entidade;

@Transactional
@Repository
public class EntidadeRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public void create() {
		System.out.println("CREATE");
		entityManager.persist(new Entidade("coluna"));
	}

	public Entidade read(Integer id) {
		System.out.println("READ");
		return entityManager.find(Entidade.class, id);
	}

	public void update() {
		System.out.println("UPDATE");
		entityManager.merge(new Entidade(1, "coluna"));
	}

	public void delete() {
		System.out.println("DELETE");
		entityManager.remove(read(1));
	}

}
