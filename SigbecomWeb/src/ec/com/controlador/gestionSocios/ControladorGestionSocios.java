package ec.com.controlador.gestionSocios;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.file.UploadedFile;

import ec.com.controlador.sesion.BeanLogin;
import ec.com.model.auditoria.ManagerLog;
import ec.com.model.dao.entity.AutRol;
import ec.com.model.dao.entity.GesDiscapacidad;
import ec.com.model.dao.entity.GesDiscapacidadPersona;
import ec.com.model.dao.entity.GesEstadoCivil;
import ec.com.model.dao.entity.GesEtnia;
import ec.com.model.dao.entity.GesGenero;
import ec.com.model.dao.entity.GesPariente;
import ec.com.model.dao.entity.GesPersona;
import ec.com.model.dao.entity.GesTipoSangre;
import ec.com.model.dao.entity.UsrAgencia;
import ec.com.model.dao.entity.UsrArea;
import ec.com.model.dao.entity.UsrCanton;
import ec.com.model.dao.entity.UsrCargo;
import ec.com.model.dao.entity.UsrConsanguinidad;
import ec.com.model.dao.entity.UsrCuentaSocio;
import ec.com.model.dao.entity.UsrEstadoSocio;
import ec.com.model.dao.entity.UsrInstitucionBancaria;
import ec.com.model.dao.entity.UsrInstruccion;
import ec.com.model.dao.entity.UsrLicenciaSocio;
import ec.com.model.dao.entity.UsrParroquia;
import ec.com.model.dao.entity.UsrProvincia;
import ec.com.model.dao.entity.UsrSocio;
import ec.com.model.dao.entity.UsrTipoCuenta;
import ec.com.model.dao.entity.UsrTipoInstruccion;
import ec.com.model.dao.entity.UsrTipoLicencia;
import ec.com.model.dao.entity.UsrTipoVivienda;
import ec.com.model.gestionPersonas.ManagerGestionPersonas;
import ec.com.model.gestionSistema.ManagerGestionSistema;
import ec.com.model.gestionSocios.ManagerGestionSocios;
import ec.com.model.modulos.util.CorreoUtil;
import ec.com.model.modulos.util.JSFUtil;
import ec.com.model.modulos.util.ModelUtil;

@Named("controladorGestionSocios")
@SessionScoped
public class ControladorGestionSocios implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BeanLogin beanLogin;

	@EJB
	ManagerLog managerLog;

	@EJB
	ManagerGestionSocios managerGestionSocios;

	@EJB
	ManagerGestionPersonas managerGestionPersonas;
	
	@EJB
	ManagerGestionSistema managerGestionSistema;

	@EJB
	CorreoUtil correoUtil;

	private UsrSocio objUsrSocio;

	private boolean skip;

	private UploadedFile file;

	private GesPariente objGesPariente;

	private GesDiscapacidadPersona objGesDiscapacidad;

	private UsrLicenciaSocio objUsrLicenciaSocio;

	private UsrInstruccion objUsrInstruccion;

	private UsrCuentaSocio objUsrCuentaSocio;

	public ControladorGestionSocios() {
		// TODO Auto-generated constructor stub
	}

	public void inicializarIngresoSocio() {
		objUsrSocio = new UsrSocio();
		objUsrSocio.setGesPersona(new GesPersona());
		objUsrSocio.setAutRol(new AutRol());
		objUsrSocio.setUsrEstadoSocio(new UsrEstadoSocio());
	}

	/***
	 * Metodo inicial para Actualizacion de datos
	 */
	public void inicializarActualizacionSocio() {

		try {
			objUsrSocio = managerGestionSocios
					.buscarSocioById(beanLogin.getCredencial().getObjUsrSocio().getCedulaSocio());
			if (objUsrSocio.getPrimerInicio().equals("S"))
				objUsrSocio.setClave("");
			if (objUsrSocio.getGesPersona().getGesDiscapacidadPersonas() == null)
				objUsrSocio.getGesPersona().setGesDiscapacidadPersonas(new ArrayList<GesDiscapacidadPersona>());
			if (objUsrSocio.getGesPersona().getGesEstadoCivil() == null)
				objUsrSocio.getGesPersona().setGesEstadoCivil(new GesEstadoCivil());
			if (objUsrSocio.getGesPersona().getGesEtnia() == null)
				objUsrSocio.getGesPersona().setGesEtnia(new GesEtnia());
			if (objUsrSocio.getGesPersona().getGesGenero() == null)
				objUsrSocio.getGesPersona().setGesGenero(new GesGenero());
			if (objUsrSocio.getGesPersona().getGesParientes() == null)
				objUsrSocio.getGesPersona().setGesParientes(new ArrayList<GesPariente>());
			if (objUsrSocio.getGesPersona().getGesTipoSangre() == null)
				objUsrSocio.getGesPersona().setGesTipoSangre(new GesTipoSangre());
			if (objUsrSocio.getUsrTipoVivienda() == null)
				objUsrSocio.setUsrTipoVivienda(new UsrTipoVivienda());
			if (objUsrSocio.getUsrParroquia() == null) {
				objUsrSocio.setUsrParroquia(new UsrParroquia());
				objUsrSocio.getUsrParroquia().setIdParroquia("0");
				objUsrSocio.getUsrParroquia().setUsrCanton(new UsrCanton());
				objUsrSocio.getUsrParroquia().getUsrCanton().setIdCanton("0");
				objUsrSocio.getUsrParroquia().getUsrCanton().setUsrProvincia(new UsrProvincia());
				objUsrSocio.getUsrParroquia().getUsrCanton().getUsrProvincia().setIdProvincia("0");
				;
			}
			if (objUsrSocio.getUsrCargo() == null)
				objUsrSocio.setUsrCargo(new UsrCargo());
			if (objUsrSocio.getUsrArea() == null)
				objUsrSocio.setUsrArea(new UsrArea());
			if (objUsrSocio.getUsrAgencia() == null)
				objUsrSocio.setUsrAgencia(new UsrAgencia());
			inicializarGesPariente();
			inicializarGesDiscapacidad();
			inicializarLicenciaSocio();
			inicializarFormacion();
			inicializarCuenta();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(),
					"inicializarActualizacionSocio", e.getMessage());
			e.printStackTrace();
		}

	}

	public void inicializarCuenta() {
		objUsrCuentaSocio = new UsrCuentaSocio();
		objUsrCuentaSocio.setUsrTipoCuenta(new UsrTipoCuenta());
		objUsrCuentaSocio.setUsrInstitucionBancaria(new UsrInstitucionBancaria());
		objUsrCuentaSocio.setUsrSocio(objUsrSocio);

	}

	public void inicializarFormacion() {
		objUsrInstruccion = new UsrInstruccion();
		objUsrInstruccion.setUsrTipoInstruccion(new UsrTipoInstruccion());
		objUsrInstruccion.setUsrSocio(objUsrSocio);
	}

	public void inicializarLicenciaSocio() {
		objUsrLicenciaSocio = new UsrLicenciaSocio();
		objUsrLicenciaSocio.setUsrSocio(objUsrSocio);
		objUsrLicenciaSocio.setUsrTipoLicencia(new UsrTipoLicencia());
	}

	/***
	 * 
	 */
	public void upload() {
		if (file != null) {
			FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	/***
	 * 
	 */
	public void actualizarPersonaSocio() {
		try {
			managerGestionPersonas.actualizarGesPersona(objUsrSocio.getGesPersona());
			System.out.println(objUsrSocio.getUsrAgencia().getIdAgencia());
			managerGestionSocios.actualizarUsrSocio(objUsrSocio);
			inicializarActualizacionSocio();
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "actualizarPersonaSocio",
					"Actualización a persona: " + objUsrSocio.getGesPersona().getCedula());
			JSFUtil.crearMensajeINFO("Datos actualizados Correctamentes.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(), "actualizarPersonaSocio",
					e.getMessage());
		}
	}

	public void actualizarSocio() {
		try {
			managerGestionSocios.actualizarUsrSocio(objUsrSocio);
			inicializarActualizacionSocio();
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "actualizarSocio",
					"Actualización a socio: " + objUsrSocio.getGesPersona().getCedula());
			JSFUtil.crearMensajeINFO("Datos actualizados Correctamente.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(), "actualizarSocio",
					e.getMessage());
		}
	}

	public void actualizarSocioDiscapacidad() {
		try {
			managerGestionPersonas.actualizarGesPersona(objUsrSocio.getGesPersona());
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "actualizarSocio",
					"Actualización a socio: " + objUsrSocio.getGesPersona().getCedula());
			JSFUtil.crearMensajeINFO("Datos actualizados Correctamente.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(), "actualizarSocio",
					e.getMessage());
		}
	}

	public void actualizarSocioFamiliares() {
		try {
			for (GesPariente pariente : objUsrSocio.getGesParientes()) {
				if (managerGestionPersonas.buscarPersonaByCedula(pariente.getGesPersona().getCedula()) == null)
					managerGestionPersonas.insertarPersona(pariente.getGesPersona());
			}
			managerGestionSocios.actualizarUsrSocio(objUsrSocio);
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "actualizarSocioFamiliares",
					"Actualización a socio: " + objUsrSocio.getGesPersona().getCedula());
			JSFUtil.crearMensajeINFO("Datos actualizados Correctamente.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(), "actualizarSocioFamiliares",
					e.getMessage());
		}
	}

	/*
	 * MÉTODO DEL PANEL WIZARD
	 */
	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // resetear en caso de que regrese
			return "solicitudPermisos";
		} else {
			return event.getNewStep();
		}
	}

	/***
	 * 
	 * @throws Exception
	 */
	public void ingresarFamiliar() throws Exception {
		try {
			ModelUtil.verificarCedulaEcuador(objGesPariente.getGesPersona().getCedula());
			objGesPariente.setUsrConsanguinidad(managerGestionSocios
					.buscarConsanguinidadById(objGesPariente.getUsrConsanguinidad().getIdConsanguinidad()));
			objGesPariente.setUsrSocio(objUsrSocio);
			if (managerGestionPersonas.buscarPersonaByCedula(objGesPariente.getGesPersona().getCedula()) != null)
				managerGestionPersonas.actualizarGesPersona(objGesPariente.getGesPersona());
			objUsrSocio.getGesParientes().add(objGesPariente);
			inicializarGesPariente();
			actualizarSocioFamiliares();
			inicializarActualizacionSocio();
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "ingresarFamiliar",
					"Se ingresa un nuevo familiar");
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
			throw new Exception("Error...");
		}

	}

	/***
	 * 
	 * @throws Exception
	 */
	public void ingresarDiscapacidad() throws Exception {
		try {
			objGesDiscapacidad.setGesDiscapacidad(managerGestionPersonas
					.buscarDiscapacidadById(objGesDiscapacidad.getGesDiscapacidad().getIdDiscapacidad()));
			objGesDiscapacidad.setEstado("A");
			objUsrSocio.getGesPersona().getGesDiscapacidadPersonas().add(objGesDiscapacidad);
			actualizarSocioDiscapacidad();
			inicializarGesDiscapacidad();
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", e.getMessage());
			PrimeFaces.current().dialog().showMessageDynamic(message);
			throw new Exception("Error...");
		}

	}

	/***
	 * 
	 */
	public void inscribirSocio() {
		try {
			if (managerGestionSocios.buscarSocioById(objUsrSocio.getCedulaSocio()) != null)
				throw new Exception("Usuario ya se encuentra registrado.");
			String clave = ModelUtil.randomAlphaNumeric();
			ModelUtil.verificarCedulaEcuador(objUsrSocio.getGesPersona().getCedula());
			objUsrSocio.getGesPersona().setApellidos(objUsrSocio.getGesPersona().getApellidos().toUpperCase());
			objUsrSocio.getGesPersona().setNombres(objUsrSocio.getGesPersona().getNombres().toUpperCase());
			objUsrSocio.getGesPersona().setEmail(objUsrSocio.getGesPersona().getEmail().toLowerCase());
			objUsrSocio.setFechaAlta(new Date());
			objUsrSocio.setPrimerInicio("S");
			objUsrSocio.setClave(ModelUtil.md5(clave));
			objUsrSocio.getUsrEstadoSocio().setIdEstado(1);
			if (managerGestionPersonas.buscarPersonaByCedula(objUsrSocio.getGesPersona().getCedula()) == null)
				managerGestionPersonas.insertarPersona(objUsrSocio.getGesPersona());
			objUsrSocio.setCedulaSocio(objUsrSocio.getGesPersona().getCedula());
			managerGestionSocios.insertarSocio(objUsrSocio);
			managerLog.generarLogUsabilidad(beanLogin.getCredencial(), this.getClass(), "inscribirUsuario",
					"Crea usuario " + objUsrSocio.getGesPersona().getCedula());
			correoUtil.enviarCorreoElectronico(objUsrSocio.getGesPersona().getEmail(),
					"Creación Usuarios Socio Comite de Empresa",
					"Se ha creado su usuario para utilizar SIGBECOM (), favor acceder al sistema con las siguentes credenciales, Usuario:"
							+ objUsrSocio.getCedulaSocio() + " , Contraseña:" + clave);
			JSFUtil.crearMensajeINFO("Usuario creado correctamente.");
			inicializarIngresoSocio();
		} catch (Exception e) {
			managerLog.generarLogErrorGeneral(beanLogin.getCredencial(), this.getClass(), "inscribirUsuario",
					"Error al crear usuario " + objUsrSocio.getGesPersona().getCedula());
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}

	}

	public void inicializarGesPariente() {
		objGesPariente = new GesPariente();
		objGesPariente.setUsrConsanguinidad(new UsrConsanguinidad());
		objGesPariente.setGesPersona(new GesPersona());
		objGesPariente.getGesPersona().setGesDiscapacidadPersonas(new ArrayList<GesDiscapacidadPersona>());
		objGesPariente.getGesPersona().setGesEstadoCivil(new GesEstadoCivil());
		objGesPariente.getGesPersona().setGesEtnia(new GesEtnia());
		objGesPariente.getGesPersona().setGesGenero(new GesGenero());
		objGesPariente.getGesPersona().setGesParientes(new ArrayList<GesPariente>());
		objGesPariente.getGesPersona().setGesTipoSangre(new GesTipoSangre());
	}

	public void inicializarGesDiscapacidad() {
		objGesDiscapacidad = new GesDiscapacidadPersona();
		objGesDiscapacidad.setGesDiscapacidad(new GesDiscapacidad());
		objGesDiscapacidad.setGesPersona(objUsrSocio.getGesPersona());
	}

	public List<SelectItem> lstSiTipoLicencia() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrTipoLicencia usrTipoLicencia : managerGestionSocios.buscarTipoLicencia()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrTipoLicencia.getTipoLicencia());
				siCivil.setValue(usrTipoLicencia.getIdTipoLicencia());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiTipoVivienda() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrTipoVivienda usrTipoVivienda : managerGestionSocios.buscarTipoVivienda()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrTipoVivienda.getTipoVivienda());
				siCivil.setValue(usrTipoVivienda.getIdTipoVivienda());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiAgencia() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrAgencia usrAgencia : managerGestionSocios.buscarAgencia()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrAgencia.getAgencia());
				siCivil.setValue(usrAgencia.getIdAgencia());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiAreas() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrArea usrArea : managerGestionSocios.buscarAreas()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrArea.getArea());
				siCivil.setValue(usrArea.getIdArea());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiCargos() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrCargo usrCargo : managerGestionSocios.buscarCargos()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrCargo.getCargo());
				siCivil.setValue(usrCargo.getIdCargo());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiProvincia() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrProvincia usrProvincia : managerGestionSocios.buscarProvincias()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrProvincia.getProvincia());
				siCivil.setValue(usrProvincia.getIdProvincia());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiCanton() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrCanton usrCanton : managerGestionSocios.buscarCantoByProvincia(
					objUsrSocio.getUsrParroquia().getUsrCanton().getUsrProvincia().getIdProvincia())) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrCanton.getCanton());
				siCivil.setValue(usrCanton.getIdCanton());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiParroquia() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrParroquia usrParroquia : managerGestionSocios
					.buscarParroquiaByCanton(objUsrSocio.getUsrParroquia().getUsrCanton().getIdCanton())) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrParroquia.getParroquia());
				siCivil.setValue(usrParroquia.getIdParroquia());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiTipoCuenta() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrTipoCuenta usrTipoCuenta : managerGestionSocios.buscarTipoCuenta()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrTipoCuenta.getTipoCuenta());
				siCivil.setValue(usrTipoCuenta.getIdTipoCuenta());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiInstitucionesFinancieras() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrInstitucionBancaria usrInstitucionBancaria : managerGestionSocios.buscarInsitucionBancaria()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrInstitucionBancaria.getInstitucionBancaria());
				siCivil.setValue(usrInstitucionBancaria.getIdInstitucionBancaria());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<SelectItem> lstSiTipoInstruccion() {
		List<SelectItem> lstSiEtnia = new ArrayList<SelectItem>();
		try {
			for (UsrTipoInstruccion usrTipoInstruccion : managerGestionSocios.buscarTipoInstruccion()) {
				SelectItem siCivil = new SelectItem();
				siCivil.setLabel(usrTipoInstruccion.getTipoInstruccion());
				siCivil.setValue(usrTipoInstruccion.getIdTipoInstruccion());
				lstSiEtnia.add(siCivil);
			}
			return lstSiEtnia;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public void agregarLicencia() {
		try {
			objUsrLicenciaSocio.setUsrTipoLicencia(managerGestionSocios
					.buscarTipoLicenciaById(objUsrLicenciaSocio.getUsrTipoLicencia().getIdTipoLicencia()));
			objUsrLicenciaSocio.setUsrSocio(objUsrSocio);
			objUsrSocio.getUsrLicenciaSocios().add(objUsrLicenciaSocio);
			actualizarSocio();
			inicializarActualizacionSocio();
			inicializarLicenciaSocio();
			JSFUtil.crearMensajeINFO("Licencia agregada corrctamente.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public void agregarFormacion() {
		try {
			objUsrInstruccion.setUsrTipoInstruccion(managerGestionSocios
					.buscarTipoInstruccionById(objUsrInstruccion.getUsrTipoInstruccion().getIdTipoInstruccion()));
			objUsrInstruccion.setUsrSocio(objUsrSocio);
			objUsrSocio.getUsrInstruccions().add(objUsrInstruccion);
			actualizarSocio();
			inicializarActualizacionSocio();
			inicializarLicenciaSocio();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public void agregarCuenta() {
		try {
			objUsrCuentaSocio.setUsrTipoCuenta(
					managerGestionSocios.buscarTipoCuentaById(objUsrCuentaSocio.getUsrTipoCuenta().getIdTipoCuenta()));
			objUsrCuentaSocio.setUsrInstitucionBancaria(managerGestionSocios.buscarInsitucionBancariaById(
					objUsrCuentaSocio.getUsrInstitucionBancaria().getIdInstitucionBancaria()));
			objUsrCuentaSocio.setFechaRegistro(new Date());
			objUsrCuentaSocio.setEstado("A");
			objUsrSocio.getUsrCuentaSocios().stream().filter(cuenta -> cuenta.getEstado().equals("A"))
					.collect(Collectors.toList()).forEach(activos -> {
						activos.setEstado("I");
						activos.setFechaBaja(new Date());
					});
			;
			objUsrSocio.addUsrCuentaSocio(objUsrCuentaSocio);
			actualizarSocio();
			inicializarCuenta();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public String finalizarActualizacionDatos() {
		try {
			if (objUsrSocio.getUsrEstadoSocio().getIdEstado() == 2) {
				actualizarSocio();
				return "/modulos/menuPrincipal.xhtml?faces-redirect=true";
			}
			objUsrSocio.getUsrEstadoSocio().setIdEstado(2);
			actualizarSocio();
			return beanLogin.actionSalirSistema();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public List<UsrCuentaSocio> getUsrCuentaSociosActivas(List<UsrCuentaSocio> lstCuentas) {
		return lstCuentas.stream().filter(cuentas -> cuentas.getEstado().equals("A")).collect(Collectors.toList());
	}

	public void cargarProvinciaSeleccionada() {
		try {
			objUsrSocio.getUsrParroquia().getUsrCanton().setUsrProvincia(managerGestionSocios.buscarProvinciaById(
					objUsrSocio.getUsrParroquia().getUsrCanton().getUsrProvincia().getIdProvincia()));
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}

	public void cargarCantonSeleccionado() {
		try {
			objUsrSocio.getUsrParroquia().setUsrCanton(
					managerGestionSocios.buscarCantonById(objUsrSocio.getUsrParroquia().getUsrCanton().getIdCanton()));
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}

	public void cargarParroquiaSeleccionada() {
		try {
			objUsrSocio.setUsrParroquia(
					managerGestionSocios.buscarParroquiaById(objUsrSocio.getUsrParroquia().getIdParroquia()));
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}

	public void inactivarDiscapacidad(GesDiscapacidadPersona disPersona) {

		try {
			objUsrSocio.getGesPersona().getGesDiscapacidadPersonas().forEach(discapacidad -> {
				if (discapacidad.getId() == disPersona.getId()) {
					discapacidad.setEstado("I");
					try {
						managerGestionSocios.actualizarDiscapacidad(discapacidad);
					} catch (Exception e) {
						JSFUtil.crearMensajeERROR("Error al actualizar discapacidad persona.");
						e.printStackTrace();
					}
				}

			});
			managerGestionSocios.actualizarUsrSocio(objUsrSocio);
			inicializarActualizacionSocio();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			this.file = event.getFile();
			verificarTamanioFotografia(event);
			objUsrSocio.setUrlFoto(ModelUtil.guardarArchivo(event.getFile().getInputStream(),
					beanLogin.getCredencial().getObjUsrSocio().getCedulaSocio(),managerGestionSistema.buscarValorParametroNombre("PATH_FOTOS"),
					".jpg"));
			managerGestionSocios.actualizarUsrSocio(objUsrSocio);
			JSFUtil.crearMensajeINFO(
					"Fotografía cargada correctamente, se actualizará en el proximo ingreso al sistema.");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public DefaultStreamedContent cargarFotografia() {
		File fotografia;
		if (!ModelUtil.isEmpty(objUsrSocio.getUrlFoto())) {
			fotografia = new File(objUsrSocio.getUrlFoto());
			try {
				return new DefaultStreamedContent(new FileInputStream(fotografia),
						new MimetypesFileTypeMap().getContentType(fotografia));
			} catch (FileNotFoundException e) {
				JSFUtil.crearMensajeERROR("Error al cargar imagen.");
				e.printStackTrace();
			}
		}
		return null;
	}

	public void verificarTamanioFotografia(FileUploadEvent event) throws Exception {
		InputStream is = event.getFile().getInputStream();
		BufferedImage img = ImageIO.read(is);
		/*
		 * 400 x 450 sería el tamaño recomendable
		 */
		int width = img.getWidth();
		int height = img.getHeight();
		if (width > 148 || height > 184) {
			throw new Exception(" El Ancho de la imagen y el alto tienen que ser de 400 x 450 px.");
		}
	}

	public void eliminarPariente(GesPariente objParienteAux) {
		try {
			for (GesPariente pariente : objUsrSocio.getGesParientes()) {
				if (pariente.getIdPariente() == objParienteAux.getIdPariente()) {
					managerGestionSocios.eliminarGesPariente(pariente);
					inicializarActualizacionSocio();
					JSFUtil.crearMensajeINFO("Se elimino la información.");
					break;
				}
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}

	}
	
	public void eliminarLicencia(UsrLicenciaSocio objLicenciaAux) {
		try {
			for (UsrLicenciaSocio licencia : objUsrSocio.getUsrLicenciaSocios()) {
				if (licencia.getId() == objLicenciaAux.getId()) {
					managerGestionSocios.eliminarUsrLicenciaSocio(licencia);
					inicializarActualizacionSocio();
					JSFUtil.crearMensajeINFO("Se elimino la información.");
					break;
				}
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}

	}
	public void eliminarTitulo(UsrInstruccion objInstruccionAux) {
		try {
			for (UsrInstruccion instruccion : objUsrSocio.getUsrInstruccions()) {
				if (instruccion.getIdInstruccion() == objInstruccionAux.getIdInstruccion()) {
					managerGestionSocios.eliminarUsrInstruccion(instruccion);
					inicializarActualizacionSocio();
					JSFUtil.crearMensajeINFO("Se elimino la información.");
					break;
				}
			}
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}

	}

	public List<GesDiscapacidadPersona> getGesDiscapacidadPersonasActivas(
			List<GesDiscapacidadPersona> lstDiscapacidadPersona) {
		return lstDiscapacidadPersona.stream().filter(discapacidad -> discapacidad.getEstado().equals("A"))
				.collect(Collectors.toList());
	}

	/******
	 * GETTERS Y SETTERS
	 * 
	 * 
	 */

	public BeanLogin getBeanLogin() {
		return beanLogin;
	}

	public void setBeanLogin(BeanLogin beanLogin) {
		this.beanLogin = beanLogin;
	}

	public UsrSocio getObjUsrSocio() {
		return objUsrSocio;
	}

	public void setObjUsrSocio(UsrSocio objUsrSocio) {
		this.objUsrSocio = objUsrSocio;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public GesPariente getObjGesPariente() {
		return objGesPariente;
	}

	public void setObjGesPariente(GesPariente objGesPariente) {
		this.objGesPariente = objGesPariente;
	}

	public GesDiscapacidadPersona getObjGesDiscapacidad() {
		return objGesDiscapacidad;
	}

	public void setObjGesDiscapacidad(GesDiscapacidadPersona objGesDiscapacidad) {
		this.objGesDiscapacidad = objGesDiscapacidad;
	}

	public UsrLicenciaSocio getObjUsrLicenciaSocio() {
		return objUsrLicenciaSocio;
	}

	public void setObjUsrLicenciaSocio(UsrLicenciaSocio objUsrLicenciaSocio) {
		this.objUsrLicenciaSocio = objUsrLicenciaSocio;
	}

	public UsrInstruccion getObjUsrInstruccion() {
		return objUsrInstruccion;
	}

	public void setObjUsrInstruccion(UsrInstruccion objUsrInstruccion) {
		this.objUsrInstruccion = objUsrInstruccion;
	}

	public UsrCuentaSocio getObjUsrCuentaSocio() {
		return objUsrCuentaSocio;
	}

	public void setObjUsrCuentaSocio(UsrCuentaSocio objUsrCuentaSocio) {
		this.objUsrCuentaSocio = objUsrCuentaSocio;
	}

}