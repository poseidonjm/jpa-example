package com.rhemsolutions.examples;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.rhemsolutions.dao.UsuarioDao;
import com.rhemsolutions.domain.Role;
import com.rhemsolutions.domain.Usuario;
import com.rhemsolutions.domain.UsuarioRole;


public class App {
	
	private static Injector injector = Guice.createInjector(new JpaPersistModule("myFirstJpaUnit"));
	private static PersistService service = injector.getInstance(PersistService.class);
	private static UsuarioDao userDao = injector.getInstance(UsuarioDao.class);
	
	public static void main(String[] args) {
		service.start();
		try {
			
//			createUser();
//			editUser();
//			listUser();
			
//			addRolesUser();
			listUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void createUser() throws NoSuchAlgorithmException, InvalidKeySpecException{		
		
		Usuario user = new Usuario();
		user.setNombres("admin");
		user.setApellidos("admin");
		user.setUsuario("admin");
		user.setAclave("123");
		user.setReclave("123");
		user.setEmail("admin@gmail.com");
		
		userDao.persist(user);
	}
	
	private static void listUser(){
		List<Usuario> users =  userDao.getAllUsers();
		
		for(Usuario user: users){
			System.out.println(user.getNombres());
		}
	}
	
	private static void editUser() throws NoSuchAlgorithmException, InvalidKeySpecException{
		Usuario user = userDao.findUserById(1L);
		user.setNombres("Administrador");
		userDao.persist(user);
	}
	
	private static void addRolesUser() throws NoSuchAlgorithmException, InvalidKeySpecException{
		Usuario user = userDao.findUserById(1L);
		Role role = userDao.findRoleById(1L);
		user.addRole(role);
		userDao.persist(user);
	}
	
	private static void showRolesUser(){
		Usuario user = userDao.findUserById(1L);
		
		List<UsuarioRole> userroles = userDao.getRoles(user);
		for(UsuarioRole userrole: userroles){
			System.out.println(userrole.getRole().getDescripcion());
		}
	}
	

}
