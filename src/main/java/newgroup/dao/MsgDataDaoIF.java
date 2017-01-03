package newgroup.dao;

import java.util.List;

import newgroup.entity.MsgDataEntity;

public interface MsgDataDaoIF<T> {

	public List<MsgDataEntity> getAll();
	public MsgDataEntity findById(long id);

}