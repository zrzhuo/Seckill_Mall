package com.zhuo.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.vo.RespBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 批量生成用户, 用于压力测试
 */

public class UserUtil {
    private static Connection getConn() throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "zrzhuo";
        String password = "123456";
        Class.forName(driver);//这里为什么要有这个反射: 为了自动注册驱动
        return DriverManager.getConnection(url, username, password);
    }
    private static void createUser(int count) throws SQLException, ClassNotFoundException, IOException {
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(String.valueOf(13939100000L + i));
            user.setNickname("user_" + i);
            user.setSlat("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSlat()));
            user.setRegisterDate(new Date());
            user.setLastLoginDate(new Date());
            user.setLoginCount(0);
            users.add(user);
        }
        System.out.println("创建用户完成！");
        //创建连接
        Connection conn = getConn();
        //准备sql语句
        String sql = "insert into t_user(login_count, nickname, register_date, slat, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(User user : users) {
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(4, user.getSlat());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("插入数据库完成!");

        //登录，获取并保存userTicket（应该就是token）
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("/Users/zrzhuo/Desktop/代码/java/seckill/src/main/resources/users.txt");
        if(file.exists())
            file.delete();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (User user : users) {
            //登录一个用户
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();

            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();

            String response = bout.toString();
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObj();

            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
        }
        raf.close();
        System.out.println("over");
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        UserUtil.createUser(2000);
    }
}
