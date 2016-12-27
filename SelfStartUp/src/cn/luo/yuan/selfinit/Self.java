package cn.luo.yuan.selfinit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gluo on 12/27/2016.
 */
public class Self {
    public static void main(String... args) {
        String selfPath = "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp";//这个是系统的自启动目录
        File dir = new File(selfPath);
        if (dir.exists()) {
            File bat = new File("startup.bat");

            try {
                if (!bat.exists()) {
                    bat.createNewFile();
                }//创建bat文件用来启动jar程序
                FileWriter writer = new FileWriter(bat);
                writer.write("java -jar " + "main.jar");//这是一个范例，替换jar的名字为你的jar名字
                writer.flush();
                writer.close();
                writer = new FileWriter(selfPath + "\\main_startup.vbs");//注册一个脚本到系统的自启动目录下，这个脚本指向之前创建的那个bat文件
                writer.write("createobject(\"wscript.shell\").run \"" + bat.getAbsolutePath() + "\",0 ");
                writer.flush();
                writer.close();
                //z注册成功;
                return;
            } catch (IOException e) {
                e.printStackTrace();
                //权限错误，需要管理员权限
            }
        } else {
            //自启动的文件夹路径错误，可能是操作系统版本的问题
        }
    }
}
