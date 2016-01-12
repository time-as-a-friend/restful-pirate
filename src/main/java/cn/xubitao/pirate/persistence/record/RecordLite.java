package cn.xubitao.pirate.persistence.record;

import cn.xubitao.dolphin.sqllite.Dolphin;
import cn.xubitao.pirate.domain.record.Record;
import cn.xubitao.pirate.domain.record.Records;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * Created by xubitao on 12/25/15.
 */
@Service
public class RecordLite implements RecordPersistence {
    @Resource
    private Dolphin dolphin;
    private Dao<Record, Integer> projectDAO;

    public Record create(Record record) throws SQLException {
        return getProjectDAO().createIfNotExists(record);
    }

    public Records loadAll(Integer contractId, Integer isHit, String consumerKey) throws SQLException {
        QueryBuilder<Record, Integer> queryBuilder = getProjectDAO().queryBuilder();
        Where where = queryBuilder.where();
        where.eq("deleteStatus", 0);
        if (contractId != null) {
            where.and().eq("contractId", contractId);
        }
        if (isHit != null) {
            where.and().eq("isHit", isHit);
        }
        if (consumerKey != null) {
            where.and().like("consumerKey", "%" + consumerKey + "%");
        }
        queryBuilder.setWhere(where);
        queryBuilder.query();
        Records records = new Records();
        records.setRecords(queryBuilder.orderBy("id", false).query());
        return records;
    }

    public Dao<Record, Integer> getProjectDAO() {
        if (projectDAO == null) {
            projectDAO = dolphin.lite(Record.class);
        }
        return projectDAO;
    }
}
