package com.heitor.ramos.gerarPdf;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * @author heitor.ramos
 *
 */
public class App {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Document document = new Document();
		BannerMeta bannerMeta = new BannerMeta();

		bannerMeta.Banner();

		LeituraProperties load = new LeituraProperties();
		Properties props = load.getProperties("application");

		try {

			String nomeDigitalização = JOptionPane.showInputDialog("Imagens Origens:\n\n "+ props.getProperty("arquivo.diretorio.entrada.imagens") + "\n\nDigite Nome : ");

			String nomeArquivo;

			nomeArquivo = Normalizer.normalize(nomeDigitalização, Normalizer.Form.NFD);
			nomeArquivo = nomeArquivo.replaceAll("[^\\p{ASCII}]", "").replaceAll(" ", "");

			PdfWriter.getInstance(document, new FileOutputStream((String) props.getProperty("arquivo.diretorio.cria.pdf") + nomeArquivo + ".pdf"));
			
			document.open();
			// document.add(new Paragraph("\n\n\n\n \t\tDocumento: "+nomeDigitalização));

			File arquivo = new File(props.getProperty("arquivo.diretorio.entrada.imagens"));
			File imagem[] = arquivo.listFiles();

			// document.add(new Paragraph("\n\n\n\n \t\tQuantidade de Páginas: "+imagem.length));

			for (int i = 0; imagem.length > i; i++) {
				File arquivos = imagem[i];

				System.out.println(props.getProperty("arquivo.diretorio.entrada.imagens") + arquivos.getName());

				document.newPage();
				Image figura2 = Image.getInstance(props.getProperty("arquivo.diretorio.entrada.imagens") + arquivos.getName());
				figura2.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
				float x = (PageSize.A4.getWidth() - figura2.getScaledWidth());
				float y = (PageSize.A4.getHeight() - figura2.getScaledHeight());
				figura2.setAbsolutePosition(x, y);
				document.add(figura2);

				Path arqIn = Paths.get(props.getProperty("arquivo.diretorio.entrada.imagens") + arquivos.getName());
				Path arqBk = Paths.get(props.getProperty("arquivo.diretorio.bkp.imagens") + arquivos.getName());

				Files.move(arqIn, arqBk);
				// Files.delete(arqIn);

			}

			JOptionPane.showMessageDialog(null,
					"Trabalho Concluido! " + "\n\n Quantidade de Imagens: " + imagem.length + "\n\n Imagens: "
							+ props.getProperty("arquivo.diretorio.bkp.imagens") + "\n\n Arquivo PDF em: "
							+ props.getProperty("arquivo.diretorio.cria.pdf") + nomeArquivo + ".pdf\n\n");

		} catch (DocumentException | IOException de) {
			// System.err.println(de.getMessage());
			JOptionPane.showMessageDialog(null, "ERRO \n\n Favor Limpa o Diretório Abaixo: \n\n " + de.getMessage());
		}
		document.close();
		System.out.print(getStringToDate("2019-09-01T09:50:20DA"));
	}

	public static Date getStringToDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
