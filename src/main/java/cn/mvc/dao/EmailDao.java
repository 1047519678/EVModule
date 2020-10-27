package cn.mvc.dao;

import cn.mvc.pojo.Email;
import cn.mvc.tools.DataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DataSource(value = "dataSource2")
public interface EmailDao {
    List<Email> queryDataByTask(@Param("taskId") int taskId);
}
