package net.itinajero.model;



import java.util.Random;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empresas")
public class Empresa 
{
	@Id
    @Column(name = "companyCode", nullable = false, length = 36)
	private String companyCode;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idHistoria", referencedColumnName = "id")
	private HistoriaEmpresa hisEmpresa;
	private String razonSocial;
	private String rfc;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="idDireccion", referencedColumnName = "id")
	private Direccion direccion;
	private String telefono;
	private String descripcion;
	private String imagen="no-image.png";
	
	public String generarCompanyCode() 
	{
	    StringBuilder codigoBuilder = new StringBuilder();
	    Random random = new Random();
	    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	    for (int i = 0; i < 36; i++) {
	        int index = random.nextInt(caracteres.length());
	        codigoBuilder.append(caracteres.charAt(index));
	    }

	    return codigoBuilder.toString();
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public HistoriaEmpresa getHisEmpresa() {
		return hisEmpresa;
	}

	public void setHisEmpresa(HistoriaEmpresa hisEmpresa) {
		this.hisEmpresa = hisEmpresa;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	@Override
	public String toString() {
		return "Empresa [companyCode=" + companyCode + ", hisEmpresa=" + hisEmpresa + ", razonSocial=" + razonSocial
				+ ", rfc=" + rfc + ", direccion=" + direccion + ", telefono=" + telefono + ", descripcion="
				+ descripcion + ", imagen=" + imagen + "]";
	}

			
}
