package com.videojj.videoservice.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {

    private static final int  BUFFER_SIZE = 2 * 1024;

    /**
     * 解压
     * @param zipFile
     * @param unzipFile
     */
    public static void unzip(File zipFile, File unzipFile) {
        File warFile = zipFile;
        try {
            //获得输出流
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream(warFile));
            ArchiveInputStream in = new ArchiveStreamFactory()
                    .createArchiveInputStream(ArchiveStreamFactory.JAR,
                            bufferedInputStream);
            JarArchiveEntry entry = null;
            //循环遍历解压
            while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(unzipFile, entry.getName()).mkdir();
                } else {
                    OutputStream out = FileUtils.openOutputStream(unzipFile);
                    IOUtils.copy(in, out);
                    out.close();
                }
            }
            in.close();
        } catch (Exception e) {
            log.error("解压出错：",e);
        }
    }

    /**
     * 压缩
     *
     */
    public static void zip(File sourceFile , File destZipFile ) {
        File outFile = destZipFile;
        try {
            outFile.createNewFile();
            //创建文件
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(outFile));
            ArchiveOutputStream out = new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.JAR,
                            bufferedOutputStream);


            File file = sourceFile;
            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file,
                    file.getName());
            out.putArchiveEntry(zipArchiveEntry);
            IOUtils.copy(new FileInputStream(file), out);
            out.closeArchiveEntry();

            out.finish();
            out.close();
        } catch (Exception e) {
            log.error("压缩出错：",e);
        }
    }

    /**
     * 压缩文件
     *
     * @param filePath
     *            待压缩的文件路径
     * @return 压缩后的文件
     */
    public static File zip(String filePath) {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                target.delete(); // 删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 添加对应的文件Entry
                addEntry("/", source, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                closeQuietly(zos, fos);
            }
        }
        return target;
    }


    /**
     * 扫描添加文件Entry
     *
     * @param base
     *            基路径
     *
     * @param source
     *            源文件
     * @param zos
     *            Zip文件输出流
     * @throws IOException
     */
    private static void addEntry(String base, File source, ZipOutputStream zos)
            throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
        String entry = base + source.getName();
        if (source.isDirectory()) {
            for (File file : source.listFiles()) {
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(entry + "/", file, zos);
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } finally {
                closeQuietly(bis, fis);
            }
        }
    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables
     *            可关闭的流对象列表
     * @throws IOException
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables
     *            可关闭的流对象列表
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {

            log.error("关闭流出错",e);
        }
    }

    public static void main(String args[]){

        try {
            /** 测试压缩方法1  */

            FileOutputStream fos1 = new FileOutputStream(new File("/Users/peng///Desktop/ceshiyasuo.zip"));

            ZipUtil.toZip("/Users/peng///Desktop/ceshiyasuo", fos1, true);


            /** 测试压缩方法2  */

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**

     * 压缩成ZIP 方法1

     * @param srcDir 压缩文件夹路径

     * @param out    压缩文件输出流

     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;

     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)

     * @throws RuntimeException 压缩失败会抛出运行时异常

     */

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)

            throws RuntimeException{



        long start = System.currentTimeMillis();

        ZipOutputStream zos = null ;

        try {

            zos = new ZipOutputStream(out);

            File sourceFile = new File(srcDir);

            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);

            long end = System.currentTimeMillis();

        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils",e);

        }finally{

            if(zos != null){

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }



    }



    /**

     * 压缩成ZIP 方法2

     * @param srcFiles 需要压缩的文件列表

     * @param out           压缩文件输出流

     * @throws RuntimeException 压缩失败会抛出运行时异常

     */

    public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {

        long start = System.currentTimeMillis();

        ZipOutputStream zos = null ;

        try {

            zos = new ZipOutputStream(out);

            for (File srcFile : srcFiles) {

                byte[] buf = new byte[BUFFER_SIZE];

                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int len;

                FileInputStream in = new FileInputStream(srcFile);

                while ((len = in.read(buf)) != -1){

                    zos.write(buf, 0, len);

                }

                zos.closeEntry();

                in.close();

            }

            long end = System.currentTimeMillis();

        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils",e);

        }finally{

            if(zos != null){

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    /**

     * 递归压缩方法

     * @param sourceFile 源文件

     * @param zos        zip输出流

     * @param name       压缩后的名称

     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;

     *false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)

     * @throws Exception

     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name,

                                 boolean KeepDirStructure) throws Exception{

        byte[] buf = new byte[BUFFER_SIZE];

        if(sourceFile.isFile()){

            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));

            // copy文件到zip输出流中
            int len;

            FileInputStream in = new FileInputStream(sourceFile);

            while ((len = in.read(buf)) != -1){

                zos.write(buf, 0, len);

            }

            // Complete the entry
            zos.closeEntry();

            in.close();

        } else {

            File[] listFiles = sourceFile.listFiles();

            if(listFiles == null || listFiles.length == 0){

                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){

                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));

                    // 没有文件，不需要文件的copy
                    zos.closeEntry();

                }

            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);

                    } else {

                        compress(file, zos, file.getName(),KeepDirStructure);

                    }

                }

            }

        }

    }

    /**
     * 解压zip文件
     * @param zipFile
     * @param unzipFilePath 解压后存放的路径
     * @return 返回解压的路径文件夹
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static String unzip(File zipFile, String unzipFilePath) throws Exception{
        String unzipPath = "";
        //判断文件是否存在
        if(!zipFile.exists() || zipFile.length()<=0){
            unzipPath="false";
            return unzipPath;
        }
        if(zipFile.length()<=0){
            unzipPath="false";
            return unzipPath;
        }
        //创建解压缩文件保存的路径
        File unzipFileDir = new File(unzipFilePath);
        //的判断文件夹是否存在如果存在则不创建 如果不存在 则创建
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()){
        //创建文件夹
            unzipFileDir.mkdirs();
        }
        //创建解压对象
        ZipEntry zipEntry = null;
        //文件保存路径路径
        String entryFilePath = null;
        //文件夹路径
        String entryDirPath = null;
        //创建问价对象
        File entryFile = null;
        //创建文件夹对象
        File entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        //创建输出字符流
        BufferedInputStream bufferedInputStream = null;
        //创建输入字符流
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //创建压缩文件对象
            ZipFile zip = new ZipFile(zipFile);
            Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();
            //第一步循环创建文件夹 第二步创建文件 第三部写入文件
            while(entries.hasMoreElements()){
                zipEntry = entries.nextElement();
                boolean isDir = zipEntry.isDirectory();
                //当前文件为文件夹
                if(isDir){
                    String dir = zipEntry.getName();
                    entryFilePath = unzipFilePath + dir + "//";
                    //定义文件夹
                    entryDir = new File(entryFilePath);
                    //如果文件夹路径不存在，则创建文件夹
                    if (!entryDir.exists() || !entryDir.isDirectory()){
                        entryDir.mkdirs();
                    }
                }else{
                    //当前是个文件
                    entryFilePath = unzipFilePath + zipEntry.getName();
                    File f = new File(entryFilePath);
                    if (index != -1){
//                        entryDirPath = f.getAbsolutePath().split(f.getName())[0];  //split方法有个bug，就是遇到特殊的字符分割的时候，就会不准确，需要正在表达式比较复杂。。。
                        entryDirPath = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-f.getName().length());
                    }else{
                        entryDirPath = "";
                    }
                    unzipPath = entryDirPath;
                    //定义文件夹
                    entryDir = new File(entryDirPath);
                    //如果文件夹路径不存在，则创建文件夹
                    if (!entryDir.exists() || !entryDir.isDirectory()){
                        entryDir.mkdirs();
                    }
                    //创建解压文件
                    entryFile = new File(entryFilePath);
                    //写入文件
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(entryFile));
                    //读取文件
                    bufferedInputStream = new BufferedInputStream(zip.getInputStream(zipEntry));
                    //文件写入
                    while ((count = bufferedInputStream.read(buffer, 0, bufferSize)) != -1){
                        bufferedOutputStream.write(buffer, 0, count);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            }
        }finally{
            try {
                if(null != bufferedInputStream){
                    bufferedInputStream.close();
                }
                if(null != bufferedOutputStream){
                    bufferedOutputStream.close();
                }
            } catch (Exception e2) {
            }
        }
        return unzipPath;
    }
}