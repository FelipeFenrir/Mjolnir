/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaria para manipulação de arquivo.
 *
 * @author Felipe de Andrade Batista
 */
public class ArquivoUtil {

    /**
     * Encode de Arquivo.
     */
    public static final String UTF8 = PropertiesUtil.getInstance().getPropertieByKey("util.encode");

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoUtil.class);

    /**
     * Move o arquivo para outra pasta.
     * @param file Aquivo de origem.
     * @param filedest Arquivo de destino.
     * @return True caso tenha sucesso em mover.
     */
    public static boolean mover(File file, File filedest) {
        if (file == null || filedest == null) {
            return false;
        }
        FileOutputStream os = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            os = new FileOutputStream(filedest);
            ArquivoUtil.garantirExistencia(filedest.getParentFile(), true);
            if (!file.renameTo(filedest) && file.length() > 0) {
                byte[] arquivoTemp = new byte[(int) file.length()];
                if (input.read(arquivoTemp) != arquivoTemp.length) {
                    LOG.warn("WARN: com o tamanho do arquivo e/ou leitura.");
                    return false;
                }
                os.write(arquivoTemp);
                if (!file.delete()) {
                    LOG.warn("WARN: O arquivo não pode ser deletado.");
                    return false;
                }
            }
        } catch (FileNotFoundException ex) {
            LOG.error("ERROR: Não foi encontrado o arquivo. ", ex);
            return false;
        } catch (IOException ex) {
            LOG.error("ERROR: Não foi possivel ler o arquivo. ", ex);
            return false;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException ex) {
                LOG.error("ERROR: Não foi possivel encerrar o input e/ou output. ", ex);
            }
        }
        return true;
    }

    /**
     * Move o arquivo para outra pasta.
     * @param origem Aquivo de origem.
     * @param destino Arquivo de destino.
     * @return True caso tenha sucesso em mover.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean mover(String origem, String destino) throws IOException {
        if (origem == null || origem.isEmpty() || destino == null || destino.isEmpty()) {
            return false;
        }
        File file = new File(origem);
        File filedest = new File(destino);
        return ArquivoUtil.mover(file, filedest);
    }

    /**
     * Cria um arquivo com base em seu nome e um conteudo em string.
     * @param filename nome do arquivo
     * @param content conteudo do arquivo
     * @return Retorna true caso o arquivo tenha sido salvo com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean salvar(String filename, String content) throws IOException {
        if (filename == null || content == null) {
            return false;
        }
        return salvar(new File(filename), content.getBytes(UTF8), UTF8);
    }

    /**
     * Cria um arquivo com base em seu nome e um conteudo em string.
     * @param arquivo Objeto File
     * @param content conteudo do arquivo
     * @param charset File de Charset do Arquivo, como UTF-8
     * @return Retorna true caso o arquivo tenha sido salvo com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean salvar(File arquivo, String content, String charset) throws IOException {
        if (content == null || charset == null || charset.isEmpty()) {
            return false;
        }
        return ArquivoUtil.salvar(arquivo, content.getBytes(charset), charset);
    }

    /**
     * Cria um arquivo com base em seu nome e um conteudo em string.
     * @param arquivo Objeto File
     * @param content conteudo do arquivo
     * @return Retorna true caso o arquivo tenha sido salvo com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean salvar(File arquivo, String content) throws IOException {
        if (content == null) {
            return false;
        }
        return ArquivoUtil.salvar(arquivo, content.getBytes(UTF8), UTF8);
    }

    /**
     * Cria um arquivo com base em seu nome e um conteudo em string.
     * @param arquivo Objeto File
     * @param content conteudo do arquivo
     * @param charset Charset do Arquivo
     * @return Retorna true caso o arquivo tenha sido salvo com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean salvar(File arquivo, byte[] content, String charset) throws IOException {
        if (arquivo == null || content == null || content.length == 0) {
            return false;
        }
        try (OutputStreamWriter write = new OutputStreamWriter((OutputStream) new FileOutputStream(arquivo), charset)) {
            if (!ArquivoUtil.garantirExistencia(arquivo)) {
                return false;
            }
            Throwable throwable = null;
            try {
                write.flush();
                write.write(new String(content, Charset.forName(charset)));
            } catch (IOException var4_6) {
                throwable = var4_6;
                throw var4_6;
            } finally {
                if (throwable != null) {
                    try {
                        write.close();
                    } catch (IOException var4_5) {
                        throwable.addSuppressed(var4_5);
                    }
                } else {
                    write.close();
                }
            }
            return true;
        } catch (IOException ex) {
            LOG.error("ERROR: Para leitura de Arquivo. ", ex);
            return false;
        }
    }

    /**
     * Lista os arquivos contidos em um diretorio.
     * @param caminho Diretorio
     * @return Lista de nome de Arquivos.
     * @throws Exception
     */
    public static ArrayList<String> listarDiretorio(String caminho) throws Exception {
        if (caminho == null) {
            return null;
        }
        File arquivo = new File(caminho);
        File[] lista = arquivo.listFiles();
        if (lista == null) {
            LOG.error("ERROR: O diretorio informado nao existe.");
            return null;
        }
        if (lista.length == 0) {
            LOG.warn("WARN: Nao foi encontrado nenhum arquivo.");
            return null;
        }
        ArrayList<String> listaArquivos = new ArrayList<>();
        for (File lista1 : lista) {
            if (lista1.isDirectory()) {
                continue;
            }
            listaArquivos.add(lista1.getName());
        }
        return listaArquivos;
    }

    /**
     * Copia um arquivo.
     * @param in InputStream de Origem.
     * @param out OutputStream de Destino.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    static final void copiar(InputStream in, OutputStream out) throws IOException {
        final int maxByte = 1024;
        int len;
        byte[] buffer = new byte[maxByte];
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Copia um arquivo.
     * @param origem Arquivo de Origem.
     * @param destino Arquivo de Destino.
     * @param charset Tipo de Charset.
     * @return True caso a cópia tenha sido feita com sucesso.
     */
    public static boolean copiar(File origem, File destino, String charset) {
        if (origem == null || destino == null) {
            return false;
        }
        if (!origem.isFile()) {
            LOG.warn("WARN: O arquivo de origem nao existe para ser"
                    + " copiado: " + origem);
            return false;
        }
        try {
            if (!ArquivoUtil.garantirExistencia(destino)) {
                return false;
            }
            return ArquivoUtil.salvar(destino, ArquivoUtil.ler(origem, charset), charset);
        } catch (IOException ex) {
            LOG.error("ERROR: Falha ao realizar a leitura do arquivo. ", ex);
            return false;
        }
    }

    /**
     * Copia um arquivo.
     * @param origem Arquivo de Origem.
     * @param destino Arquivo de Destino.
     * @return True caso a cópia tenha sido feita com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean copiar(File origem, File destino) throws IOException {
        return ArquivoUtil.copiar(origem, destino, UTF8);
    }

    /**
     * Apaga o arquivo.
     * @param caminhoArquivo Arquivo a ser apagado.
     * @return True caso tenha sucesso.
     */
    public static boolean apagar(String caminhoArquivo) {
        if (caminhoArquivo == null) {
            return false;
        }
        if (new File(caminhoArquivo).delete()) {
            LOG.debug("Arquivo apagado com sucesso.");
            return true;
        }
        LOG.warn("WARN: Arquivo nao pode ser apagado.");
        return false;
    }

    /**
     * Apaga o diretorio.
     * @param caminhoDiretorio Diretorio a ser apagado.
     * @return True caso tenha sucesso.
     */
    public static boolean apagarDiretorio(File caminhoDiretorio) {
        if (caminhoDiretorio == null) {
            return false;
        }
        if (!ArquivoUtil.limparDiretorio(caminhoDiretorio)) {
            LOG.warn("WARN: Nao foi possivel esvaziar o diretorio: "
                    + caminhoDiretorio.getAbsolutePath());
            return false;
        }
        return caminhoDiretorio.delete();
    }

    /**
     * Apaga os arquivos em um diretório.
     * @param caminhoDiretorio Diretorio a ser apagado.
     * @return True caso tenha sucesso.
     */
    public static boolean limparDiretorio(File caminhoDiretorio) {
        File[] oldFiles;
        if (caminhoDiretorio == null) {
            return false;
        }
        if (!caminhoDiretorio.isDirectory()) {
            if (caminhoDiretorio.isFile()) {
                throw new IllegalArgumentException("ERRO: O parametro"
                        + " especificado aponta para um arquivo, e nao para um diretorio. ");
            }
            LOG.warn("WARN: O diretorio nao existe para que seja"
                    + " apagado: " + caminhoDiretorio.getAbsolutePath());
            return false;
        }
        oldFiles = caminhoDiretorio.listFiles();
        for (File oldFile : oldFiles) {
            if (oldFile.isFile()) {
                if (oldFile.delete()) {
                    continue;
                }
                LOG.warn("WARN: Nao foi possivel apagar o arquivo: "
                        + oldFile.getAbsolutePath());
                continue;
            }
            if (ArquivoUtil.apagarDiretorio(oldFile)) {
                continue;
            }
            LOG.warn("WARN: Nao foi possivel apagar o subdiretorio:"
                    + oldFile.getAbsolutePath());
            return false;
        }
        return caminhoDiretorio.listFiles().length == 0;
    }

    /**
     * Transforma o arquivo em Byte Array.
     * @param in Objeto ZipInputStream que representa o arquivo
     * @return Retorna o Byte Array que representa o arquivo.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] toByteArray(ZipInputStream in) throws IOException {
        return ArquivoUtil.toByteArray((InputStream) in);
    }

    /**
     * Transforma o arquivo em Byte Array.
     * @param in Objeto InputStream que representa o arquivo
     * @return Retorna o Byte Array que representa o arquivo.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        final int maxByte = 2048;
        if (in == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[maxByte];

        int bytesRead = -1;
        try {
            while ((bytesRead = in.read(bytes)) != -1) {
                buffer.write(bytes, 0, bytesRead);
            }
        } catch (IOException ex) {
            LOG.warn("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
        }
        return buffer.toByteArray();
    }

    /**
     * Lê um arquivo.
     * @param aFile Arquivo.
     * @return String que representa o arquivo.
     */
    public static String ler(File aFile) {
        return ArquivoUtil.ler(aFile, UTF8, true);
    }

    /**
     * Lê um arquivo.
     * @param aFile Arquivo.
     * @param charset Charset do Arquivo (Ex: UTF-8).
     * @return String que representa o arquivo.
     */
    public static String ler(File aFile, String charset) {
        return ArquivoUtil.ler(aFile, charset, true);
    }

    /**
     * Lê um arquivo.
     * @param aFile Arquivo.
     * @param charset Charset do Arquivo (Ex: UTF-8).
     * @param systemLineSeparator Separador de Linha do SO (Sistema Operacional).
     * @return String que representa o arquivo.
     */
    public static String ler(File aFile, String charset, boolean systemLineSeparator) {
        StringBuilder contents;
        block28:
        {
            contents = new StringBuilder();
            if (aFile == null) {
                return null;
            }
            BufferedReader input = null;
            FileInputStream fileInput = null;
            InputStreamReader reader = null;
            try {
                fileInput = new FileInputStream(aFile);
                reader = null;
                if (charset == null || charset.trim().isEmpty()) {
                    return null;
                }
                reader = new InputStreamReader((InputStream) fileInput, charset);
                if (systemLineSeparator) {
                    input = new BufferedReader(reader);
                    String line = input.readLine();
                    while (line != null) {
                        contents.append(line);
                        line = input.readLine();
                        if (line == null) {
                            continue;
                        }
                        contents.append(System.getProperty("line.separator"));
                    }
                    break block28;
                }
                if (aFile.length() > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("ERRO: O tamanho do"
                            + " arquivo excede o limite maximo de 2147483647 bytes");
                }
                char[] chars = new char[(int) aFile.length()];
                reader.read(chars);
                return new String(chars).trim();
            } catch (FileNotFoundException ex) {
                LOG.error("ERROR: O arquivo não foi encontrado. ", ex);
            } catch (IOException ex) {
                LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (fileInput != null) {
                        fileInput.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    LOG.error(ex.getMessage(), (Throwable) ex);
                }
            }
        }
        return contents.toString();
    }

    /**
     * Verifica o arquivo de acordo com um tamanho limite.
     * @param arquivo Arquivo a ser verificado.
     * @param tamanhoLimiteKB Tamanho limite do arquivo.
     * @return True caso o arquivo esteja dentro do tamanho limite.
     */
    public static boolean tamanhoLimite(File arquivo, long tamanhoLimiteKB) {
        final int maxByte = 1024;
        if (arquivo == null) {
            return false;
        }

        long tamanho = arquivo.length();
        int tamanhoKB = (int) (tamanho / maxByte);

        return (long) tamanhoKB < tamanhoLimiteKB;
    }

    /**
     * Transforma o arquivo em Byte Array.
     * @param file
     * @return Retorna o Byte Array que representa o arquivo.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] toByteArray(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream f = null;
        byte[] b = null;
        try {
            //b = ler(file).getBytes(UTF8);
            b = new byte[(int) file.length()];
            f = new FileInputStream(file);
            f.read(b);
        } catch (IOException ex) {
            LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                LOG.error("ERROR: Falha ao fechar o FileInputStream. ", ex);
            }
        }
        return b;
    }

    /**
     * Transforma o arquivo em Byte Array.
     * @param caminhoArquivo Path do Arquivo.
     * @return Retorna o Byte Array que representa o arquivo.
     * @throws FileNotFoundException Arquivo nao encontrado.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] toByteArray(String caminhoArquivo) throws FileNotFoundException, IOException {
        if (caminhoArquivo == null) {
            return null;
        }
        return ArquivoUtil.toByteArray(new File(caminhoArquivo));
    }

    /**
     * Garante que o diretorio ou arquivo existe e caso seja diretório ele pode ser criado.
     * @param arquivo Nome do arquivo ou diretório.
     * @param createDirectory Flag de criação de diretório.
     * @return True caso exista o diretório ou arquivo.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean garantirExistencia(File arquivo, boolean createDirectory) throws IOException {
        if (arquivo == null) {
            LOG.warn("WARN: O arquivo nao pode ser null.");
            return false;
        }
        if (!arquivo.exists()) {
            if (createDirectory) {
                if (!arquivo.mkdirs()) {
                    return false;
                }
            } else {
                if (arquivo.getParentFile() != null && !arquivo.getParentFile()
                        .isDirectory() && !arquivo.getParentFile().mkdirs()) {
                    return false;
                }
                if (!arquivo.createNewFile()) {
                    return false;
                }
            }
        } else {
            if (createDirectory && arquivo.isFile()) {
                LOG.warn("WARN: Existe um arquivo com o nome do"
                        + " diretorio cuja criacao foi solicitada: "
                        + arquivo.getAbsolutePath());
                return false;
            }
            if (!createDirectory && arquivo.isDirectory()) {
                LOG.warn((Object) "WARN: Existe uma pasta com o nome do arquivo"
                        + " cuja criacao foi solicitada.");
                return false;
            }
        }
        return true;
    }

    /**
     * Garante que o diretorio ou arquivo existe.
     * @param arquivo Nome do arquivo ou diretório.
     * @return True caso exista o diretório ou arquivo.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static boolean garantirExistencia(File arquivo) throws IOException {
        return ArquivoUtil.garantirExistencia(arquivo, false);
    }

    /**
     * Garante que existe uma barra ao iniciar o path do arquivo ou diretório.
     * @param path Caminho do arquivo ou diretório.
     * @return String corrigida com o path.
     */
    public static String garantirBarraInicio(String path) {
        if (path == null) {
            return null;
        }
        if (!path.startsWith("/") && !path.startsWith("\\") && path.indexOf(":") <= 0) {
            path = (path.indexOf("\\") > 0 ? "\\" : "/") + path;
        }
        return path;
    }

    /**
     * Garante que existe uma barra ao fim do path do arquivo ou diretório.
     * @param dirPath Caminho do arquivo ou diretório.
     * @return String corrigida com o path.
     */
    public static String garantirBarraFim(String dirPath) {
        final int max = 92;
        if (dirPath == null) {
            return null;
        }
        if (!dirPath.matches(".*[\\\\/]")) {
            dirPath = dirPath + (dirPath.indexOf(max) >= 0 ? "\\" : "/");
        }
        return dirPath;
    }

    /**
     * Remove a barra de inicio do Path.
     * @param path Path do arquivo ou diretório.
     * @return String do Path Corrigida.
     */
    public static String removerBarraInicio(String path) {
        if (path == null) {
            return null;
        }
        if (path.startsWith("/") || path.startsWith("\\")) {
            path = path.substring(1);
        } else if (path.indexOf(":") > 0) {
            path = path.substring(path.indexOf(":") + 1);
        }
        return path;
    }

    /**
     * Pega o caminho relativo no classpath.
     * @param caminhoRelativo
     * @return
     * @throws FileNotFoundException Arquivo nao encontrado.
     */
    public static File getArquivoRelativoClasspath(String caminhoRelativo) throws FileNotFoundException {
        URL url = null;
        try {
            url = ArquivoUtil.class.getResource(caminhoRelativo);
        } catch (Exception e) {
            LOG.error("ERROR: Falha ao recuperar recursos. ", e);
        }
        if (url == null) {
            return null;
        }
        return new File(url.getPath());
    }

    /**
     * Pega o caminho relativo no classpath.
     * @param caminhoRelativo
     * @return
     * @throws FileNotFoundException Arquivo nao encontrado.
     */
    public static InputStream getInputStreamRelativoClasspath(String caminhoRelativo) throws FileNotFoundException {
        if (caminhoRelativo == null) {
            return null;
        }
        return ArquivoUtil.class.getResourceAsStream(caminhoRelativo);
    }

    /**
     * Zipa o arquivo ou pasta.
     * @param arqSaida caminho e nome do Arquivo Zipado.
     * @param diretorio Diretorio a ser Zipado.
     * @return True caso tenha executado com sucesso.
     */
    public static boolean zip(String arqSaida, String diretorio) {
        final int maxByte = 2048;
        byte[] dados = new byte[maxByte];
        File f;
        BufferedInputStream origem;
        FileInputStream streamDeEntrada;
        FileOutputStream destino;
        ZipOutputStream saida;
        ZipEntry entry;

        try {
            destino = new FileOutputStream(arqSaida);
            saida = new ZipOutputStream(new BufferedOutputStream(destino));
            f = new File(diretorio);
            String[] arquivos = f.list();
            for (String s : arquivos) {
                int cont;
                File arquivo = new File(diretorio + s);
                if (!arquivo.isFile()) {
                    continue;
                }
                LOG.debug("Compactando: " + s);
                streamDeEntrada = new FileInputStream(arquivo);
                origem = new BufferedInputStream(streamDeEntrada, maxByte);
                entry = new ZipEntry(diretorio + s);
                saida.putNextEntry(entry);
                while ((cont = origem.read(dados, 0, maxByte)) != -1) {
                    saida.write(dados, 0, cont);
                }
                origem.close();
            }
            saida.close();
            return true;
        } catch (IOException e) {
            LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", e);
            return false;
        }
    }

    /**
     * Zipa o arquivo.
     * @param file Nome do Arquivo Zipado.
     * @param level Profundidade de busca para Zipar.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static ByteArrayOutputStream zip(File file, int level) throws IOException {
        if (file == null) {
            return null;
        }
        String entryName = file.getName();
        FileInputStream fis = new FileInputStream(file);
        return ArquivoUtil.zip(fis, level, entryName);
    }

    /**
     * Zipa o arquivo.
     * @param is Nome do InputStream Zipado.
     * @param level Profundidade de busca para Zipar.
     * @param entryName nome do arquivo.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static ByteArrayOutputStream zip(InputStream is, int level, String entryName) throws IOException {
        if (is == null || entryName == null || entryName.isEmpty()) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        ZipOutputStream zipOutputStream = new ZipOutputStream(baos);
        Throwable throwable = null;
        try {
            int bytesRead;
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            zipOutputStream.setLevel(level);
            byte[] buffer = new byte[8192];
            while ((bytesRead = is.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }
            is.close();
        } catch (IOException buffer) {
            throwable = buffer;
            throw buffer;
        } finally {
            if (throwable != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException buffer) {
                    throwable.addSuppressed(buffer);
                }
            } else {
                zipOutputStream.close();
            }
        }
        return baos;
    }

    /**
     * Zipa o arquivo.
     * @param arquivo Byte Array de Aquivo.
     * @param level Profundidade de busca para Zipar.
     * @param entryName nome do arquivo.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] zip(byte[] arquivo, int level, String entryName) throws IOException {
        if (arquivo == null || entryName == null || entryName.isEmpty()) {
            return null;
        }
        try {
            ZipOutputStream zipOutputStream;
            ByteArrayOutputStream baos;
            ByteArrayInputStream is = new ByteArrayInputStream(arquivo);
            Throwable throwable = null;
            try {
                int bytesRead;
                baos = new ByteArrayOutputStream(4096);
                zipOutputStream = new ZipOutputStream(baos);
                zipOutputStream.putNextEntry(new ZipEntry(entryName));
                zipOutputStream.setLevel(level);
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException buffer) {
                throwable = buffer;
                throw buffer;
            } finally {
                if (throwable != null) {
                    try {
                        is.close();
                    } catch (IOException buffer) {
                        throwable.addSuppressed(buffer);
                    }
                } else {
                    is.close();
                }
            }
            zipOutputStream.close();
            return baos.toByteArray();
        } catch (IOException ex) {
            LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
            return null;
        }
    }

    /**
     * Zipa o arquivo.
     * @param arquivo Byte Array de Aquivo.
     * @param level Profundidade de busca para Zipar.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] zip(byte[] arquivo, int level) throws IOException {
        if (arquivo == null) {
            return null;
        }
        try {
            ZipOutputStream zipOutputStream;
            ByteArrayOutputStream baos;
            ByteArrayInputStream is = new ByteArrayInputStream(arquivo);
            Throwable throwable = null;
            try {
                int bytesRead;
                baos = new ByteArrayOutputStream(4096);
                zipOutputStream = new ZipOutputStream(baos);
                zipOutputStream.putNextEntry(new ZipEntry("arquivo.xml"));
                zipOutputStream.setLevel(level);
                byte[] buffer = new byte[8192];
                while ((bytesRead = is.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }
            } catch (Throwable buffer) {
                throwable = buffer;
                throw buffer;
            } finally {
                if (throwable != null) {
                    try {
                        is.close();
                    } catch (Throwable buffer) {
                        throwable.addSuppressed(buffer);
                    }
                } else {
                    is.close();
                }
            }
            zipOutputStream.close();
            return baos.toByteArray();
        } catch (IOException ex) {
            LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
            return null;
        }
    }

    /**
     * Zipa o arquivo.
     * @param file Aquivo.
     * @param level Profundidade de busca para Zipar.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] zipByte(File file, int level) throws IOException {
        if (file == null) {
            return null;
        }
        String entryName = file.getName();
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = ArquivoUtil.zip(fis, level, entryName);
        return baos.toByteArray();
    }

    /**
     * Zipa o arquivo.
     * @param is InputStream representando um Arquivo.
     * @param level Profundidade de busca para Zipar.
     * @param entryName Nome do Arquivo.
     * @return True caso tenha executado com sucesso.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] zipByte(InputStream is, int level, String entryName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        baos = ArquivoUtil.zip(is, level, entryName);
        if (baos == null) {
            return null;
        }
        return baos.toByteArray();
    }

    /**
     * Deszipa o arquivo.
     * @param input InputStream representando o arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] unzip(InputStream input) throws IOException {
        if (input == null) {
            return null;
        }
        ZipInputStream zis = new ZipInputStream(input);
        zis.getNextEntry();
        return ArquivoUtil.toByteArray(zis);
    }

    /**
     * Deszipa o arquivo.
     * @param arquivo ByteArray representando o arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String unzip(byte[] arquivo) throws IOException {
        if (arquivo == null) {
            return null;
        }
        return new String(ArquivoUtil.unzip(new ByteArrayInputStream(arquivo)), UTF8);
    }

    /**
     * Deszipa o arquivo.
     * @param xml ByteArray representando o arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String descompactarZipOuGZip(byte[] xml) throws IOException {
        String xmlRet = "";
        if (xml == null) {
            return null;
        }
        if (xml.length == 0) {
            return xmlRet;
        }
        xmlRet = ArquivoUtil.isGzipStream(xml) ? ArquivoUtil.descompactarGZip(xml) : ArquivoUtil.unzip(xml);
        return xmlRet;
    }

    /**
     * Ler arquivo reservando linha a linha em uma lista.
     * @param aFile Objeto File, arquivo a ser lido.
     * @return Lista de Strings.
     */
    public static LinkedList<String> lerArquivo(File aFile) {
        return lerArquivo(aFile, UTF8);
    }

    /**
     * Ler arquivo reservando linha a linha em uma lista.
     * @param aFile Objeto File, arquivo a ser lido.
     * @param charset Charset do Arquivo.
     * @return Lista de Strings.
     */
    public static LinkedList<String> lerArquivo(File aFile, String charset) {
        LinkedList<String> lista;
        lista = new LinkedList<>();
        if (aFile == null) {
            return null;
        }
        BufferedReader input = null;
        FileInputStream fileInput = null;
        InputStreamReader reader = null;
        try {
            fileInput = new FileInputStream(aFile);
            reader = null;
            reader = new InputStreamReader(fileInput, charset);
            input = new BufferedReader(reader);
            String line = null;
            while ((line = input.readLine()) != null) {
                lista.add(line);
            }
        } catch (FileNotFoundException ex) {
            LOG.error("ERROR: O arquivo não foi encontrado. ", ex);
        } catch (IOException ex) {
            LOG.error("ERROR: Falha ao realizar a leitura do Arquivo. ", ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (fileInput != null) {
                    fileInput.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                LOG.error(ex.getMessage(), (Throwable) ex);
            }
        }
        return lista;
    }

    /**
     * Converte um InputStream em String com Encode UTF-8.
     * @param is Objeto InputStream.
     * @return String representando o InputStream.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String toString(InputStream is) throws IOException {
        byte[] result = ArquivoUtil.toByteArray(is);
        if (result == null) {
            return null;
        }
        return new String(result, UTF8);
    }

    /**
     * Converte um InputStream em String.
     * @param is Objeto InputStream.
     * @return String representando o InputStream.
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String toStringEncode(InputStream is) throws IOException {
        byte[] result = ArquivoUtil.toByteArray(is);
        if (result == null) {
            return null;
        }
        return new String(result);
    }

    /**
     * Compacta em um arquivo Gzip.
     * @param str Arquivo a ser compactado.
     * @return
     */
    public static String compactarGZip(String str) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            GZIPOutputStream gz = new GZIPOutputStream(bos);
            Throwable throwable = null;
            try {
                gz.write(str.getBytes(UTF8));
                bos.close();
            } catch (Throwable var4_6) {
                throwable = var4_6;
                throw var4_6;
            } finally {
                if (throwable != null) {
                    try {
                        gz.close();
                    } catch (Throwable var4_5) {
                        throwable.addSuppressed(var4_5);
                    }
                } else {
                    gz.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            //e.printStackTrace();
        }
        return DatatypeConverter.printBase64Binary(bos.toByteArray());
    }

    /**
     * Compacta em um arquivo Gzip.
     * @param xml Arquivo a ser compactado.
     * @return
     */
    public static byte[] compactarGZip(byte[] xml) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            GZIPOutputStream gz = new GZIPOutputStream(bos);
            Throwable throwable = null;
            try {
                gz.write(xml);
                bos.close();
            } catch (Throwable var4_6) {
                throwable = var4_6;
                throw var4_6;
            } finally {
                if (throwable != null) {
                    try {
                        gz.close();
                    } catch (Throwable var4_5) {
                        throwable.addSuppressed(var4_5);
                    }
                } else {
                    gz.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            //e.printStackTrace();
        }
        return bos.toByteArray();
    }

    /**
     * Descompacta o Gzip.
     * @param xml Array de Bytes representando o Arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String descompactarGZip(byte[] xml) throws IOException {
        return ArquivoUtil.descompactarGZip(DatatypeConverter.printBase64Binary(xml));
    }

    /**
     * Descompacta o Gzip.
     * @param contentBytes Array de Bytes representando o Arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] decompressGZip(byte[] contentBytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy((InputStream) new GZIPInputStream(new ByteArrayInputStream(contentBytes)), (OutputStream) out);
        return out.toByteArray();
    }

    /**
     * Descompacta o Gzip.
     * @param xml Array de Bytes representando o Arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static byte[] decompressZipOuGZip(byte[] xml) throws IOException {
        byte[] xmlRet = null;
        if (xml == null) {
            return null;
        }
        if (xml.length == 0) {
            return xmlRet;
        }
        if (ArquivoUtil.isGzipStream(xml)) {
            xmlRet = ArquivoUtil.decompressGZip(xml);
        } else {
            String xmlRet2 = ArquivoUtil.unzip(xml);
            xmlRet = ArquivoUtil.decompressGZip(ArquivoUtil.compactarGZip(xmlRet2.getBytes()));
        }
        return xmlRet;
    }

    /**
     * Descompacta o Gzip.
     * @param str Nome do Arquivo.
     * @return
     * @throws IOException ApiError de Leitura de Arquivo.
     */
    public static String descompactarGZip(String str) throws IOException {
        String line;
        StringBuilder s1 = new StringBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(str));
        GZIPInputStream gs = new GZIPInputStream(bais);
        InputStreamReader xover = new InputStreamReader(gs);
        BufferedReader is = new BufferedReader(xover);
        while ((line = is.readLine()) != null) {
            s1.append(line);
        }
        return s1.toString();
    }

    /**
     * Verifica se é um arquivo Gzip.
     * @param bytes Array de Bytes representando o Arquivo.
     * @return True se for GZIP ou false
     */
    public static boolean isGzipStream(byte[] bytes) {
        int head = bytes[0] & 255 | bytes[1] << 8 & 65280;
        return 35615 == head;
    }
}
