package org.ikigaidigital.domain.repository;

import org.ikigaidigital.domain.entity.TimeDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeDepositRepository extends JpaRepository<TimeDeposit, Integer> {
}
