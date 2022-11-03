package poo0054;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import poo0054.constant.FileConstant;
import poo0054.dao.TableAttributeDao;
import poo0054.entity.TableAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/11/3 14:22
 */
public class MybatisTest {
    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionFactory testSessionFactory;

    @Before
    public void init() {
        try {
            InputStream inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
            //构建configuration 并创建出默认的 sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, null, null);

            //使用另外一套数据源
            InputStream resourceAsStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
            testSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream, "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryAllByLimit() {
        //每次都是一个新的sqlSession  其中 executor 匹配成功就是一个代理对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            //从mapperRegistry中获取MapperProxy代理对象
            TableAttributeDao roleMapper = sqlSession.getMapper(TableAttributeDao.class);
            //执行该代码 真正会执行MapperProxy的invoke方法
            TableAttribute attribute = new TableAttribute(null, null, null);
            attribute.setTableCode("package");
            List<TableAttribute> sysUser = roleMapper.queryAllByLimit(attribute);
            for (TableAttribute tableAttribute : sysUser) {
                System.out.println(tableAttribute);
            }
        }
    }

}