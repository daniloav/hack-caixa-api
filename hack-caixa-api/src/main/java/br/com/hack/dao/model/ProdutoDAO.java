package br.com.hack.dao.model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.hack.dao.Conexao;
import br.com.hack.model.Produto;

public class ProdutoDAO {

	static ProdutoDAO instance = new ProdutoDAO();

	public static ProdutoDAO getInstance() {
		return instance;
	}

	public ProdutoDAO() {
	}

	public List<Produto> leTodos() {

		Statement stmt = null;
		List<Produto> lProduto = new ArrayList<Produto>();
		Connection conn = Conexao.getInstance().conectar();

		if (conn != null) {
			try {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUTO ORDER BY CO_PRODUTO asc");
				while (rs.next()) {
					
					Produto produto = new Produto();
					produto.setCodigoProduto(rs.getInt("CO_PRODUTO"));
					produto.setNomeProduto(rs.getString("NO_PRODUTO"));
					produto.setTaxaJuros(rs.getDouble("PC_TAXA_JUROS"));
					produto.setNumeroMinimoParcelas(rs.getInt("NU_MINIMO_MESES"));
					produto.setNumeroMaximoParcelas(rs.getInt("NU_MAXIMO_MESES"));
					produto.setValorMinimo(rs.getDouble("VR_MINIMO"));
					produto.setValorMaximo(rs.getDouble("VR_MAXIMO"));
					lProduto.add(produto);
					
				}
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}

		}

		return lProduto;

	}

}
