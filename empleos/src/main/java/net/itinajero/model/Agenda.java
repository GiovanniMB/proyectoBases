package net.itinajero.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="agenda")
public class Agenda 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private Date fecha;
	@ManyToOne
	@JoinColumn(name="idSolicitud")
	private Solicitud solicitud;
	@ManyToOne
	@JoinColumn(name="idRH")
	private Usuario usuarioRH;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Solicitud getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}
	public Usuario getUsuarioRH() {
		return usuarioRH;
	}
	public void setUsuarioRH(Usuario usuarioRH) {
		this.usuarioRH = usuarioRH;
	}
	@Override
	public String toString() {
		return "Agenda [id=" + id + ", fecha=" + fecha + ", solicitud=" + solicitud + ", usuarioRH=" + usuarioRH + "]";
	}
	
}
