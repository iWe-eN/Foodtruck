package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.dto.VendasCanceladasDTO;

public class RelatorioDAO {

	public ArrayList<VendasCanceladasDTO> gerarRelatorioVendasCanceladasDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<VendasCanceladasDTO> vendasCanceladasDTO = new ArrayList<VendasCanceladasDTO>();
		String query = "SELECT u.nome as NOME, "  
			+	" v.datacancelamento as DATA_CANCELAMENTO, (SELECT sum(p.preco * itv.quantidade) FROM itemvenda itv, produto p WHERE itv.idvenda = v.idvenda and itv.idproduto = p.idproduto GROUP BY idvenda) AS SUBTOTAL, " 
			+	" v.taxaentrega as TAXA_ENTREGA, " 
			+	" (v.taxaentrega +"  
			+	"        (SELECT sum(p.preco * itv.quantidade) " 
			+	"        FROM itemvenda itv, produto p "  
			+	"        WHERE itv.idvenda = v.idvenda "  
			+	"        and itv.idproduto = p.idproduto " 
			+	"        GROUP BY idvenda)) AS TOTAL "  
			+	"        FROM usuario u , venda v "  
			+	"        WHERE v.idusuario = u.idusuario "  
			+	"        AND v.datacancelamento is not null";
		
		try {
			resultado = stmt.executeQuery(query);
				while(resultado.next()) {
					VendasCanceladasDTO vendaCancelada = new VendasCanceladasDTO();
					vendaCancelada.setNome(resultado.getString(1));
					
					vendaCancelada.setDataCancelamento(LocalDateTime.parse(resultado.getString(2), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	
					vendaCancelada.setSubtotal(Double.parseDouble(resultado.getString(3)));
	
					if(resultado.getString(4) != null){
					vendaCancelada.setTaxaEntrega(Double.parseDouble(resultado.getString(4)));
					}
					if(resultado.getString(5) != null){
					vendaCancelada.setTotal(Double.parseDouble(resultado.getString(5)));
					} else {
						vendaCancelada.setTotal(Double.parseDouble(resultado.getString(3)));
					}
					vendasCanceladasDTO.add(vendaCancelada);
				}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar e query do método gerarRelatorioVendasCanceladasDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return vendasCanceladasDTO;
	}

	

}
