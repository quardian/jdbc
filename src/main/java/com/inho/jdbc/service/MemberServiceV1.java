package com.inho.jdbc.service;

import com.inho.jdbc.domain.Member;
import com.inho.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember   = memberRepository.findById(fromId);
        Member toMember     = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId,   fromMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외발생");
        }
    }

}
