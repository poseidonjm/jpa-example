package com.rhemsolutions.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import com.rhemsolutions.domain.Role;
import com.rhemsolutions.domain.Role_;
import com.rhemsolutions.domain.Usuario;
import com.rhemsolutions.domain.UsuarioRole;
import com.rhemsolutions.domain.UsuarioRole_;
import com.rhemsolutions.domain.Usuario_;
import com.rhemsolutions.util.PasswordEncryptionService;
import com.rhemsolutions.util.Util;

public class UsuarioDao {

	@Inject Provider<EntityManager> emProvider;
	@Inject PasswordEncryptionService pes;
	@Inject Util util;
	
	@Transactional
	public Usuario persist(Usuario entity) throws NoSuchAlgorithmException, InvalidKeySpecException {
		if(entity.getAclave() != null){
			entity.setSalt(pes.generateSalt());
			entity.setClave(pes.getEncryptedPassword(entity.getAclave(), entity.getSalt()));
		}
		emProvider.get().persist(entity);
		return entity;
	}

	
	@Transactional
	public void remove(List<Usuario> entities) {
		for(Usuario entity: entities){
			emProvider.get().remove(entity);
		}		
	}
	
	@Transactional
	public Usuario addRolToUser(Usuario usuario, Role rol){
		usuario.addRole(rol);
		emProvider.get().persist(usuario);
		return usuario;
	}
	
	@Transactional
	public void removeRolOfUser(UsuarioRole usuario_role) {
		emProvider.get().remove(usuario_role);
	}
	
	public Usuario getUser(String username){
		CriteriaBuilder cb = emProvider.get().getCriteriaBuilder();
		CriteriaQuery<Usuario> c = cb.createQuery(Usuario.class);
		Root<Usuario> r = c.from(Usuario.class);
		Predicate condition = cb.equal(r.get(Usuario_.usuario), username);
		c.where(condition);
		TypedQuery<Usuario> q = emProvider.get().createQuery(c);		
		return util.getSingleResult(q);
	}
	
	public List<Usuario> getAllUsers() {

		CriteriaBuilder cb = emProvider.get().getCriteriaBuilder();
		CriteriaQuery<Usuario> c = cb.createQuery(Usuario.class);
		Root<Usuario> r = c.from(Usuario.class);
		c.orderBy(cb.asc(r.get(Usuario_.usuario)));
		TypedQuery<Usuario> q = emProvider.get().createQuery(c);		
		return q.getResultList();
	}
	
	public List<Role> getAllRoles() {

		CriteriaBuilder cb = emProvider.get().getCriteriaBuilder();
		CriteriaQuery<Role> c = cb.createQuery(Role.class);
		Root<Role> r = c.from(Role.class);
		c.orderBy(cb.asc(r.get(Role_.descripcion)));
		TypedQuery<Role> q = emProvider.get().createQuery(c);		
		return q.getResultList();
	}
		
	public List<UsuarioRole>  getRoles(Usuario usuario){
		CriteriaBuilder cb = emProvider.get().getCriteriaBuilder();
		CriteriaQuery<UsuarioRole> c = cb.createQuery(UsuarioRole.class);
		Root<UsuarioRole> r = c.from(UsuarioRole.class);
		Predicate condition = cb.equal(r.get(UsuarioRole_.usuario), usuario);
		c.where(condition);		
		TypedQuery<UsuarioRole> q = emProvider.get().createQuery(c);
		return q.getResultList();
	}
	
	
		
	

}