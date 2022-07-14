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

import com.mjolnir.toolbox.app.PropertiesUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArquivoUtil {

    public static final String UTF8 = PropertiesUtil.getInstance().getPropertieByKey("util.encode");

    public static boolean move(File file, File file_destine) {
        if (file == null || file_destine == null) {
            return false;
        }
        FileOutputStream os = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            os = new FileOutputStream(file_destine);
            ArquivoUtil.garantirExistencia(file_destine.getParentFile(), true);
            if (!file.renameTo(file_destine) && file.length() > 0) {
                byte[] fileTemp = new byte[(int) file.length()];
                if (input.read(fileTemp) != fileTemp.length) {
                    log.warn("WARN: File over of max length size.");
                    return false;
                }
                os.write(fileTemp);
                if (!file.delete()) {
                    log.warn("WARN: the file cannot be deleted.");
                    return false;
                }
            }
        } catch (FileNotFoundException ex) {
            log.error("ERROR: File not found. ", ex);
            return false;
        } catch (IOException ex) {
            log.error("ERROR: Cannot read file. ", ex);
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
                log.error("ERROR: Cannot end to input/output file stream. ", ex);
            }
        }
        return true;
    }

    public static boolean move(String origin, String destine) throws IOException {
        if (origin == null || origin.isEmpty() || destine == null || destine.isEmpty()) {
            return false;
        }
        File file = new File(origin);
        File filedest = new File(destine);
        return ArquivoUtil.move(file, filedest);
    }

    public static boolean create(String filename, String content) throws IOException {
        if (filename == null || content == null) {
            return false;
        }
        return create(new File(filename), content.getBytes(UTF8), UTF8);
    }

    public static boolean create(File file, String content, String charset) throws IOException {
        if (content == null || charset == null || charset.isEmpty()) {
            return false;
        }
        return ArquivoUtil.create(file, content.getBytes(charset), charset);
    }

    public static boolean create(File file, String content) throws IOException {
        if (content == null) {
            return false;
        }
        return ArquivoUtil.create(file, content.getBytes(UTF8), UTF8);
    }

    public static boolean create(File file, byte[] content, String charset) throws IOException {
        if (file == null || content == null || content.length == 0) {
            return false;
        }
        try (OutputStreamWriter write = new OutputStreamWriter((OutputStream) new FileOutputStream(file), charset)) {
            if (!ArquivoUtil.garantirExistencia(file)) {
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
            log.error("ERROR: On file read. ", ex);
            return false;
        }
    }

    public static ArrayList<String> listFilesInPath(String path) throws Exception {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        File[] file_list = file.listFiles();
        if (file_list == null) {
            log.error("ERROR: Path do not exists.");
            return null;
        }
        if (file_list.length == 0) {
            log.warn("WARN: Path is Empty.");
            return null;
        }
        ArrayList<String> verified_file_list = new ArrayList<>();
        for (File list : file_list) {
            if (list.isDirectory()) {
                continue;
            }
            verified_file_list.add(list.getName());
        }
        return verified_file_list;
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        final int maxByte = 1024;
        int len;
        byte[] buffer = new byte[maxByte];
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    public static boolean copy(File origin, File destine, String charset) {
        if (origin == null || destine == null) {
            return false;
        }
        if (!origin.isFile()) {
            log.warn("WARN: The origin file does not exists: " + origin);
            return false;
        }
        try {
            if (!ArquivoUtil.garantirExistencia(destine)) {
                return false;
            }
            return ArquivoUtil.create(destine, ArquivoUtil.read(origin, charset), charset);
        } catch (IOException ex) {
            log.error("ERROR: Fail to read file. ", ex);
            return false;
        }
    }

    public static boolean copy(File origin, File destine) throws IOException {
        return ArquivoUtil.copy(origin, destine, UTF8);
    }

    public static boolean erase(String path) {
        if (path == null) {
            return false;
        }
        if (new File(path).delete()) {
            log.debug("File successful deleted.");
            return true;
        }
        log.warn("WARN: File cannot be deleted.");
        return false;
    }

    public static boolean erasePath(File path) {
        if (path == null) {
            return false;
        }
        if (!ArquivoUtil.cleanPath(path)) {
            log.warn("WARN: Cannot erase the path: " + path.getAbsolutePath());
            return false;
        }
        return path.delete();
    }

    public static boolean cleanPath(File path) {
        File[] oldFiles;
        if (path == null) {
            return false;
        }
        if (!path.isDirectory()) {
            if (path.isFile()) {
                throw new IllegalArgumentException("ERROR: The arg is a file not a path. ");
            }
            log.warn("WARN: The path does not exists: " + path.getAbsolutePath());
            return false;
        }
        oldFiles = path.listFiles();
        for (File oldFile : oldFiles) {
            if (oldFile.isFile()) {
                if (oldFile.delete()) {
                    continue;
                }
                log.warn("WARN: Cannot erase the file: " + oldFile.getAbsolutePath());
                continue;
            }
            if (ArquivoUtil.erasePath(oldFile)) {
                continue;
            }
            log.warn("WARN: Cannot erase the sub-path:" + oldFile.getAbsolutePath());
            return false;
        }
        return oldFiles.length == 0;
    }

    public static byte[] toByteArray(ZipInputStream in) throws IOException {
        return ArquivoUtil.toByteArray((InputStream) in);
    }

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
            log.warn("ERROR: Fail to read file. ", ex);
        }
        return buffer.toByteArray();
    }

    public static String read(File aFile) {
        return ArquivoUtil.read(aFile, UTF8, true);
    }

    public static String read(File aFile, String charset) {
        return ArquivoUtil.read(aFile, charset, true);
    }

    public static String read(File aFile, String charset, boolean systemLineSeparator) {
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
                    throw new IllegalArgumentException("ERROR: The length of file is over of 2147483647 bytes");
                }
                char[] chars = new char[(int) aFile.length()];
                reader.read(chars);
                return new String(chars).trim();
            } catch (FileNotFoundException ex) {
                log.error("ERROR: The file does not found. ", ex);
            } catch (IOException ex) {
                log.error("ERROR: Fail to read the file. ", ex);
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
                    log.error(ex.getMessage(), (Throwable) ex);
                }
            }
        }
        return contents.toString();
    }

    public static boolean isMaxSize(File file, long max_size_kb) {
        final int maxByte = 1024;
        if (file == null) {
            return false;
        }

        long size = file.length();
        int size_in_kb = (int) (size / maxByte);

        return (long) size_in_kb < max_size_kb;
    }

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
            log.error("ERROR: Fail to read the file. ", ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                log.error("ERROR: Fail to close the FileInputStream. ", ex);
            }
        }
        return b;
    }

    public static byte[] toByteArray(String file_path) throws FileNotFoundException, IOException {
        if (file_path == null) {
            return null;
        }
        return ArquivoUtil.toByteArray(new File(file_path));
    }

    public static boolean garantirExistencia(File file, boolean createDirectory) throws IOException {
        if (file == null) {
            log.warn("WARN: The file is null.");
            return false;
        }
        if (!file.exists()) {
            if (createDirectory) {
                if (!file.mkdirs()) {
                    return false;
                }
            } else {
                if (file.getParentFile() != null && !file.getParentFile()
                        .isDirectory() && !file.getParentFile().mkdirs()) {
                    return false;
                }
                if (!file.createNewFile()) {
                    return false;
                }
            }
        } else {
            if (createDirectory && file.isFile()) {
                log.warn("WARN: Existe um file com o nome do"
                        + " diretorio cuja criacao foi solicitada: "
                        + file.getAbsolutePath());
                return false;
            }
            if (!createDirectory && file.isDirectory()) {
                log.warn((Object) "WARN: Existe uma pasta com o nome do file"
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
