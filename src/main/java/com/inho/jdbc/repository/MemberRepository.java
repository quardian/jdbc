package com.inho.jdbc.repository;

import com.inho.jdbc.domain.Member;

public interface MemberRepository {
    Member save(Member member) ;
    Member findById(String memberId);
    int update(String memberId, int money);
    int delete(String memberId);
}
