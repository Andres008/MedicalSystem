package ec.com.model.gestionSistema;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.com.model.dao.entity.AutMenu;
import ec.com.model.dao.entity.AutPerfile;
import ec.com.model.dao.entity.AutRol;
import ec.com.model.dao.entity.AutRolPerfil;
import ec.com.model.dao.entity.UsrSocio;
import ec.com.model.dao.entity.VAutMenuRol;
import ec.com.model.dao.manager.ManagerDAOSegbecom;

/**
 * Session Bean implementation class ManagerAutorizacion
 */
@Stateless(mappedName = "managerGestionSistema")
@LocalBean
public class ManagerGestionSistema {

	@EJB
	ManagerDAOSegbecom managerDAOSegbecom;

	public ManagerGestionSistema() {
		// TODO Auto-generated constructor stub
	}

	public Credencial obtenerAcceso(String pIdUsuario, String pClave) throws Exception {
		UsrSocio usuario = findByIdAutUsuario(pIdUsuario);
		Credencial credencial = new Credencial();

		if (usuario == null)
			throw new Exception("Usuario no existe, verifique su código.");
		if (usuario.getEstado().equalsIgnoreCase("N"))
			throw new Exception("El usuario no está activo.");
		if (!pClave.equals(usuario.getClave()))
			throw new Exception("Verifique su contraseña.");

		// System.out.println(usuario.getApellidos()+" "+usuario.getNombres()+"
		// "+pIdUsuario);
		credencial = new Credencial();
		credencial.setObjUsrSocio(usuario);
		// credencial.setCorreo(usuario.getGesPersona().getCorreo());
		credencial.setPrimerInicio(usuario.getPrimerInicio());
		return credencial;
	}

	public UsrSocio findByIdAutUsuario(String idUsuario) throws Exception {
		UsrSocio usuario = (UsrSocio) managerDAOSegbecom.findById(UsrSocio.class, idUsuario);
		return usuario;
	}

	@SuppressWarnings("unchecked")
	public List<VAutMenuRol> findVAutMenuRol(AutRol objAutRol) throws Exception {
		try {
			return managerDAOSegbecom.findWhere(VAutMenuRol.class, "o.idRol=" + objAutRol.getIdRol(), "o.orden ASC");
		} catch (Exception e) {
			throw new Exception("Error al buscar VAutMenuRol. " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<AutRolPerfil> findRolPerfilbyRol(AutRol objAutRol, VAutMenuRol vAutMenuRol) throws Exception {
		try {
			return managerDAOSegbecom.findWhere(AutRolPerfil.class,
					"o.autRol.id=" + objAutRol.getIdRol()
							+ " and o.estado='A' and o.autPerfile.estado='A' and o.autPerfile.autMenu.id="
							+ vAutMenuRol.getId(),
					"o.autPerfile.orden ASC");
		} catch (Exception e) {
			throw new Exception("Error al buscar Rol Perfil");
		}
	}

	public void ingresarAutRol(AutRol objAutRol) throws Exception {
		try {
			managerDAOSegbecom.insertar(objAutRol);
		} catch (Exception e) {
			throw new Exception("Error al ingresar Rol. "+e.getMessage());
		}		
	}

	@SuppressWarnings("unchecked")
	public List<AutRol> buscarTodosAutRol() throws Exception {
		try {
			return managerDAOSegbecom.findAll(AutRol.class, "o.estado ASC, o.nombre ASC");
		} catch (Exception e) {
			throw new Exception("Error al obtener el listado de Roles. "+e.getMessage());
		}
	}

	public void actualizarObjeto(Object object) throws Exception {
		try {
			managerDAOSegbecom.actualizar(object);
		} catch (Exception e) {
			throw new Exception("Error al actualizar "+object.getClass());
		}
		
	}

	@SuppressWarnings("unchecked")
	public List<AutMenu> buscarTodosAutMenu() throws Exception {
		try {
			List<AutMenu> lstAutMenu =managerDAOSegbecom.findAll(AutMenu.class, "o.orden ASC");
			lstAutMenu.forEach(autMenu->{
				autMenu.getAutPerfiles().forEach(perfil->{
					perfil.getId();
					perfil.getAutRolPerfils().forEach(rol->{
						rol.getAutRol().getNombre();
					});
				});
			});
			return lstAutMenu;
		} catch (Exception e) {
			throw new Exception("Error al buscar listado de menu");
		}
		
	}

	public void insertarAutMenu(AutMenu objAutMenu) throws Exception {
		try {
			managerDAOSegbecom.insertar(objAutMenu);
		} catch (Exception e) {
			throw new Exception("Error al ingresar Menu. "+e.getMessage());
		}
	}

	public void ingresarAutPerfil(AutPerfile objPerfil) throws Exception {
		try {
			managerDAOSegbecom.insertar(objPerfil);
		} catch (Exception e) {
			throw new Exception("Error al ingresar Perfil. "+e.getMessage());
		}
		
	}

	public void ingresarAutRolPerfil(AutRolPerfil objAutRolPerfil) throws Exception {
		try {
			managerDAOSegbecom.insertar(objAutRolPerfil);
		} catch (Exception e) {
			throw new Exception("Error al ingresar Rol Perfil. "+e.getMessage());
		}
		
	}

}