package es.project.Pandemic.EntidadesYClasesSecundarias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = 9, unique = true)
	private String documentoIdentidad;
	
	@Column(nullable = false, length = 45)
	private String contrasenna;
	
	@ManyToMany
	@JoinTable(
			name="rolesusuariocentro",
			joinColumns = @JoinColumn(name="usuario"),
			inverseJoinColumns = @JoinColumn(name="rol")
	)
	@OrderBy("nombre ASC")
	@Column(nullable = false, length = 45)
	private List<Rol> roles;
	
	@ManyToMany
	@JoinTable(
			name="notificacion",
			joinColumns = @JoinColumn(name="usuario"),
			inverseJoinColumns = @JoinColumn(name="id")
	)
	@OrderBy("fecha DESC")
	private List<Notificacion> notificaciones;
	
	/*@Transient
	@Autowired
	ServicioCitas servicioCitas;
	
	//@Transient
	@Autowired
	ServicioPruebasComplementarias servicioPruebasComplementarias;*/
	
	public Usuario() {}

	public Usuario(long id, boolean logueado, String documentoIdentidad, String contrasenna, List<Rol> roles, List<Notificacion> notificaciones) {
		this.id = id;
		this.documentoIdentidad = documentoIdentidad;
		this.contrasenna = contrasenna;
		this.roles = roles;
		this.notificaciones = notificaciones;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDocumentoIdentidad() {
		return documentoIdentidad;
	}
	
	public void setDocumentoIdentidad(String documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}
	
	public String getContrasenna() {
		return contrasenna;
	}
	
	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}
	
	public List<Rol> getRoles() {
		return roles != null ? roles : new ArrayList<>();
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	
	public List<Notificacion> getNotificaciones() {
		return notificaciones != null ? notificaciones : new ArrayList<>();
	}
	
	/*public List<Notificacion> getNotificacionesExtendidas() {
		if(notificaciones != null) {
			List<Notificacion> listaNotificacionesExtendidas = new ArrayList<>();
			notificaciones.forEach((notificacion) -> {
				if (notificacion.getCodigo().contains("cita")) {
					Cita informacionCita = servicioCitas.getCitaPorId(notificacion.getIdReferencia());
					listaNotificacionesExtendidas.add(informacionCita != null ? new NotificacionCita(notificacion, informacionCita) : notificacion);
				} else if (notificacion.getCodigo().contains("prueba")) {
					PruebaComplementaria informacionPrueba = servicioPruebasComplementarias.getPruebaComplemetariaPorId(notificacion.getIdReferencia());
					listaNotificacionesExtendidas.add(informacionPrueba != null ? new NotificacionPruebaComplementaria(notificacion, informacionPrueba) : notificacion);
				} else {
					listaNotificacionesExtendidas.add(notificacion);
				}
			});
			return listaNotificacionesExtendidas;
		}
		return new ArrayList<>();
		
	}*/

	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	public List<String> getRolesAsString() {
		return roles != null ? roles.stream().map(rol -> rol.toString()).collect(Collectors.toList()) : new ArrayList<>();
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoles() != null
				? this.getRoles().stream().map(rol -> new SimpleGrantedAuthority(rol.toString())).collect(Collectors.toList())
				: new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return this.contrasenna;
	}

	@Override
	public String getUsername() {
		return this.getDocumentoIdentidad();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
