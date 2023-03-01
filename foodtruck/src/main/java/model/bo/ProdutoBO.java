package model.bo;

import java.util.ArrayList;

import model.dao.ProdutoDAO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoBO {
	public ProdutoVO cadastrarProdutoBO(ProdutoVO produtoVO) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		if(produtoDAO.verificarExistenciaRegistroPorNomeDAO(produtoVO)) {
			System.out.println("\nUsuário já cadastrado na Base de Dados");
		} else {
			produtoVO = produtoDAO.cadastrarProdutoDAO(produtoVO);
		}
		return produtoVO;
	}

	public ArrayList<TipoProdutoVO> consultarTipoProdutoDAO() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		return produtoDAO.consultarTipoProdutoDAO();
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosBO() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ArrayList<ProdutoVO> listaProdutoVO = produtoDAO.ConsultarTodosProdutosDAO();
		if (listaProdutoVO.isEmpty()) {
			System.out.println("\nLista de produtos está vazia!");

		}
		return listaProdutoVO;
	}

	public ProdutoVO consultarProdutosBO(ProdutoVO produtoVO) {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ProdutoVO produto = produtoDAO.consultarProdutoDAO(produtoVO);
		if (produto == null) {
			System.out.println("\nUsuário não foi localizado");
		}
		return produto;
	}

	public boolean atualizarProdutoBO(ProdutoVO produtoVO) {
		boolean resultado = false;
		ProdutoDAO produtoDAO = new ProdutoDAO();
		if (produtoDAO.verificarExistenciaRegistroPorIdProdutoDAO(produtoVO.getIdProduto())) {
			if (produtoDAO.verificarDesligamentoUsuarioPorIdProdutoDAO(produtoVO.getIdProduto())) {
				System.out.println("\nUsuário já se encontra desligado na base de dados!");
			} else {
				resultado = produtoDAO.atualizarProdutoDAO(produtoVO);
			}
		} else {
			System.out.println("\nUsuário não existe na base de dados!");
		}
		return resultado;
	}

	public boolean excluirProdutoBO(ProdutoVO produtoVO) {
		boolean resultado = false;
		ProdutoDAO produtoDAO = new ProdutoDAO();
		if (produtoDAO.verificarExistenciaRegistroPorIdProdutoDAO(produtoVO.getIdProduto())) {
			if (produtoDAO.verificarDesligamentoUsuarioPorIdProdutoDAO(produtoVO.getIdProduto())) {
				System.out.println("\nUsuário já se encontra desligado na base de dados!");
			} else {
				resultado = produtoDAO.excluirProdutoDAO(produtoVO);
			}
		} else {
			System.out.println("\nUsuário não existe na base de dados!");
		}
		return resultado;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosVigentesBO() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		ArrayList<ProdutoVO> listaProdutosVO = produtoDAO.consultarTodosProdutosVigentesDAO();
		if(listaProdutosVO.isEmpty()) {
			System.out.println("\nLista de produtos está vazia.");
		}
		return listaProdutosVO;
	}

}
