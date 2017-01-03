package newgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import newgroup.entity.MsgDataEntity;

public interface MsgDataRepository
	extends JpaRepository<MsgDataEntity, Long> {

}