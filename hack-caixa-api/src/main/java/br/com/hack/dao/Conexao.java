package br.com.hack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

	private static Conexao instance = new Conexao();

	public static Conexao getInstance() {
		return instance;
	}

	private Conexao() {
	}

	public Connection conectar() {
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String strConexao = "jdbc:sqlserver://dbhackathon.database.windows.net:1433;databaseName=hack";
		String usuario = "hack";
		String senha = "Password23";

		Connection conn = null;

		try {
			Class.forName(driver);
			System.out.println("Driver registrado");
			conn = DriverManager.getConnection(strConexao, usuario, senha);
			System.out.println("Conexao efetuada");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver do banco nao encontrado");
		} catch (SQLException ex) {
			System.out.println("Erro ao obter conexao: " + ex.getMessage());
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return conn;
	}
}
