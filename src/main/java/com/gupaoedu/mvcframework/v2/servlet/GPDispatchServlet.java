package com.gupaoedu.mvcframework.v2.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GPDispatchServlet extends HttpServlet {

    private Properties contextConfig = new Properties();
    //享元模式，缓存
    private List<String> classMames = new ArrayList<String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //6、委派,根据URL去找到一个对于的Method并通过response返回
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1、加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        //2、扫描相关的类
        doScanner(contextConfig.getProperty("scanPackage"));

        //=========IOC部分=========
        //3、初始化IOC容器,将扫描到的相关的类实例化，保存到IOC容器中
        doInstance();

        //AOP，新生成的代理对象

        //=========DI部分=========
        //4、完成依赖注入
        doAutowired();

        //5、初始化HandlerMapping
        doInitHalderMappint();

        System.out.println("mini Spring framework is init");
    }

    private void doInitHalderMappint() {
    }

    private void doAutowired() {
    }

    private void doInstance() {
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/"+ scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        //当成是一个ClassPath文件夹
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if (file.getName().endsWith(".class")){
                    String classqName = scanPackage + "." + file.getName().replace(".class", "");
                    //Class.forName(classpName)
                    classMames.add(classqName);
                }
            }

        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
