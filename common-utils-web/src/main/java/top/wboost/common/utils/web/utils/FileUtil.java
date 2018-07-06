/**
 * 
 */
package top.wboost.common.utils.web.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import top.wboost.common.base.enums.CharsetEnum;
import top.wboost.common.util.SystemUtil;

/**
 * 导入导出文件工具类
 * @ClassName: FileUtil
 * @author sjw
 * @date 2016年7月27日 上午10:15:59
 */
public class FileUtil {

    private static Logger log = Logger.getLogger(FileUtil.class);

    /**
     * 导出文件
     * @param @param path 导出文件地址
     * @param @param bytes 文件数据
     * @return void 返回类型
     */
    public static void exportFile(File file, byte[] bytes) {
        OutputStream outputStream = null;
        BufferedOutputStream bis = null;

        try {
            outputStream = new FileOutputStream(file);
            bis = new BufferedOutputStream(outputStream);
            outputStream.write(bytes);
        } catch (Exception e) {
            if (e instanceof java.io.FileNotFoundException) {
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    try {
                        file.createNewFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                exportFile(file, bytes);
            } else {
                log.error("写出文件出错,原因为:" + e);
            }
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 导出相对于classes文件夹文件
     * @param path
     * @param bytes
     */
    public static void exportFileToClasspath(String path, byte[] bytes) {
        String realPath = FileUtil.class.getResource("/").getPath() + path;
        File createFile = new File(
                realPath.replace("/", SystemUtil.FILE_SEPARATOR).replace("\\", SystemUtil.FILE_SEPARATOR));
        exportFile(createFile, bytes);
    }

    /**
     * 导出基于项目的文件
     * @param path
     * @param bytes
     */
    public static void exportFileToProject(String path, byte[] bytes) {
        String filePath = FileUtil.class.getResource("/").getPath();
        String realPath = new File(filePath).getParentFile().getParentFile().getPath() + path;
        File createFile = new File(
                realPath.replace("/", SystemUtil.FILE_SEPARATOR).replace("\\", SystemUtil.FILE_SEPARATOR));
        exportFile(createFile, bytes);
    }

    public static String importFileFromProject(String path) {
        String filePath = FileUtil.class.getResource("/").getPath();
        String realPath = new File(filePath).getParentFile().getParentFile().getPath() + path;
        File createFile = new File(
                realPath.replace("/", SystemUtil.FILE_SEPARATOR).replace("\\", SystemUtil.FILE_SEPARATOR));
        return importFile(createFile);
    }

    public static String importFileFromClasspath(String path) {
        String realPath = FileUtil.class.getResource("/").getPath() + path;
        File createFile = new File(
                realPath.replace("/", SystemUtil.FILE_SEPARATOR).replace("\\", SystemUtil.FILE_SEPARATOR));
        return importFile(createFile);
    }

    /**
     * 导入文件数据，编码为 ISO-8859-1
     * @param @param path 文件地址
     * @param @return 设定文件
     * @return String 返回类型
     */
    public static String importFile(File file) {
        try {
            return importFile(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String importFile(File file, CharsetEnum charset) {
        try {
            return importFile(new FileInputStream(file), charset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 导入文件数据，编码为 ISO-8859-1
     * @param @param path 文件地址
     * @param @return 设定文件
     * @return String 返回类型
     */
    public static String importFile(MultipartFile file) {
        try {
            return importFile(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String importFile(MultipartFile file, CharsetEnum charset) {
        try {
            return importFile(file.getInputStream(), charset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String importFile(InputStream inputStream) {
        return importFile(inputStream, CharsetEnum.ISO_8859_1);
    }

    public static String importFile(InputStream inputStream, CharsetEnum charset) {
        BufferedInputStream bis = null;
        StringBuffer stringBuffer = null;

        try {
            bis = new BufferedInputStream(inputStream);
            int len = 0;
            byte[] b = new byte[1024];
            stringBuffer = new StringBuffer();
            while ((len = bis.read(b)) != -1) {
                stringBuffer.append(new String(b, 0, len, charset.getName()));
            }
        } catch (Exception e) {
            log.error("读取文件出错，原因为:" + e);
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(inputStream);
        }

        return stringBuffer.toString();
    }

    public static String importFileNio(FileInputStream inputStream, CharsetEnum charset) {
        byte[] result = null;
        try (FileChannel channel = inputStream.getChannel()) {
            long allByteLength = channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int nowByteLength = 0;
            result = new byte[Integer.parseInt(String.valueOf(allByteLength))];
            while (channel.read(byteBuffer) != -1) {
                int position = byteBuffer.position();
                System.arraycopy(byteBuffer.array(), 0, result, nowByteLength, position);
                nowByteLength += position;
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(result, charset.getCharset());
    }

    /**
     * 导入文本
     * @param @param path
     * @param @return 设定文件
     * @return List<String> 返回类型
     */
    public static List<String> importText(String path) {
        File file = new File(path);
        try {
            return importText(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> importText(InputStream inputStream) {
        Reader reader = null;
        BufferedReader br = null;
        List<String> list = null;
        try {
            reader = new InputStreamReader(inputStream);
            br = new BufferedReader(reader);
            list = new ArrayList<String>();
            String str = "";
            while ((str = br.readLine()) != null) {
                list.add(new String(str.getBytes(), CharsetEnum.ISO_8859_1.getCharset()));
            }
        } catch (Exception e) {
            log.error("读取文件出错，原因为:" + e);
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(reader);
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(FileUtil
                .importFileNio(new FileInputStream(new File("C:\\Users\\jwsun\\Desktop\\json.txt")), CharsetEnum.UTF_8)
                .replaceAll("(\r\n|\r|\n|\n\r|\t)", "").replaceAll("\\s+", ""));

    }
}
