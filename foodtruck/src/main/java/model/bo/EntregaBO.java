package model.bo;

import java.util.ArrayList;
import java.util.Random;

import model.dao.EntregaDAO;
import model.dao.UsuarioDAO;
import model.dao.VendaDAO;
import model.vo.EntregaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.UsuarioVO;
import model.vo.VendaVO;

public class EntregaBO {

	public boolean cadastrarEntregaBO(int idVenda) {
		boolean retorno = true;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaEntregadores = usuarioDAO.consultarListaEntregadores();
		if(listaEntregadores.isEmpty()) {
			System.out.println("Não existem entregadores cadastrados na base de dados!");
			retorno = false;
		} else {
			Random gerador = new Random();
			UsuarioVO entregador = listaEntregadores.get(gerador.nextInt(listaEntregadores.size()));
			EntregaVO entregaVO = new EntregaVO(0, idVenda, entregador.getIdUsuario(), SituacaoEntregaVO.PEDIDO_REALIZADO, null);
			EntregaDAO entregaDAO = new EntregaDAO();
			boolean resultado = entregaDAO.cadastrarEntregaDAO(entregaVO);
			if(!resultado) {
				System.out.println("Houve um problema ao tentar cadastrar a entrega!");
				retorno = false;
			}
		}
		return retorno;
	}
	public boolean atualizarSituacaoEntregaBO(VendaVO vendaVO) {
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaBO vendaBO = new VendaBO();
		boolean resultado = vendaBO.verificarVendaParaAtualizarSituacaoEntrega(vendaVO);
		if(resultado) {
			retorno = entregaDAO.atualizarSituacaoEntregaDAO(vendaVO);
		}
		return retorno;
		
	}
	
	public boolean cancelarEntregaBO(VendaVO vendaVO) {
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaDAO vendaDAO = new VendaDAO();
		VendaVO venda = vendaDAO.consultarVendaDAO(vendaVO);
		EntregaVO entregaVO = new EntregaVO();
		if(venda != null) {
			if(venda.getDataCancelamento() == null) {
				if(venda.getDataVenda().isAfter(vendaVO.getDataCancelamento())) {
					if(venda.isFlagEntrega()) {
						entregaVO = entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda());
					} if(entregaVO.getSituacaoEntrega().getValor() <= SituacaoEntregaVO.PREPARANDO_PEDIDO.getValor()) {
						retorno = entregaDAO.cancelarEntregaDAO(vendaVO, SituacaoEntregaVO.ENTREGA_CANCELADA.getValor());
						if(!retorno) {
							System.out.println("\nNão foi possivel alterar a situação da entrega para cancelada.");
						}
					}
				}
			}
		}
		return retorno;
	}
}
