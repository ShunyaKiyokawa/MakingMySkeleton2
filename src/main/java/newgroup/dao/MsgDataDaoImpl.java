package newgroup.dao;

import java.util.List;

import javax.persistence.EntityManager;

import newgroup.entity.MsgDataEntity;

public class MsgDataDaoImpl implements MsgDataDaoIF<MsgDataEntity> {

	private EntityManager entityManager;

	public MsgDataDaoImpl(){
		super();
	}
	public MsgDataDaoImpl(EntityManager manager){
		entityManager = manager;
	}

	@Override
	public List<MsgDataEntity> getAll() {
		return entityManager
				.createQuery("from MsgDataEntity")
				.getResultList();
	}

	@Override
	public MsgDataEntity findById(long id) {
		return (MsgDataEntity)entityManager
				.createQuery("from MsgDataEntity where id = "
				+ id).getSingleResult();
	}
}