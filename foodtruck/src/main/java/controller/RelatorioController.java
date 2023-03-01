package controller;

import java.util.ArrayList;

import model.bo.RelatorioBO;
import model.dto.VendasCanceladasDTO;
import model.vo.VendaVO;

public class RelatorioController {

	public ArrayList<VendasCanceladasDTO> gerarRelatorioVendasCanceladasController() {
		RelatorioBO relatorioBO = new RelatorioBO();
		return relatorioBO.gerarRelatorioVendasCanceladasBO();
	}

	public void gerarRelatorioListaPedidosController() {
		RelatorioBO relatorioBO = new RelatorioBO();
		 relatorioBO.gerarRelatorioListaPedidosBO();
		
	}

	public void gerarRelatorioAcompanhamentoPedidosController(VendaVO vendaVO) {
		RelatorioBO relatorioBO = new RelatorioBO();
		 relatorioBO.gerarRelatorioAcompanhamentoPedidosBO(vendaVO);		
	}



}
