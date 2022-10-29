package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import Visao.Resultado;

public class Tabuleiro implements CampoObservador {

	private final int linhas;
	private final int colunas;
	
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<Resultado>>observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		super();
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVisinhos();
		sortearMinas();
	}
	
	public void paraCadaCampo(Consumer <Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public void registrarObservador(Consumer<Resultado> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		observadores.stream()
		.forEach(o -> o.accept( new Resultado(resultado)));
	}
  
	public void abrir(int linha , int coluna) {
		try {
			campos.parallelStream().filter(c -> c.getLinha() == linha && c.getColuna()== coluna)
			.findFirst()
			.ifPresent( c-> c.abrir()); 
			
		}catch(Exception e){
			//FIXME Ajustar a implemntação do metodo abrir
			campos.forEach( c -> c.setAberto(true));
			throw e;
		}
	}
	
	
	
	public void alternarMarcacao(int linha , int coluna) {
		campos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna()== coluna)
		.findFirst()
		.ifPresent( c-> c.alternarMarcacao());
	}
	
	

	private void gerarCampos() {
		// TODO Auto-generated method stub
		
		for(int linha = 0; linha < linhas ; linha++) {
			for(int coluna = 0; coluna < colunas ; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registrarObservador(this);
				campos.add(campo);
				
			}
		}
		
	}
	
	private void associarVisinhos() {
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
		
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			
			int aleatorio =  (int)(Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
			}while(minasArmadas < minas);
		
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch( c-> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		campos.stream().forEach( c -> c.reiniciar());
		sortearMinas();
	}
	
	public int getLinhas() {
		return linhas;
	}
	
	public int getColunas() {
		return colunas;
	}
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		
		if(evento == CampoEvento.EXPLODIR) {
			
			mostrarMinas();
			notificarObservadores(false);
			
		}else if(objetivoAlcancado()){
				notificarObservadores(true);
				
			}
		}
	
	private void mostrarMinas() {
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	 }
}
	
	

