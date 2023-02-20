package com.niyf.music.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author niyf
 * @Date 2023-2-20 11:13
 * @Description
 **/
public class FileUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        String musicFolderPath = "车载音乐";
        writeCarMusicNameFile(musicFolderPath);
    }

    /**
     * 获取项目路径
     * @return
     */
    public static String getProjectPath() {
        Path path = Paths.get(FileUtil.class.getResource("/").getPath().substring(1));
        String classesPath = path.toString();
        System.out.println("项目classesPath = " + classesPath);
        int endIdx = classesPath.indexOf("\\target\\classes");
        String projectPath = classesPath.substring(0, endIdx);
        System.out.println("项目projectPath = " + projectPath);

        return projectPath;

        // try {
        //     String music = FileUtil.class.getClassLoader().getResource("carMusicName.txt").toURI().getPath();
        //     System.out.println("music = " + music);
        // } catch (URISyntaxException e) {
        //     throw new RuntimeException(e);
        // }
        // return classesPath;
    }

    /**
     * 写入音乐文件名文件
     * @param musicFolderPath
     */
    public static void writeCarMusicNameFile(String musicFolderPath) {
        String projectPath = getProjectPath();
        File projectFile = new File(projectPath);
        File[] files = projectFile.listFiles(fnf -> musicFolderPath.equals(fnf.getName()));
        if (files.length == 0) {
            throw new RuntimeException("音乐文件夹不存在：" + musicFolderPath);
        }
        File folder = files[0];
        if (!folder.isDirectory()) {
            throw new RuntimeException("音乐文件夹路径有误：" + folder.getPath());
        }

        List<String> fileNameList = Arrays.asList(folder.list());
        System.out.println("音乐文件数目：" + fileNameList.size());

        String fileNameStr = fileNameList.stream().sorted(String::compareToIgnoreCase)
                .collect(Collectors.joining("\n"));
        String fileName = projectPath + "\\carMusicName.txt";
        String dateStr = DATE_FORMAT.format(new Date());
        String dateFileName = String.format("%s\\音乐文件名目录\\carMusicName-%s.txt", projectPath, dateStr);
        try (FileWriter writer = new FileWriter(fileName); FileWriter dateWriter = new FileWriter(dateFileName)) {
            writer.write(fileNameStr);
            System.out.printf("音乐名文件写入完成，文件名：%s%n", fileName);
            dateWriter.write(fileNameStr);
            System.out.printf("音乐名文件写入完成，文件名：%s%n", dateFileName);
        } catch (IOException e) {
            throw new RuntimeException(String.format("写入文件报错，文件名：%s，原因：%s", fileName, e.getMessage()));
        }
    }

}
