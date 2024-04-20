package net.itinajero.service;

import java.util.List;
import net.itinajero.model.Usuario;

public interface IUsuariosService 
{

	void guardar(Usuario usuario);
	void eliminar(Integer idUsuario);
	List<Usuario> buscarTodos();
	Usuario buscarPorUsername(String username);
	List<Usuario> buscarRegistrados();
	Usuario buscarPorId(Integer idUsuario);
	int bloquear(int idUsuario);
	int activar(int idUsuario);
	Usuario buscarPorCompanyCode(String companyCode);
}
