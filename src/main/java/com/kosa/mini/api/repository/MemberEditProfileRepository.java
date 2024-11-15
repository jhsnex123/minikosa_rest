package com.kosa.mini.api.repository;

import com.kosa.mini.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEditProfileRepository extends JpaRepository<Member,Integer> {
}
