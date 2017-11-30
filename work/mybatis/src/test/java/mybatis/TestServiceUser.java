package mybatis;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import mybatis.inf.IServiceUser;
import mybatis.model.ModelUser;
import mybatis.svr.ServiceUser;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestServiceUser {

    private static IServiceUser service = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // classpath 를 이용한 설정 파일 로딩
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ApplicationContext.xml");

        // DI를 이용한 serviceuser 인스턴스 생성
        service = context.getBean("serviceuser", ServiceUser.class);
        
        // DB Table 초기화 코드
        javax.sql.DataSource dataSource = (DataSource) context.getBean("dataSource");
        org.apache.ibatis.jdbc.ScriptRunner runner = new
        org.apache.ibatis.jdbc.ScriptRunner( dataSource.getConnection() );
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        String sf = cl.getResource("ddl/board.ddl.mysql.sql").getFile();
        java.io.Reader br = new java.io.BufferedReader( new java.io.FileReader(sf) );
        runner.runScript( br);
        runner.closeConnection();
    }

    @Test
    public void test01_InsertUser() throws Exception {
        LocalDateTime dt = LocalDateTime.now();
        String s = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ModelUser user = new ModelUser();
        user.setUserid("222");
        user.setEmail("222@222.com");
        user.setPasswd("222");
        user.setName("222");
        user.setMobile("010-222-222");
        user.setRetireYN(true);
        user.setInsertUID("222");
        user.setInsertDT(s);
        System.out.println("TestServiceUser : " + user.toString());
        int rs = service.insertUser(user);
        assertTrue(rs >= 0);
    }

    @Test
    public void test02_Login() throws Exception {
        List<ModelUser> rs = null;

        ModelUser user = new ModelUser();
        user.setUserid("userid");
        user.setPasswd("password");
        rs = service.login(user);

        assertEquals(1, rs.size());
        assertEquals("userid", rs.get(0).getUserid());
    }

    @Test
    public void test03_Logout() {
        // fail("Not yet implemented");
    }

    @Test
    public void test04_UpdatePasswd() throws Exception {
        int rs = -1;

        String newPasswd = "1004";
        String currentPasswd = "password";
        String userid = "userid";

        rs = service.updatePasswd(newPasswd, currentPasswd, userid);
        assertEquals(1, rs);
    }
    
    @Test
    public void test05_UpdateUserInfo() throws Exception {
        int rs = -1;
        ModelUser updateValue = new ModelUser();
        updateValue.setEmail("333@333.com");
        updateValue.setPasswd("333");
        updateValue.setName("333");
        updateValue.setMobile("010-333-333");
        ModelUser searchValue = new ModelUser();
        searchValue.setUserid("userid");
        rs = service.updateUserInfo(updateValue, searchValue);
        assertEquals(1, rs);
    }

    @Test
    public void test06_DeleteUser() throws Exception {
        int rs = -1;

        ModelUser user = new ModelUser();
        user.setUserid("userid");
        System.out.println("TestServiceUser : " + user.toString());
        rs = service.deleteUser(user);
        assertEquals(1, rs);
    }

    @Test
    public void test07_SelectUserOne() throws Exception {
        List<ModelUser> rs = null;
        ModelUser user = new ModelUser();
        user.setUserno(1);
        rs = service.selectUserOne(user);
        assertEquals(1, rs.size());
        assertSame(1, rs.get(0).getUserno());
    }

    @Test
    public void test08_SelectUserList() throws Exception {
        List<ModelUser> rs = null;
        ModelUser user = new ModelUser();
        user.setUserid("s");
        rs = service.selectUserList(user);
        
        assertEquals(1, rs.size());
        assertEquals("userid", rs.get(0).getUserid());
    }

    @Test
    public void test09_Checkuserid() throws Exception {
        int rs = -1;
        String userid = "userid";
        rs = service.checkuserid(userid);
        assertEquals(1, rs);
    }

}
