package Visao;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Modelo.Tabuleiro;
import Visao.Resultado;
@SuppressWarnings({ "serial", "unused" })
public class PainelTabuleiro extends JPanel{

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		setLayout(new GridLayout(
				tabuleiro.getLinhas(),tabuleiro.getColunas()));
		
		tabuleiro.paraCadaCampo(c -> add( new BotaoCampo(c)));
		tabuleiro.registrarObservador( e -> {
			SwingUtilities.invokeLater(() ->{
			if(e.isGanhou()) {
				JOptionPane.showMessageDialog(this,"Ganhou :)");
			}else {
				JOptionPane.showMessageDialog(this,"Perdeu playboy");
			}
			tabuleiro.reiniciar();
			});
		});
	
 }
}