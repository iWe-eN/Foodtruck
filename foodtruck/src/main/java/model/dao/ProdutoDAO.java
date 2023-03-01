package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoDAO {

	public ProdutoVO cadastrarProdutoDAO(ProdutoVO produtoVO) {
		String query = "INSERT INTO PRODUTO (idtipoproduto, nome, preco, datacadastro) VALUES (?, ?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			pstmt.setInt(1, produtoVO.getTipoProduto().getValor());
			pstmt.setString(2, produtoVO.getNome());
			pstmt.setDouble(3, produtoVO.getPreco());
			pstmt.setObject(4, produtoVO.getDataCadastro());
			pstmt.execute();
			ResultSet resultado = pstmt.getGeneratedKeys();
			if (resultado.next()) {
				produtoVO.setIdProduto(Integer.parseInt(resultado.getString(1)));
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método cadastrarProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return produtoVO;
		
	
	}

	public ArrayList<TipoProdutoVO> consultarTipoProdutoDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<TipoProdutoVO> listaTipoProdutoVO = new ArrayList<TipoProdutoVO>();
		String query = ("SELECT descricao FROM tipoproduto");

		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				TipoProdutoVO tipoProdutoVO = TipoProdutoVO.valueOf(resultado.getString(1));
				listaTipoProdutoVO.add(tipoProdutoVO);
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método consultarTipoProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}

		return listaTipoProdutoVO;
	}

	public ArrayList<ProdutoVO> ConsultarTodosProdutosDAO() {
        String query = "select * from produto;";
        Connection conn = Banco.getConnection();
        Statement stmt = Banco.getStatement(conn);
        ArrayList<ProdutoVO> produtos = new ArrayList<ProdutoVO>();
        ResultSet resultado = null;
        try {
            resultado = stmt.executeQuery(query);
            while (resultado.next()) {
                ProdutoVO produto = new ProdutoVO();
                TipoProdutoVO tipoProduto = TipoProdutoVO.getTipoProdutoVOporValor(resultado.getInt(2));
                produto.setIdProduto(resultado.getInt(1));
                produto.setTipoProduto(tipoProduto);
                produto.setNome(resultado.getString(3));
                produto.setPreco(resultado.getDouble(4));
                produto.setDataCadastro(LocalDateTime.parse(resultado.getString(5),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                if (resultado.getString(6) != null)
                    produto.setDataExclusao(LocalDateTime.parse(resultado.getString(6),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                produtos.add(produto);
            }
        } catch (SQLException erro) {
            System.out.println("Ocorreu um erro no método ConsultarTodosProdutosDAO");
            System.out.println(erro.getMessage());
            produtos = null;
        }
        return produtos;
    }

	public ProdutoVO consultarProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ProdutoVO produto = new ProdutoVO();
		String querry = "SELECT " + 
					"PRODUTO.IDPRODUTO, " + 
					"TIPOPRODUTO.DESCRICAO, " + 
					"PRODUTO.NOME, " + 
					"PRODUTO.PRECO, " + 
					"PRODUTO.DATACADASTRO, " + 
					"PRODUTO.DATAEXCLUSAO " + 
					"FROM " + 
					"PRODUTO " + 
					"INNER JOIN TIPOPRODUTO ON " + 
					"PRODUTO.IDTIPOPRODUTO = TIPOPRODUTO.IDTIPOPRODUTO " + 
					"WHERE PRODUTO.IDPRODUTO = " + produtoVO.getIdProduto() + ";";
		
		try {
			resultado = stmt.executeQuery(querry);
			while(resultado.next()) {
				produto.setIdProduto(Integer.parseInt(resultado.getString(1)));
				produto.setTipoProduto(TipoProdutoVO.valueOf(resultado.getString(2)));
				produto.setNome(resultado.getString(3));
				produto.setPreco(Double.parseDouble(resultado.getString(4)));
				produto.setDataCadastro(LocalDateTime.parse(resultado.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if(resultado.getString(6) != null) {
					produto.setDataExclusao(LocalDateTime.parse(resultado.getString(6), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a querry do método consultarProdutoDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return produto;
	}
	

	public boolean verificarExistenciaRegistroPorIdProdutoDAO(int idProduto) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = ("SELECT idproduto FROM produto WHERE idproduto = " + idProduto);

		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método verificarExistenciaRegistroPorIdProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}

		return retorno;
	}

	public boolean verificarDesligamentoUsuarioPorIdProdutoDAO(int idProduto) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT dataexclusao FROM produto WHERE idproduto = " + idProduto;

		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				String dataExclusao = resultado.getString(1);
				if (dataExclusao != null) {
					retorno = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método verificarDesligamentoUsuarioPorIdProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}

		return retorno;
	}

	public boolean atualizarProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "UPDATE produto SET idTipoProduto = " + produtoVO.getTipoProduto().getValor()
				+ ", nome = '" + produtoVO.getNome() 
				+ "', preco = '" + produtoVO.getPreco()
				+ "', DataCadastro = '" + produtoVO.getDataCadastro()
				+ "' WHERE idproduto = " + produtoVO.getIdProduto();
				
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método atualizarProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean excluirProdutoDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "UPDATE produto SET dataexclusao = '" + produtoVO.getDataExclusao() + "' WHERE idproduto = " + produtoVO.getIdProduto();

		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro AO executar a query do método excluirProdutoDAO. ");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public ArrayList<ProdutoVO> consultarTodosProdutosVigentesDAO() {
		 String query = "SELECT PRODUTO.IDPRODUTO, TIPOPRODUTO.DESCRICAO, PRODUTO.NOME,PRODUTO.PRECO,PRODUTO.DATACADASTRO"
		 		+ " FROM TIPOPRODUTO INNER JOIN PRODUTO ON PRODUTO.IDTIPOPRODUTO ="
		 		+ " TIPOPRODUTO.IDTIPOPRODUTO WHERE PRODUTO.DATAEXCLUSAO IS NULL;";
	        Connection conn = Banco.getConnection();
	        Statement stmt = Banco.getStatement(conn);
	        ArrayList<ProdutoVO> produtos = new ArrayList<ProdutoVO>();
	        ResultSet resultado = null;
	        try {
	            resultado = stmt.executeQuery(query);
	            while (resultado.next()) {
	                ProdutoVO produto = new ProdutoVO();
	                
	                produto.setIdProduto(resultado.getInt(1));
	                produto.setTipoProduto(TipoProdutoVO.valueOf(resultado.getString(2)));
	                produto.setNome(resultado.getString(3));
	                produto.setPreco(Double.parseDouble(resultado.getString(4)));
	                produto.setDataCadastro(LocalDateTime.parse(resultado.getString(5),
	                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	                produtos.add(produto);
	            }
	        } catch (SQLException erro) {
	            System.out.println("Ocorreu um erro no método consultarTodosProdutosVigentesDAO");
	            System.out.println(erro.getMessage());
	            produtos = null;
	        }
	        finally {
				Banco.closeStatement(stmt);
				Banco.closeConnection(conn);
				Banco.closeResultSet(resultado);
			}
	        return produtos;
	}

	public boolean verificarExistenciaRegistroPorNomeDAO(ProdutoVO produtoVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String querry = "SELECT NOME FROM PRODUTO WHERE NOME = '" + produtoVO.getNome() + "';";
		try {
			resultado = stmt.executeQuery(querry);
			if(resultado.next()) {
				retorno = true;
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao executar a querry do método verificarExistenciaRegistroPorNomeDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
	
}
