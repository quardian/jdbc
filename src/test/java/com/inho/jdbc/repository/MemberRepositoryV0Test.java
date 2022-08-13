package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException
    {
        // 등록(C)
        Member member = new Member("memberV4", 10000);
        repository.save(member);

        // 검색(R)
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        // 수정(U)
        repository.update(member.getMemberId(), 50000);
        Member updateMember = repository.findById(member.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(50000);
        
        // 삭제(D)
        repository.delete(member.getMemberId());
        Assertions.assertThatThrownBy( ()-> repository.findById(member.getMemberId()) )
                .isInstanceOf(NoSuchElementException.class);

    }
}