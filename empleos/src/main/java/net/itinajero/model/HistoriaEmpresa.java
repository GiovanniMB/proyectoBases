package net.itinajero.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="historiaempresa")
public class HistoriaEmpresa 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String historia;
	private String imagen="no-image.png";
	private Date fechaCreacion;
	@ManyToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinTable(name="historiasocios",
			   joinColumns = @JoinColumn(name="idHisrotia"),
			   inverseJoinColumns = @JoinColumn(name="idSocio")			
			)
	private List<Socio> socios;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHistoria() {
		return historia;
	}
	public void setHistoria(String historia) {
		this.historia = historia;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public List<Socio> getSocios() {
		return socios;
	}
	public void setSocios(List<Socio> socios) {
		this.socios = socios;
	}
	@Override
	public String toString() {
		return "HistoriaEmpresa [id=" + id + ", historia=" + historia + ", imagen=" + imagen + ", fecha=" + fechaCreacion
				+ ", socios=" + socios + "]";
	}
		
}
